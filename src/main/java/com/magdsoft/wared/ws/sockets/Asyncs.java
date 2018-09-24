package com.magdsoft.wared.ws.sockets;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magdsoft.wared.ws.CarLocationDetailed;
import com.magdsoft.wared.ws.UserLocationDetailed;
import com.magdsoft.wared.ws.model.TheOrder;
import com.magdsoft.wared.ws.model.User;
import com.magdsoft.wared.ws.sockets.GenerateMessagesCommon.TripStatus;

@Service("asyncs")
public class Asyncs {
	@Autowired
	public SocketDataStructure socketDataStructure;

	@Autowired
	public GenerateMessagesCommon generateMessagesCommon;

	@Autowired
	public GenerateMessagesToUser generateMessagesToUser;

	@Autowired
	public GenerateMessagesToDriver generateMessagesToDriver;

	@Autowired
	public DatabaseHandler databaseHandler;

	@Autowired
	public Sender sender;

	public static int getTimeToWait(int type) {
		return 15000;
	}
	
	@Transactional
	@Async
	public void carGet(User user, TheOrder order) {
		final int tripId = order.getId();
		final int userId = user.getId();
		// Associate trip with user
		socketDataStructure.userDataStructure.getById(userId).setTripId(tripId);
		
		// Instantiate a new car picker and tell it what to send to user, or car according to
		//   the data from the web service
		new CarPicker() {

			@Override
			public void sendToUser(int userId, TripStatus tripStatusChange, String reason) {
				sender.sendJsonToWebSocketSessionAsync(
					socketDataStructure.userDataStructure.getById(userId).getSession(),
					generateMessagesCommon.prepareTripStatusMessage(reason, tripStatusChange.toString(), tripId)
				);
			}
			
			@Override
			public void sendToCar(CarLocationDetailed car) {
				sender.sendJsonToWebSocketSessionAsync(
					car.getSession(),
					generateMessagesToDriver.generateRequestMessage(user, order)
				);
			}
			
			@Override
			public SocketDataStructure getSocketDataStructure() {
				return socketDataStructure;
			}
			
			@Override
			public int getCarType() {
				// No car types in Wared
				return 0;
			}
			
			@Override
			public int durationToWait() {
				return 15000;
			}
			
			@Override
			public double carRanges() {
				return 0.906;
			}
		}.sendRequestToNearCars(socketDataStructure.userDataStructure.getById(user.getId()));
	}
	
	@Async
	public void cancelTrip(int userId, int driverId) {
		endOrCancelTrip(userId, driverId, TripStatus.CANCELLED);
	}

	@Async
	public void reachUser(int userId, int driverId) {
		endOrCancelTrip(userId, driverId, TripStatus.REACHED);
	}

	public void endOrCancelTrip(int userId, int driverId, TripStatus status) {
		try {	
			UserLocationDetailed user = socketDataStructure.userDataStructure.getById(userId);
			// ID for car is the driver id
			CarLocationDetailed car = socketDataStructure.carsDataStructure.getById(driverId);

			int tripId = car.getTripId();
			
			user.setTripId(null);
			user.setDriverId(null);
			car.setTripId(null);
			car.setUserId(null);

			Map<String, Object> dataToSend = generateMessagesCommon.prepareTripStatusMessage(status.toString(), null, tripId);

			sender.sendJsonToWebSocketSessionAsync(user.getSession(), dataToSend);
			sender.sendJsonToWebSocketSessionAsync(car.getSession(), dataToSend);

			databaseHandler.stopTheTrip(tripId, status);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}


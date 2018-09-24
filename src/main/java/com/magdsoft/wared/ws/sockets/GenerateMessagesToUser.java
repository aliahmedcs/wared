package com.magdsoft.wared.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magdsoft.wared.ws.CarLocationDetailed;
import com.magdsoft.wared.ws.LatLngBearing;
import com.magdsoft.wared.ws.model.Driver;
import com.magdsoft.wared.ws.model.TheOrder;

@Service
public class GenerateMessagesToUser {

	public enum Status {
		ADDED,
		REMOVED;

		@Override
		public String toString() {
			switch(this) {
			case ADDED:
				return "New location";
			case REMOVED:
				return "Old location";
			default:
				return "NOTHING";
			}
		}
	}

	/**
	 * The object which handles database processes related to the sockets
	 */
	@Autowired
	private DatabaseHandler databaseHandler;

	/**
	 * Prepare car location update to send to user by given id and LatLngBearing
	 *
	 * @param carSentStatus The status whether is it a new location or an old one
	 * @param location      The car location to send
	 * @param carLocation   The car data structure
	 * @param time		Time estimated to be sent if he is on trip
	 * @param onTrip	Whether the message sent in "on-trip" style
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareCarByLocationToUserMessage(CarLocationDetailed carLocation,
		       	LatLngBearing location, Status carSentStatus, int time, boolean onTrip) {

		Map<String, Object> dataToSend = new HashMap<>();

		if (!onTrip) {
			dataToSend.put("type", 0);
			dataToSend.put("status", carSentStatus.toString());
			dataToSend.put("driverId", carLocation.getDriverId());
			dataToSend.put("bearing", location.getBearing());
			dataToSend.put("longitude", location.getLongitude());
			dataToSend.put("latitude", location.getLatitude());
			return dataToSend;
		} else {
			// The alternation between lat and lng is intented because the app
			//    is already on.
			// Delegate to the other method
			return prepareLocationChangeMessage(carLocation.getId(),
				       carLocation.getLocation().getBearing(),
				       carLocation.getLocation().getLongitude(),
				       carLocation.getLocation().getLatitude(),
				       time);
		}
	}

	/**
	 * Prepare car location update to send to user
	 *
	 * @param carLocation   The car location to send
	 * @param carSentStatus The status whether is it a new location or an old one
	 * @param time		Time estimated to be sent if he is on trip
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareCarToUserMessage(CarLocationDetailed carLocation,
		       	Status carSentStatus, int time, boolean onTrip) {
		return prepareCarByLocationToUserMessage(carLocation,
			       	carLocation.getLocation(),
			       	carSentStatus, time, onTrip);
	}

	/**
	 * Prepare message to be sent at driver acceptance of a trip
	 *
	 * @param driverId Driver ID
	 * @param tripId   Trip ID
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareDriverToUserByTripMessage(CarLocationDetailed car, int tripId,double time) {
		Driver driver = databaseHandler.getDriverData(car.getId());
		TheOrder trip = databaseHandler.getOrderById(tripId);
		Map<String, Object> dataToSend = new HashMap<>();
		dataToSend.put("type", 1);
		dataToSend.put("tripId", tripId);
		dataToSend.put("status", "accepted");
		dataToSend.put("cost", trip.getCost());
        	dataToSend.put("time",time);
		dataToSend.put("latitude", trip.getLatitude());
		dataToSend.put("longitude", trip.getLongitude());
		dataToSend.put("address", trip.getAddress());
		dataToSend.put("driverId", driver.getId());
		dataToSend.put("driverName", driver.getName());
		dataToSend.put("driverPhone", driver.getPhone());
		dataToSend.put("driverUserImage", driver.getUserImage());

		return dataToSend;
	}

	/**
	 * Prepare location change message to send to the user associated with the given trip id
	 * This happens during a trip only
	 *
	 * @param userId	The driver id
	 * @param bearing	The bearing
	 * @param longitude	The location's longitude
	 * @param latitude	The location's latitude
	 * @param time		The time estimated to reach the user (in Wared only)
	 * @return Message to be sent by using the Sender service
	 */

	public Map<String, Object> prepareLocationChangeMessage(int driverId, double bearing, double latitude, double longitude, int time) {

		// Make the message as a map for Spring to generate as JSON
		Map<String, Object> theMap = new HashMap<>();
		theMap.put("driverId", driverId);
		theMap.put("bearing", bearing);
		theMap.put("longitude", longitude);
		theMap.put("latitude", latitude);
		theMap.put("time", time);
		theMap.put("type", 10);
		// Return the prepared message
		return theMap;

	}

}

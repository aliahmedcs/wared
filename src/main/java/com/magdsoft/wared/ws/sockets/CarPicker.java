package com.magdsoft.wared.ws.sockets;

import java.util.List;

import com.magdsoft.wared.ws.CarLocationDetailed;
import com.magdsoft.wared.ws.LatLngBearing;
import com.magdsoft.wared.ws.UserLocationDetailed;
import com.magdsoft.wared.ws.sockets.GenerateMessagesCommon.TripStatus;
import com.magdsoft.wared.ws.sockets.LocationAwareObjectDAO.Distances;

/**
 * The object from this class handles the trip requests.
 * When a trip is requested, the request goes to this object to be handled.
 * This object will search for near cars, then send the request to the nearest
 *   and wait for response. If accepted OK, if rejected or timeout, goes to
 *   the next nearest one. If no one accepted in this range, it repeats the
 *   process one more time, then if no car accepted. It sends cancellation
 *   to the user.
 *
 */
public abstract class CarPicker {
	
	/**
	 * Gets the socket data structure.
	 *
	 * @return SocketDataStructure
	 */
	public abstract SocketDataStructure getSocketDataStructure();

	/**
	 * Get range to search the car with.
	 *
	 * @return The range
	 */
	public abstract double carRanges();

	/**
	 * Gets the car type to use
	 *
	 * @return The car type id
	 */
	public abstract int getCarType();

	/**
	 * Duration that this object will wait for a car to accept a request.
	 * After which the request is considered rejected.
	 * 
	 * @return The duration in milliseconds
	 */
	public abstract int durationToWait();

	/**
	 * Sends the request to a car. Must be overriden to make it really send
	 *
	 * @param car      The car location data object
	 */
	public abstract void sendToCar(CarLocationDetailed car);

	/**
	 * Send a trip status change message to user. Must be overriden to make it really send
	 *
	 * @param userId 
	 * @param tripId
	 * @param tripStatusChange 
	 * @param reason If there is a reason, what is it. May be null
	 */
	public abstract void sendToUser(int userId, GenerateMessagesCommon.TripStatus tripStatusChange, String reason);

	/**
	 * Here the operation happens
	 *
	 * @param user     The user location object (typically from the DAO), with its trip id set
	 */
	public void sendRequestToNearCars(UserLocationDetailed user) {
		LatLngBearing userLocation = user.getLocation();
		SocketDataStructure socketDataStructure = getSocketDataStructure();
		boolean isCarAvailable = false;
		int iterations = 0;
		while(!isCarAvailable && (iterations < 2)) {
			iterations++;
			List<Distances<CarLocationDetailed>> distance = 
				 socketDataStructure.getNearCars(getCarType(), userLocation.getLatitude(), userLocation.getLongitude());
			// If no cars, make a new iteration
			if (distance.isEmpty()) { continue; }

			// Loop in cars until finding an available one
			// The car is available if no trip associated with it
			for(Distances<CarLocationDetailed> currentCarWithDistance : distance) {
				CarLocationDetailed currentCar = currentCarWithDistance.getValue();
				if (currentCar.getTripId() == null) {
					sendToCar(currentCar);
					currentCar.addATripRequest(user);

					try {
						synchronized(currentCar) {
							currentCar.wait(durationToWait());
						}
					} catch(InterruptedException ex) {
					}

					// After waiting for reply, see whether the driver accepted, the user cancelled or what happened really

					// Now trip id for this user is null? The trip is likely cancelled
					if (user.getTripId() == null)
						return;
					
					// Test if car is now on this trip for the same user
					if (user.getTripId().equals(currentCar.getTripId()) && (user.getId().equals(currentCar.getUserId()))) {
						// The operation ends here
						return;
					}

				}
			}
			// No cars, or no available ones?
			sendToUser(user.getId(), iterations == 0 ? TripStatus.SEARCHING_AGAIN : TripStatus.CANCELLED, "no-available-cars");
			user.setTripId(null);
			user.setDriverId(null);
		}
	}
}


package com.magdsoft.wared.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("generateMessagesCommon")
public class GenerateMessagesCommon {

	public enum TripStatus {
		CANCELLED,
		SEARCHING_AGAIN,
		ACCEPTED_BY_DRIVER,
		REACHED;

		public String toString() {
			switch(this) {
				case CANCELLED: return "trip-cancelled";
				case SEARCHING_AGAIN: return "searching-again";
				case ACCEPTED_BY_DRIVER: return "trip-accepted-by-driver";
				case REACHED: return "trip-reached";
				default: return "UNKNOWN";
			}
		}
	}

	/**
	 * Prepare trip status change message
	 *
	 * @param message	Message (maybe returned from GenerateMessagesToDriver#TripStatus#toString)
	 * @param reason	The reason of this message, null if no reason
	 * @param tripId	The tripId
	 * @return Message to be sent by using the Sender service
	 */
	public Map<String, Object> prepareTripStatusMessage(String message, String reason, int tripId) {

		// Make the message as a map for Spring to generate as JSON
		Map<String, Object> theMap = new HashMap<>();
		theMap.put("message", message);

		if (null != reason) theMap.put("reason", reason);

		theMap.put("tripId", tripId);
		// Return the prepared message
		return theMap;

	}
}


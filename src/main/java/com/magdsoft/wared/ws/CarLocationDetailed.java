package com.magdsoft.wared.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.WebSocketSession;

public class CarLocationDetailed implements LocationAndSessionAware { 

	private LatLngBearing location;

	private int driverId;
	private int carTypeId;
	private int carId;
	// This is nullable because the car may be not on trip (it is available)
	private Integer tripId;
	// The user id associated with this driver when on a trip
	private Integer userId;
	private WebSocketSession session;

	/**
	 * The current trip requests. As a map mapping USER id to issuing time.
	 * You can get trip id from user id.
	 */
	public final Map<Integer, Date> currentRequestsWithIssuingTime = new HashMap<>();

	/**
	 * @param driverId
	 * @param carTypeId
	 * @param session
	 */
	public CarLocationDetailed(int driverId, int carTypeId, 
			WebSocketSession session) {
		this.driverId = driverId;
		this.carTypeId = carTypeId;
		this.session = session;
	}

	/**
	 * @return the location
	 */
	public LatLngBearing getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(LatLngBearing location) {
		this.location = location;
	}

	/**
	 * @return the driverId
	 */
	public int getDriverId() {
		return driverId;
	}

	/**
	 * @param driverId the driverId to set
	 */
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	/**
	 * @return the carTypeId
	 */
	public int getCarTypeId() {
		return carTypeId;
	}

	/**
	 * @param carTypeId the carTypeId to set
	 */
	public void setCarTypeId(int carTypeId) {
		this.carTypeId = carTypeId;
	}

	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}

	/**
	 * @return the tripId
	 */
	public Integer getTripId() {
		return tripId;
	}

	/**
	 * @param tripId the tripId to set
	 */
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the session
	 */
	public WebSocketSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public CarLocationDetailed() {
		
	}

	@Override
	public Integer getId() {
		return getDriverId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carId;
		result = prime * result + carTypeId;
		result = prime * result + driverId;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((session == null) ? 0 : session.getId().hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarLocationDetailed other = (CarLocationDetailed) obj;
		if (carId != other.carId)
			return false;
		if (carTypeId != other.carTypeId)
			return false;
		if (driverId != other.driverId)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (session == null) {
			if (other.session != null)
				return false;
		} else if (!session.getId().equals(other.session.getId()))
			return false;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int compareTo(LocationAware o) {
		int byLocation = this.getLocation().compareTo(o.getLocation());
		if (byLocation != 0) return byLocation;
		if (!(o instanceof CarLocationDetailed)) { return 1; }
		CarLocationDetailed other = (CarLocationDetailed) o;
		if (carId < other.carId) return -1;
		if (carId > other.carId) return 1;
		if (tripId < other.tripId) return -1;
		if (tripId > other.tripId) return 1;
		return 0;
	}

	/**
	 * Clears all trip requests
	 *
	 */
	@Async
	public void clearRequests() {
		this.currentRequestsWithIssuingTime.clear();
	}

	/**
	 * Associate this car with a trip and user (Typically when user accepts a trip).
	 *
	 * @param user The user location object, this object must have the trip id set
	 */
	public void associateWithATripAndUser(UserLocationDetailed user) {
		clearRequests();

		this.setUserId(user.getId());
		this.setTripId(user.getTripId());

		user.setDriverId(this.getId());
	}

	/**
	 * Add a trip request to this car using the given user info object.
	 *
	 * @param userForTripRequest user location object which must have trip id set
	 */
	public void addATripRequest(UserLocationDetailed userForTripRequest) {
		this.currentRequestsWithIssuingTime.put(userForTripRequest.getUserId(), new Date());
	}

	/**
	 * Remove a trip request from this car using the given user info object.
	 *
	 * @param userForTripRequest user location object which must have trip id set
	 */
	public void removeATripRequest(UserLocationDetailed userForTripRequest) {
		this.currentRequestsWithIssuingTime.remove(userForTripRequest.getUserId());
	}
}

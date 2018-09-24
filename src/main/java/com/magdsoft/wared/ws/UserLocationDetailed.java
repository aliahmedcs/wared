package com.magdsoft.wared.ws;

import org.springframework.web.socket.WebSocketSession;

public class UserLocationDetailed implements LocationAndSessionAware { 

	private LatLngBearing location;

	private int userId;
	// This is nullable because the car may be not on trip (it is available)
	private Integer tripId;
	// The driver id associated with this user when on a trip
	private Integer driverId;
	private WebSocketSession session;

	/**
	 * @param userId
	 * @param session
	 */
	public UserLocationDetailed(int userId, WebSocketSession session) {
		this.userId = userId;
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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
	 * @return the driverId
	 */
	public Integer getDriverId() {
		return driverId;
	}

	/**
	 * @param driverId the driverId to set
	 */
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
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

	public UserLocationDetailed() {
		
	}

	@Override
	public Integer getId() {
		return getUserId();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((session == null) ? 0 : session.getId().hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result + userId;
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
		UserLocationDetailed other = (UserLocationDetailed) obj;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
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
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public int compareTo(LocationAware o) {
		int byLocation = this.getLocation().compareTo(o.getLocation());
		if (byLocation != 0) return byLocation;
		if (!(o instanceof UserLocationDetailed)) { return 1; }
		UserLocationDetailed other = (UserLocationDetailed) o;
		if (userId < other.userId) return -1;
		if (userId > other.userId) return 1;
		if (tripId < other.tripId) return -1;
		if (tripId > other.tripId) return 1;
		return 0;
	}
	
}

package com.magdsoft.wared.ws.model;

public class Request {
	private String apiToken;
	private Double latitude;
	private Double longitude;
	private String address;
	private String city;

	/**
	 * @return the apiToken
	 */
	public String getApiToken() {
		return apiToken;
	}

	/**
	 * @param apiToken the apiToken to set
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

}

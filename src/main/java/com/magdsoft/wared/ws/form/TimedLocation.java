package com.magdsoft.wared.ws.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class TimedLocation {
	
	private String apiToken;
	@NotNull
	private Double longitude;
	
	@NotNull
	private Double latitude;

	
    
	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}

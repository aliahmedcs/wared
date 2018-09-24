package com.magdsoft.wared.ws.form;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;


public class UpdateData {
	private String name;
	@Column(unique=true)
	private String phone;
//	private String password;

	private MultipartFile userImage;
//	private Boolean isActive;
	
	@Email
	private String email;
//	private Integer rate;
	private String address;
	private Double latitude;
	private Double longitude;
	private String apiToken;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public MultipartFile getUserImage() {
		return userImage;
	}
	public void setUserImage(MultipartFile userImage) {
		this.userImage = userImage;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
}

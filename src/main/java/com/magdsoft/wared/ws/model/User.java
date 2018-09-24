package com.magdsoft.wared.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private String name;
	private String password;
	@Column(unique=true)
	private String phone;

	private String userImage;
	@Column(columnDefinition = "Boolean default false")
	private Boolean isActive;
	private Integer veryficationCode;
    @Email
	private String email;
//	private Integer rate;
	private String address;
	private Double latitude;
	private Double longitude;
	@Column(unique=true)
	private String apiToken;
	@OneToMany(mappedBy="user_id")
	private List<TheOrder> theOrder_id=new ArrayList();
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
	
	
	
	
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<TheOrder> getTheOrder_id() {
		return theOrder_id;
	}
	public void setTheOrder_id(List<TheOrder> theOrder_id) {
		this.theOrder_id = theOrder_id;
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
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	
	public Integer getVeryficationCode() {
		return veryficationCode;
	}
	public void setVeryficationCode(Integer veryficationCode) {
		this.veryficationCode = veryficationCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}

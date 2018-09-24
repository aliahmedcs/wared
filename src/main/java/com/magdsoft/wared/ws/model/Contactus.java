package com.magdsoft.wared.ws.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Contactus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String message;
	private String title ;
	@OneToOne
	private User userId;
	
	@OneToOne
	private Driver driverId;
	
	
	public Driver getDriverId() {
		return driverId;
	}
	public void setDriverId(Driver driverId) {
		this.driverId = driverId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	private String type;
	private String apiToken;
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;
	
	
	
	public String getApiToken() {
		return apiToken;
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
	public Integer getId() {
		return id;
	}
	public User getUserId() {
		return userId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

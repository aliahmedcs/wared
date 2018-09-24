package com.magdsoft.wared.ws.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
//import com.magdsoft.wared.ws.model.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//import com.magdsoft.wared.ws.model.Driver.Status;

@Entity
public class TheOrder {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	@ManyToOne(cascade=CascadeType.ALL)
	private User user_id;
	private Double cost;
	@OneToOne
	private Driver driver_id;
	private Double longitude;
	private Double latitude;
	private String address;
	//private Boolean isActive;
	//public enum Status{تم,اتلغى,جارى, ينفذ,لارد};
	
	@Column(columnDefinition="enum('تم','اتلغى','جارى', 'ينفذ','لا_رد')")
	 private String status;
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;
	
	
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
	public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Driver getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(Driver driver_id) {
		this.driver_id = driver_id;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

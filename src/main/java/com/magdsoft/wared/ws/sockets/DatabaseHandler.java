package com.magdsoft.wared.ws.sockets;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magdsoft.wared.ws.model.Driver;
import com.magdsoft.wared.ws.model.Profit;
import com.magdsoft.wared.ws.model.User;
import com.magdsoft.wared.ws.sockets.GenerateMessagesCommon.TripStatus;
import com.magdsoft.wared.ws.model.TheOrder;

@Service
public class DatabaseHandler {
	@Autowired
	public EntityManager entityManager;

	@Transactional
	public Driver getDriverData(int driverId) {
		return entityManager.find(Driver.class, driverId);
	}

	@Transactional
	public Driver getDriverForOrder(int orderId) {
		TheOrder trip = entityManager.find(TheOrder.class, orderId);
		return trip.getDriver_id();
	}
	
	@Transactional
	public User getUserForOrder(int orderId) {
		TheOrder trip = entityManager.find(TheOrder.class, orderId);
		return trip.getUser_id();
	}

	@Transactional
	public TheOrder getOrderById(int orderId) {
		return entityManager.find(TheOrder.class, orderId);
	}

	@Async
	@Transactional
	public void stopTheTrip(int orderId, TripStatus status) {
		TheOrder trip = entityManager.find(TheOrder.class, orderId);
//		trip.setIsActive(true);
//		trip.setStatus(true);
		trip.setStatus(TripStatus.REACHED.equals(status) ? "تم" : "لاغى");
		Profit pr = null;
		if (TripStatus.REACHED.equals(status)) {
			pr = new Profit();
			pr.setDriver_id(trip.getDriver_id());
			pr.setMoney(trip.getCost());
			pr.setTheOrder_id(trip);
		}
		entityManager.persist(trip);
		if (pr != null) entityManager.persist(pr);
	}

	@Async
	@Transactional
	public void assignOrderToDriver(int orderId, int driverId) {
		TheOrder trip = entityManager.find(TheOrder.class, orderId);
		Driver driver = entityManager.find(Driver.class, driverId);
		trip.setDriver_id(driver);
//		trip.setIsActive(true);
//		trip.setStatus(false);
		trip.setStatus("جارى");
		entityManager.persist(trip);
	}

	@Transactional
	public TheOrder getOrderForUser(int userId) {
		Query q = entityManager.createQuery("select ord from TheOrder ord where ord.user_id.id = :id", TheOrder.class);
		q.setParameter("id", userId);
		TheOrder trip = (TheOrder) q.getSingleResult();
		return trip;
	}

	@Transactional
	public TheOrder getOrderForDriver(int driverId) {
		Query q = entityManager.createQuery("select ord from TheOrder ord where ord.driver_id.id = :id"
				+ " order by ord.createdAt desc", TheOrder.class);
		q.setParameter("id", driverId);
		q.setMaxResults(1);
		TheOrder trip = (TheOrder) q.getSingleResult();
		return trip;
	}

//	@Transactional
//	@Async
//	public void assignTripToUser(int carId, int tripId) {
//		UserTrip trip = entityManager.find(UserTrip.class, tripId);
//		trip.setCar_id(entityManager.find(Car.class, carId));
//		entityManager.persist(trip);
//	}
//	
//	@Transactional
//	@Async
//	public void setTripStartingTime(int tripId) {
//		UserTrip trip = entityManager.find(UserTrip.class, tripId);
//		trip.setStartAt(Date.from(Instant.now()));
//		entityManager.persist(trip);
//	}
//	
//	@Transactional
//	@Async
//	public void setTripEndTime(int tripId) {
//		UserTrip trip = entityManager.find(UserTrip.class, tripId);
//		trip.setEndAt(Date.from(Instant.now()));
//		entityManager.persist(trip);
//	}

	@Transactional
	@Async
	public void updateCarLocation(Integer driverId, Integer bearing, Double latitude, Double longitude) {
		Driver car = entityManager.find(Driver.class, driverId);
		car.setLatitude(latitude);
		car.setLongitude(longitude);
		entityManager.persist(car);
	}

}

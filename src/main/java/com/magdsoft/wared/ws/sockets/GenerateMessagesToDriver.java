package com.magdsoft.wared.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magdsoft.wared.ws.model.TheOrder;
import com.magdsoft.wared.ws.model.User;

@Service
public class GenerateMessagesToDriver {
	@Autowired
	public EntityManager entityManager;
	
	public Map<String, Object> generateRequestMessage(Map<String, Object> userData) {
		entityManager.getTransaction().begin();
		User user = entityManager.find(User.class, userData.get("userId"));

		Map<String, Object> request = new HashMap<>();
		request.put("name", user.getName());
		request.put("startLatitude", userData.get("startLatitude"));
		request.put("startLongitude", userData.get("startLongitude"));

		// TODO: Correct the web service to include the end coordinates in
		// the data sent to the socket
	
//		request.put("endLatitude", userData.get("endLatitude"));
//		request.put("endLongitude", userData.get("endLongitude"));

		request.put("from", userData.get("startAtAddress"));
		request.put("to", userData.get("endAtAddress"));
		return request;
	}		

	public Map<String, Object> generateRequestMessage(User user, TheOrder order) {
		Map<String, Object> dataToSendWhenRequested = new HashMap<>();
		dataToSendWhenRequested.put("cost", order.getCost());
		dataToSendWhenRequested.put("tripId", order.getId());
		dataToSendWhenRequested.put("latitude", order.getLatitude());
		dataToSendWhenRequested.put("longitude", order.getLongitude());
		dataToSendWhenRequested.put("startAtAddress", order.getAddress());
		dataToSendWhenRequested.put("userPhone", user.getPhone());
		dataToSendWhenRequested.put("userImage", user.getUserImage());
		dataToSendWhenRequested.put("userName", user.getName());
		return dataToSendWhenRequested;
	}

}

package com.magdsoft.wared.ws.sockets;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.magdsoft.wared.ws.LocationAndSessionAware;

/**
 * DAO data structure (Add, Update, Delete) for the in-memory location aware objects database
 * Indexes the added (or updated) data by id and by location
 */
public class LocationAndSessionAwareObjectDAO<T extends LocationAndSessionAware> extends LocationAwareObjectDAO<T> {

	private final Map<String, T> objectsBySessionId = new HashMap<>();

	@Override
	public synchronized void addObject(T object) {
		super.addObject(object);
		objectsBySessionId.put(object.getSession().getId(), object);
	}

	@Override
	public synchronized void deleteObject(int id) {
		objectsBySessionId.remove(this.getById(id).getSession().getId());
		super.deleteObject(id);
	}

	public synchronized T getBySession(WebSocketSession session) {
		return objectsBySessionId.get(session.getId());
	}

	public void setSessionForObject(T object, WebSocketSession session) {
		WebSocketSession oldSession = (object.getSession());
		T obj = objectsBySessionId.get(oldSession.getId());

		synchronized(this) {
			objectsBySessionId.remove(oldSession.getId());
			object.setSession(session);
			objectsBySessionId.put(session.getId(), obj);
			if (null != object.getLocation()) {
				this.updateObjectLocation(object.getId(), object.getLocation().getLatitude(), object.getLocation().getLongitude(),
						object.getLocation().getBearing());
			}
		}
	}
}

package com.magdsoft.wared.ws;

import org.springframework.web.socket.WebSocketSession;

public interface LocationAndSessionAware extends LocationAware {
	public WebSocketSession getSession();
	public void setSession(WebSocketSession session);
}

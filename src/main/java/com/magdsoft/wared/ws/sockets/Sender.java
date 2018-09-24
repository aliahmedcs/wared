package com.magdsoft.wared.ws.sockets;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Sender {

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Sends a message to a web socket session
	 * Automatically handle if the socket is null or closed
	 *
	 * @param webSocketSession Socket to send a message to
	 * @param message          The message body, converts automatically to Json using ObjectMapper
	 */
	public void sendJsonToWebSocketSession(WebSocketSession webSocketSession, Object message) {
		if (null == webSocketSession) return;
		if (!webSocketSession.isOpen()) return;
		try {
			String messageAsString = objectMapper.writeValueAsString(message);
			synchronized(webSocketSession) {
				webSocketSession.sendMessage(new TextMessage(messageAsString));
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * The Async counter-part to Sender#sendJsonToWebSocketSession
	 * Just delegate to it asynchronously
	 *
	 * @param webSocketSession Socket to send a message to
	 * @param message          The message body, converts automatically to Json using ObjectMapper
	 */
	@Async
	public void sendJsonToWebSocketSessionAsync(WebSocketSession webSocketSession, Object message) {
		sendJsonToWebSocketSession(webSocketSession, message);
	}
}

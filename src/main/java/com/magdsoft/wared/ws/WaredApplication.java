package com.magdsoft.wared.ws;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import com.magdsoft.wared.ws.sockets.WebSocketController;
import com.magdsoft.wared.ws.sockets.WebSocketUserController;

@Configuration
@EnableAsync
@EnableWebSocket
class WaredWebSocketConfigurer implements WebSocketConfigurer {

	@Bean
	public WebSocketController webSocketController() {
		return new WebSocketController();
	}

	@Bean
	public WebSocketUserController webSocketUserController() {
		return new WebSocketUserController();
	}

	@Override
	public void registerWebSocketHandlers(org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketController(), "/driverSocket");
		registry.addHandler(webSocketUserController(), "/userSocket");
	}
}

@SpringBootApplication
public class WaredApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaredApplication.class, args);
	}
}

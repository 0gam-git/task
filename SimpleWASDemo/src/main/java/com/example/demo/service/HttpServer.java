package com.example.demo.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.demo.httpserver.HttpRequest;

@Service
public class HttpServer {

	private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());

	private static final int parallelism = 20;

	private int port;

	public void start() {
		ExecutorService executorService = Executors.newWorkStealingPool(parallelism);

		try (ServerSocket server = new ServerSocket(getPort())) {
			logger.info("Accepting connections on port " + server.getLocalPort());

			while (true) {
				try {
					Socket connection = server.accept();
					HttpRequest request = new HttpRequest(connection);
					executorService.execute(request);

				} catch (Exception ex) {
					logger.log(Level.WARNING, "Error accepting connection", ex);
				}
			}
		} catch (IOException ex) {
			logger.log(Level.WARNING, "Error accepting connection", ex);
		}
	}

	// ---------------------------------------------------

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}

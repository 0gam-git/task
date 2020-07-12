package com.example.demo.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.demo.httpserver.HttpRequest;

@Service
public class HttpServer {

	private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());

	private int port;

	public void start() {
		try (ServerSocket server = new ServerSocket(getPort())) {
			logger.info("Accepting connections on port " + server.getLocalPort());

			while (true) {
				try {
					Socket connection = server.accept();
					logger.info("Accepting connections on ip : " + connection.getInetAddress());
					HttpRequest request = new HttpRequest(connection);

					Thread thread = new Thread(request);
					thread.start();

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

package com.example.demo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.service.HttpServer;

@SpringBootApplication
public class SimpleWasDemoApplication implements CommandLineRunner, ExitCodeGenerator {

	private static final Logger logger = Logger.getLogger(SimpleWasDemoApplication.class.getCanonicalName());

	@Value("${demo.port}")
	private int defaultPort;

	private final HttpServer httpServer;

	private final ConfigurableApplicationContext context;

	public SimpleWasDemoApplication(HttpServer httpServer, ConfigurableApplicationContext context) {
		this.httpServer = httpServer;
		this.context = context;
	}

	@Override
	public void run(String... args) throws Exception {
		int port = 0;

		if (0 < args.length) {
			String arg = args[0];

			if (arg != null && !"".equals(arg)) {
				try {
					port = Integer.parseInt(arg);
				} catch (NumberFormatException ex) {
					logger.log(Level.WARNING, "not supported argument.", ex);
					usage();
					System.exit(SpringApplication.exit(context));
				}
			}
		}

		if (port <= 0 || port > 65535) {
			port = defaultPort;
		}

		httpServer.setPort(port);
		httpServer.start();
	}

	@Override
	public int getExitCode() {
		return 42;
	}

	public static void main(String[] args) {
		SpringApplication.run(SimpleWasDemoApplication.class, args);
	}

	// -----------------------------------------

	private void usage() {
		System.out.println("args - [<port>]");
	}

}

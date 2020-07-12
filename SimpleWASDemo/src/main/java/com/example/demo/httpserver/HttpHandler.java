package com.example.demo.httpserver;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.demo.httpserver.type.HttpMethodType;

public abstract class HttpHandler {
	public static final List<String> DEFAULT_PATH = Arrays.asList("*");

	private final HashMap<String, ArrayList<Route>> routes = new HashMap<>();
	private final HashMap<String, Route> defaultRoutes = new HashMap<>();

	private Socket socket;
	private DataOutputStream writer;

	public HttpHandler() {
	}

	public void handle(HttpRequest request, HttpResponse response) {
		
		String httpRequestType = request.getRequestType().toUpperCase();
		if (!routes.containsKey(httpRequestType)) {
			response.message(501, "No " + httpRequestType + " routes exist.");
			return;
		}

		Route route = defaultRoutes.get(httpRequestType);
		int bestFit = 0;
		for (Route testRoute : routes.get(httpRequestType)) {
			if (testRoute.matchesPerfectly(request.getSplitPath())) {
				route = testRoute;
				break;
			}

			int testScore = testRoute.howCorrect(request.getSplitPath());
			if (testScore > bestFit) {
				route = testRoute;
				bestFit = testScore;
			}
		}

		if (route == null) {
			response.message(501, "NOT_A_METHOD_ERROR");
			return;
		}

		route.invoke(request, response);
	}

	public void get(Route route) {
		addRoute(HttpMethodType.GET.name(), route);
	}

	public void post(Route route) {
		addRoute(HttpMethodType.GET.name(), route);
	}

	public void delete(Route route) {
		addRoute(HttpMethodType.DELETE.name(), route);
	}

	public void addRoute(String httpMethod, Route route) {
		httpMethod = httpMethod.toUpperCase();

		if (!routes.containsKey(httpMethod)) {
			routes.put(httpMethod, new ArrayList<Route>());
		}

		routes.get(httpMethod).add(route);

		if (route.matchesPerfectly(DEFAULT_PATH)) {
			defaultRoutes.put(httpMethod, route);
		}
	}

	// --------------------------------------------------------

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setWriter(DataOutputStream writer) {
		this.writer = writer;
	}

	public DataOutputStream getWriter() {
		return writer;
	}
}
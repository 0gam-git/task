package com.example.demo.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.httpserver.type.HttpMethodType;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616.html
 */
public class HttpRequest implements Runnable {

	private HttpRouter router;

	private Socket connection;

	private HttpHandler handler;

	private String httpRequest;

	private String requestLine;

	private String requestType;

	private String requestProtocol;

	private Map<String, String> headers = new HashMap<>();

	private List<String> splitPath = new ArrayList<>();

	private String path;

	private String fullPath;

	// the POST data
	private Map<String, String> params = new HashMap<>();

	private List<String> varargs = new ArrayList<>();

	private String requestBody;

	public HttpRequest(Socket connection) throws IOException, SocketException, HttpException {
		connection.setKeepAlive(true);
		setConnection(connection);
	}

	@Override
	public void run() {
		if (getConnection().isClosed()) {
			System.out.println("Socket is closed...");
		}

		try {
			createResponse().respond();
		} catch (IOException | HttpException e) {
			e.printStackTrace();
		}
	}

	public HttpResponse createResponse() throws IOException, HttpException {
		parseRequest();
		HttpResponse response = new HttpResponse(this);
		determineHandler().handle(this, response);

		return response;
	}

	/**
	 * https://github.com/dkuntz2/java-httpserver/issues/12 RFC 2616#4.2:
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
	 * 
	 */
	public void parseRequest() throws IOException, SocketException, HttpException {
		BufferedReader input = new BufferedReader(new InputStreamReader(getConnection().getInputStream()));
		StringBuilder requestBuilder = new StringBuilder();

		String firstLine = input.readLine();
		if (firstLine == null) {
			throw new HttpException("Input is returning nulls...");
		}

		while (firstLine.isEmpty()) {
			firstLine = input.readLine();
		}

		setRequestLine(firstLine);
		requestBuilder.append(getRequestLine());
		requestBuilder.append("\n");

		for (String line = input.readLine(); line != null && !line.isEmpty(); line = input.readLine()) {
			requestBuilder.append(line);
			requestBuilder.append("\n");

			String[] items = line.split(": ");

			if (items.length == 1) {
				throw new HttpException("No key value pair in \n\t" + line);
			}

			String value = items[1];
			for (int i = 2; i < items.length; i++) {
				value += ": " + items[i];
			}

			getHeaders().put(items[0], value);
		}

		if (getRequestType().equals(HttpMethodType.GET.name())) {

		} else {

		}

		if ((getRequestType().equals(HttpMethodType.POST.name())
				|| getRequestType().equals(HttpMethodType.DELETE.name())
				|| getRequestType().equals(HttpMethodType.PUT.name())) && getHeaders().containsKey("Content-Length")) {
			int contentLength = Integer.parseInt(getHeaders().get("Content-Length"));
			StringBuilder b = new StringBuilder();

			for (int i = 0; i < contentLength; i++) {
				b.append((char) input.read());
			}

			requestBuilder.append(b.toString());

			requestBody = b.toString();

			String[] data = requestBody.split("&");
			getParams().putAll(parseInputData(data));
		}

		setHttpRequest(requestBuilder.toString());
	}

	private Map<String, String> parseInputData(String[] data) {
		Map<String, String> out = new HashMap<String, String>();
		for (String item : data) {
			if (item.indexOf("=") == -1) {
				out.put(item, null);
				continue;
			}

			String value = item.substring(item.indexOf('=') + 1);
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}

			out.put(item.substring(0, item.indexOf('=')), value);
		}

		return out;
	}

	public HttpHandler determineHandler() {
		if (router == null) {
			return new HttpHandler() {
			};
		}

		String path = getSplitPath().isEmpty() ? "" : getSplitPath().get(0);
		return router.route(path, this);
	}

	public boolean isType(String requestTypeCheck) {
		return getRequestType().equalsIgnoreCase(requestTypeCheck);
	}

	public void setRequestLine(String line) throws HttpException {
		this.requestLine = line;

		String[] splitty = requestLine.trim().split(" ");
		if (splitty.length != 3) {
			throw new HttpException("Request line has a number of spaces other than 3.");
		}

		setRequestType(splitty[0].toUpperCase());

		setFullPath(splitty[1]);

		setRequestProtocol(splitty[2]);
	}

	public String getRequestLine() {
		return requestLine;
	}

	public void setFullPath(String inPath) {
		this.fullPath = inPath;
		setPath(inPath);
		setSplitPath(inPath);
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setSplitPath(String fullPath) {
		for (String segment : fullPath.substring(1).split("/")) {
			if (segment.isEmpty()) {
				continue;
			}

			getSplitPath().add(segment);
		}

		if (getSplitPath().isEmpty()) {
			return;
		}

		if (getSplitPath().get(getSplitPath().size() - 1).indexOf('?') != -1) {
			String lastItem = getSplitPath().get(getSplitPath().size() - 1);
			getSplitPath().set(getSplitPath().size() - 1, lastItem.substring(0, lastItem.indexOf('?')));

			String[] data = lastItem.substring(lastItem.indexOf('?') + 1).split("&");

			getParams().putAll(parseInputData(data));
		}
	}

	public void setSplitPath(List<String> path) {
		this.splitPath = path;
	}

	public List<String> getSplitPath() {
		return splitPath;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setParams(Map<String, String> data) {
		this.params = data;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void mergeParams(Map<String, String> data) {
		this.params.putAll(data);
	}

	public String getParam(String key) {
		return this.params.get(key);
	}

	public void mergeVarargs(List<String> data) {
		this.varargs.addAll(data);
	}

	public List<String> getVarargs() {
		return this.varargs;
	}

	public void setHttpRequest(String httpRequest) {
		this.httpRequest = httpRequest;
	}

	public String getHttpRequest() {
		return httpRequest;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestProtocol(String requestProtocol) {
		this.requestProtocol = requestProtocol;
	}

	public String getRequestProtocol() {
		return requestProtocol;
	}

	public void setHandler(HttpHandler handler) {
		this.handler = handler;
	}

	public HttpHandler getHandler() {
		return handler;
	}

	public void setRouter(HttpRouter router) {
		this.router = router;
	}

	public HttpRouter getRouter() {
		return router;
	}

	public String getRequestBody() {
		return requestBody;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HttpRequest from ");
		builder.append(getConnection().getLocalAddress().getHostAddress());
		builder.append("\n\t");
		builder.append("Request Line: ");
		builder.append(getRequestLine());
		builder.append("\n\t\t");
		builder.append("Request Type ");
		builder.append(getRequestType());
		builder.append("\n\t\t");
		builder.append("Request Path ");
		builder.append(getFullPath());

		return builder.toString();
	}
}

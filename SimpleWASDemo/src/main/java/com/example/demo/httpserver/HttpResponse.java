package com.example.demo.httpserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

	private static String serverInfo;
	private static Map<Integer, String> responses;
	private int code = 200;
	private byte[] body;
	private String mimeType = "text/plain";
	private long size = -1;

	private Map<String, String> headers = new HashMap<>();

	private Socket socket;
	private DataOutputStream writer;

	public HttpResponse(HttpRequest req) throws IOException {
		socket = req.getConnection();
		writer = new DataOutputStream(socket.getOutputStream());
	}

	public void message(int code, String message) {
		setCode(code);
		setBody(message);
		setMimeType("text/plain");
	}

	public void error(int code, String message, Throwable t) {
		t.printStackTrace();
		message(code, message);
	}

	protected void writeLine(String line) throws IOException {
		getWriter().writeBytes(line + "\n");
	}

	// ----------------------------------------------------------

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body.getBytes();
	}

	public void setBody(byte[] bytes) {
		body = bytes;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		if (size < 0) {
			throw new RuntimeException("Response Content-Length must be non-negative.");
		}

		this.size = size;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public DataOutputStream getWriter() {
		return writer;
	}

	public static String getResponseCodeMessage(int code) {
		if (responses == null || responses.isEmpty()) {
			setupResponses();
		}

		if (responses.containsKey(code)) {
			return code + " " + responses.get(code);
		}

		return Integer.toString(code);
	}

	// --------------------------------------------

	private static void setupResponses() {
		responses = new HashMap<Integer, String>();

		responses.put(100, "Continue");
		responses.put(101, "Switching Protocols");

		responses.put(200, "OK");
		responses.put(201, "Created");
		responses.put(202, "Accepted");
		responses.put(203, "Non-Authoritative Information");
		responses.put(204, "No Content");
		responses.put(205, "Reset Content");
		responses.put(206, "Partial Content");

		responses.put(300, "Multiple Choices");
		responses.put(301, "Moved Permanently");
		responses.put(302, "Found");
		responses.put(303, "See Other");
		responses.put(304, "Not Modified");
		responses.put(305, "Use Proxy");
		responses.put(307, "Temporary Redirect");

		responses.put(400, "Bad Request");
		responses.put(401, "Unauthorized");
		responses.put(402, "Payment Required");
		responses.put(403, "Forbidden");
		responses.put(404, "Not Found");
		responses.put(405, "Method Not Allowed");
		responses.put(406, "Not Acceptable");
		responses.put(407, "Proxy Authentication Required");
		responses.put(408, "Request Timeout");
		responses.put(409, "Conflict");
		responses.put(410, "Gone");
		responses.put(411, "Length Required");
		responses.put(412, "Precondition Failed");
		responses.put(413, "Request Entity Too Large");
		responses.put(414, "Request-URI Too Long");
		responses.put(415, "Unsupported Media Type");
		responses.put(416, "Request Range Not Satisfiable");
		responses.put(417, "Expectation Failed");
		responses.put(418, "I'm a teapot");
		responses.put(420, "Enhance Your Calm");

		responses.put(500, "Internal Server Error");
		responses.put(501, "Not implemented");
		responses.put(502, "Bad Gateway");
		responses.put(503, "Service Unavaliable");
		responses.put(504, "Gateway Timeout");
		responses.put(505, "HTTP Version Not Supported");
	}

	public static void setServerInfo(String serverInfo) {
		HttpResponse.serverInfo = serverInfo;
	}

	public static String getServerInfo() {
		return serverInfo;
	}
}

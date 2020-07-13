package com.example.demo.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import com.example.demo.httpserver.HttpRequest;
import com.example.demo.httpserver.HttpResponse;
import com.example.demo.httpserver.type.HttpMethodType;

public class ServletImpl implements SimpleServlet {

	@Override
	public void service(HttpRequest req, HttpResponse res) throws IOException {

		if (req.getRequestType().equals(HttpMethodType.GET.name())) {
			// file read?
			// url mapping?

		} else {
			String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n")
					.append("</HEAD>\r\n").append("<BODY>").append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
					.append("</BODY></HTML>\r\n").toString();
			if (req.getRequestProtocol().startsWith("HTTP/")) { // send a MIME header
				sendHeader(res.getWriter(), "HTTP/1.0 501 Not Implemented", "text/html; charset=utf-8", body.length());
			}
			res.getWriter().writeBytes(body);
			res.getWriter().flush();
		}
	}

	private void sendHeader(DataOutputStream out, String responseCode, String contentType, int length)
			throws IOException {
		out.writeBytes(responseCode + "\r\n");
		Date now = new Date();
		out.writeBytes("Date: " + now + "\r\n");
		out.writeBytes("Server: JHTTP 2.0\r\n");
		out.writeBytes("Content-length: " + length + "\r\n");
		out.writeBytes("Content-type: " + contentType + "\r\n\r\n");
		out.flush();
	}
}

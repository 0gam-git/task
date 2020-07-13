package com.example.demo.service;

import java.io.IOException;

import com.example.demo.httpserver.HttpRequest;
import com.example.demo.httpserver.HttpResponse;

public interface SimpleServlet {

	public void service(HttpRequest req, HttpResponse res) throws IOException;

}

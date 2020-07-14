package com.example.demo.httpserver;

public class ServletMapping {

	private String servletName;
	private String urlPattern;
	private String servletClass;

	public ServletMapping(String servletName, String urlPattern, String servletClass) {
		this.servletName = servletName;
		this.urlPattern = urlPattern;
		this.servletClass = servletClass;
	}

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

}

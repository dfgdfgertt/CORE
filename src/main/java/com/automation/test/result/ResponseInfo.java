package com.automation.test.result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseInfo {
	private int code;
	private Map<String, List<String>> headers;
	private String body;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new HashMap<String, List<String>>();
		}
		
		if (headers.containsKey(key)) {
			headers.get(key).add(value);
		}
		else {
			headers.put(key, Arrays.asList(value));
		}
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (code > 0) {
			sb.append("Response code: ").append(code).append("\n");
		}
		
		if (headers != null) {
			sb.append("Header:").append("\n");
			for (Map.Entry<String, List<String>> ent : headers.entrySet()) {
				sb.append(" ").append(ent.getKey()).append(": ").append(String.join("; ", ent.getValue())).append("\n");
			}
		}
		
		if (body != null) {
			sb.append("Body: ").append(body);
		}
		
		return sb.toString();
	}
}

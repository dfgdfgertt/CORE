package com.automation.test.helper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkHelper {
	public static final String CHECK_IP_URL = "http://checkip.amazonaws.com/";
	
	public static String getPublicIP() throws IOException {
		URL url = new URL(CHECK_IP_URL);
		try (InputStream stream = url.openStream()) {
			return IOUtils.toString(stream, StandardCharsets.US_ASCII).replaceAll("\\r|\\n", "");
		}
	}
}

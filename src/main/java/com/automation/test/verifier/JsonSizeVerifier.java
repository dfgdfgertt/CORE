package com.automation.test.verifier;

import com.google.gson.JsonArray;

public class JsonSizeVerifier extends AbstractVerifier<JsonArray, Integer> {

	@Override
	public boolean isOk(JsonArray actual) {
		JsonArray json = actual;
		int expectedlSize = getExpected();
		msg = String.format("Check json size. Expected: %d, actual: %d", expectedlSize, json.size());
		return json.size() == expectedlSize;
	}

}

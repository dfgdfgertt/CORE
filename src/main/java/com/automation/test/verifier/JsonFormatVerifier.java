package com.automation.test.verifier;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonFormatVerifier extends AbstractDataVerifier<String> {

	@Override
	public boolean isOk(String actual) {
		try {
			new Gson().fromJson(actual, Object.class);
			return true;
		} catch (JsonSyntaxException e) {
			msg = "The actual value does not follow JSON format. Inner exception: " + e.getMessage();
			return false;
		}
	}

}

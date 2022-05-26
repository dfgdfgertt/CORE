package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonVerifier extends AbstractDataVerifier<String> {

	private List<String> ignoredKeys;
	
	public JsonVerifier() {
		ignoredKeys = new ArrayList<String>();
	}
	
	public JsonVerifier(List<String> ignoredKeys) {
		this.ignoredKeys = ignoredKeys;
		if (this.ignoredKeys == null) {
			this.ignoredKeys = new ArrayList<>();
		}
	}
	
	private void removeKeys(JsonObject json, List<String> keys) {
		for (String key : keys) {
			json.remove(key);
		}
		
		for (Map.Entry<String, JsonElement> pair : json.entrySet()) {
			if (pair.getValue() instanceof JsonObject) {
				removeKeys((JsonObject)pair.getValue(), keys);
			}
		}
	}
	
	@Override
	public boolean isOk(String actual) throws TestIOException {
		this.explanation = "See detail in comment";
		JsonParser parser = new JsonParser();
		
		JsonElement expectedElem = null, actualElem = null;
		
		try {
			expectedElem = parser.parse(getExpected());
			actualElem = parser.parse(actual);
		} catch (JsonParseException ex) {
			throw new TestIOException("Json string is not valid: " + ex.getMessage(), ex);
		}
		
		if (ignoredKeys.size() > 0 && expectedElem instanceof JsonObject && actualElem instanceof JsonObject) {
			removeKeys((JsonObject)expectedElem, ignoredKeys);
			removeKeys((JsonObject)actualElem, ignoredKeys);
		}
		
		msg = String.format("Verify json, expected: %s, actual: %s", expectedElem, actualElem);
		
		return expectedElem.equals(actualElem);
	}

}

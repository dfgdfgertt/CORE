package com.automation.test.verifier;

import java.util.Map;
import java.util.Map.Entry;

public class MapPatternVerifier extends AbstractDataVerifier<Map<String, String>> {

	@Override
	public boolean isOk(Map<String, String> actual) {
		Map<String, String> expected = getExpected();
		for (Entry<String, String> ent : expected.entrySet()) {
			String key = ent.getKey();
			if (actual.containsKey(key)) {
				if (!actual.get(key).matches(ent.getValue())) {
					msg = String.format("MapPatternVerifier: Verify string pattern failed. Expected: %s, actual: %s", ent.getValue(), actual.get(key));
					return false;
				}
			}
			else {
				msg = String.format("MapPatternVerifier: Expected key %s does not exist", key);
				return false;
			}
		}
		
		return true;
	}

}

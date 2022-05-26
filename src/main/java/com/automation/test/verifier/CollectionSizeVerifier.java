package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;

import java.util.List;

public class CollectionSizeVerifier<T> extends AbstractVerifier<List<T>, Long> {

	@Override
	public boolean isOk(List<T> actual) throws TestIOException {
		Long expected = getExpected();
		
		if (actual == null) {
			if (expected == null) {
				msg = "Expected and actual value is null";
				return true;
			}
			else {
				msg = String.format("Expected size is %d but actual list is null", expected);
				return false;
			}
		}
		else {
			explanation = String.valueOf(actual.size());
			msg = String.format("Compare list size. Expected: %d, actual: %d", expected, actual.size());
			if (expected == actual.size()) {
				return true;
			}
			else {
				return false;
			}
		}
	}

}

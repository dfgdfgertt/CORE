package com.automation.test.verifier;

public class SimpleVerifier<T> extends AbstractDataVerifier<T> {

	@Override
	public boolean isOk(T actual) {

		T expected = getExpected();
		msg = String.format("SimpleVerifier: Check objects equal. Expected: %s, actual: %s", expected, actual);
		if (actual != null) {
			return actual.equals(expected);
		} else {		
			return actual == expected;
		}

	}

	@Override
	public String toString() {
		return msg;
	}
}

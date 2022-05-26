package com.automation.test.verifier;

/**
 * Verify 2 text data are same
 * @author peter.tu.tran
 *
 */
public class PlainTextVerifier extends AbstractDataVerifier<String> {



	public boolean isMatched(String expected, String actual) {
		if (expected == null) {
			msg = "PlainTextVerifier: Expected value was not set";
			return false;
		} else if (actual == null) {
			msg = "PlainTextVerifier: Actual value is null";
			return false;
		} else {
			if(enableVerifyContains && actual.contains(expected)){
				msg = String.format("Actual result '%s' contains expected text '%s'", actual, expected);
				return true;
			}
			msg = String.format("PlainTextVerifier: Verify strings equal. Expected: %s, actual: %s", expected, actual);
			return expected.equals(actual);
		}
	}

	@Override
	public boolean isOk(String actual) {
		this.explanation = "See detail of actual in comment";
		return isMatched(getExpected(), actual);
	}

	private boolean enableVerifyContains;
	public boolean enableVerifyContains(boolean enableVerifyContains){
		return this.enableVerifyContains=enableVerifyContains;
	}
}

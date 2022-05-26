package com.automation.test.verifier;

import java.util.Arrays;

public class BinaryVerifier extends AbstractDataVerifier<byte[]> {

	@Override
	public boolean isOk(byte[] actual) {
		boolean equal = Arrays.equals(actual, getExpected());
		msg = String.format("BinaryVerifier: Compare 2 byte arrays. Expected: %s, actual: %s", true, equal);
		return equal;
	}

}

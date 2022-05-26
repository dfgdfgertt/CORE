package com.automation.test.verifier;

public interface RuntimeExpectable<T> {
	T getExpected() throws Exception;
}

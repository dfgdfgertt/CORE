package com.automation.test.publisher;

import com.automation.test.exception.TestIOException;

import java.io.IOException;

/**
 * A way to send out data or perform given action
 * @author peter.tu.tran
 *
 */
public interface Publisher<T> {
    /**
     * Get data preparing for publishing
     * @return Input data
     */
	T getInput();
	
	/**
	 * Send out data or perform given action
	 * @throws TestIOException
	 */
	void publish() throws IOException;
	
	/**
	 * Describe what the publisher does
	 * @return What the publisher does
	 */
	String getAction();
}

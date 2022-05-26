package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
/**
 * The way how to get data
 * @author peter.tu.tran
 * @param <T> Return data type
 */
public interface Reader<T> {
    /**
     * Read data once
     * @return
     * @throws TestIOException
     */
	T getData() throws Exception;
	
	/**
	 * Read data upon call
	 * @return
	 * @throws TestIOException
	 */
	T read() throws Exception;
	
	/**
	 * Clean cache so the next time getData will read data again
	 */
	void resetCache();
}

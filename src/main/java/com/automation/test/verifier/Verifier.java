package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;

/**
 * A way to verify data or behavior
 * @author peter.tu.tran
 *
 * @param <ACT> Actual data type
 * @param <EXP> Expected data type
 */
public interface Verifier<ACT, EXP> {
    /**
     * Get expected data
     * @return Expected data
     */
    EXP getExpected();
	/**
	 * Check if actual data matches a given condition against expected data
	 * @param actual
	 * @return true if ok
	 * @throws TestIOException 
	 */
	boolean isOk(ACT actual) throws Exception;
	/**
     * Get info or error message during verifying
     * @return
     */
	String getMessage();
	/**
     * Get info how it passed or failed to verify
     * @return
     */
	String getExplanation();
	
	void resetCache();
}

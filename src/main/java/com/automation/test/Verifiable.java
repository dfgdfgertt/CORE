package com.automation.test;

/**
 * Used to verify action
 * @author peter.tu.tran
 *
 */
public interface Verifiable {
    /**
     * Check if a given condition is passed
     * @return true if ok
     */
	boolean isOk() throws Exception;
}

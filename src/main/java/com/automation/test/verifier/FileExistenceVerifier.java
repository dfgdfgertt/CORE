package com.automation.test.verifier;

import java.io.File;

public class FileExistenceVerifier extends AbstractFileVerifier<Object, Object> {

	protected String filepath;
	private boolean expectedExistence;
	
	public FileExistenceVerifier(String filepath, boolean exists, int timeout) {
		super(timeout);
		this.filepath = filepath;
		this.expectedExistence = exists;
	}
	
	protected boolean isExisted() throws Exception {
	    return new File(filepath).exists();
	}

	@Override
	public boolean isOk(Object actual) {
		int nSleep = getNSleep();
		boolean actualExistence;
		
		try {
    		while ((actualExistence = isExisted()) != expectedExistence) {
    			if (nSleep-- > 0) {
    				try {
    					Thread.sleep(SLEEP_PERIOD);
    				} catch (InterruptedException e) {
    					msg = String.format("Checking file '%s' existence threw exception: %s", filepath, e.getMessage());
    					return false;
    				}
    			} else {
    				msg = String.format("Checking file '%s' existence timed out (exceed %d ms). Expected: %s, actual: %s", filepath, timeout, expectedExistence, actualExistence);
    			    return false;
    			}
    		}
    		
    		msg = String.format("Checking file '%s' existence. Expected: %s, actual: %s", filepath, expectedExistence, actualExistence);
		} catch (Exception ex) {
		    explanation = ex.getMessage();
		    msg = String.format("Checking file '%s' existence threw exception: %s", filepath, ex.getMessage());
		    return false;
		}
		
		return expectedExistence == actualExistence;
	}
	
}

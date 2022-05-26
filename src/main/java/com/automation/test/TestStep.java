package com.automation.test;

import com.automation.test.result.TestStepResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Single test step
 * @author peter.tu.tran
 *
 */
public abstract class TestStep implements Testable {
	protected List<TestStepResult> results;

	public TestStep() {
		results = new ArrayList<>();
	}
	
	/**
	 * Get desc, input, expect, acutal, and result of each step
	 * @return Result of test step
	 */
	public List<TestStepResult> getResults() {
		return results;
	}

}

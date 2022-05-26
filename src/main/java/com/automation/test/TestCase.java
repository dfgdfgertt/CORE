package com.automation.test;

import com.automation.test.result.TestCaseResult;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class to run a single test case
 * @author peter.tu.tran
 *
 */
public final class TestCase implements Testable {
	private String name;
	private String description;
	private List<TestStep> steps;
	
	public TestCase(String name, String description) {
		this.name = name;
		this.description = description;
		steps = new ArrayList<>();
	}
	
	/**
	 * Add a new step
	 * @param step Single test step
	 */
	public void addStep(TestStep step) {
		steps.add(step);
	}
	
	/**
	 * Run all defined test steps and create test result
	 */
	@Override
	public void run() {
		TestCaseResult tcResult = new TestCaseResult(name, description);
		Reporter.getCurrentTestResult().setAttribute("result", tcResult);
		
		for (TestStep step : steps) {
			try {
				step.run();
			} finally {
				tcResult.addSteps(step.getResults());
			}
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}

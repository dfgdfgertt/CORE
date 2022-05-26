package com.automation.test.report;

import com.automation.test.result.TestStepResult;

import java.io.IOException;
import java.util.List;

public interface ITestExporter {
	public String createTestResult(String testSuite, List<TestStepResult> results, String resultPath) throws IOException, IllegalArgumentException, IllegalAccessException;
}

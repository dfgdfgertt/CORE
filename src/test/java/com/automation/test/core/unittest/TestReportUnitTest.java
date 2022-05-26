package com.automation.test.core.unittest;

import com.automation.test.result.TestCaseResult;
import com.automation.test.result.TestStepResult;
import com.automation.test.result.TestSuiteResult;
import com.automation.test.report.ExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.MatcherAssert;

import java.time.LocalDateTime;


public class TestReportUnitTest {
//	@Test
	public void testExcelReporter() throws Exception {
		TestSuiteResult rsSuite = new TestSuiteResult();
		rsSuite.setSuiteName("SFDC");
		rsSuite.setReportTime(LocalDateTime.of(2011, 1, 1, 1, 1, 1));
		rsSuite.setSapInfo("SAPW75");
		rsSuite.setBuildNum("123");
		rsSuite.setPassNo(1);
		rsSuite.setFailNo(1);
		rsSuite.setSkipNo(0);
		rsSuite.setTotal(2);

		TestCaseResult tcResult = new TestCaseResult("TC1", "TC1 description");
		tcResult.setStatus("PASSED");
		tcResult.setTime((long) 10);
		TestStepResult step = new TestStepResult();
		step.setActual("Actual 1");
		step.setActualComment("Actual comment 1");
		step.setDescription("Description 1");
		step.setDescriptionComment("Description comment 1");
		step.setExpected("Expected 1");
		step.setExpectedComment("Expected comment 1");
		step.setExplanation("Explanation 1");
		step.setInput("Input 1");
		step.setInputComment("Input comment 1");
		step.setResult("PASSED");
		tcResult.addStep(step);

		TestCaseResult tcResult2 = new TestCaseResult("TC2", "TC2 description");
		tcResult2.setStatus("FAILED");
		tcResult2.setTime((long) 20);
		TestStepResult step2 = new TestStepResult();
		step2.setActual("Actual 2");
		step2.setActualComment("Actual comment 2");
		step2.setDescription("Description 2");
		step2.setDescriptionComment("Description comment 2");
		step2.setExpected("Expected 2");
		step2.setExpectedComment("Expected comment 2");
		step2.setExplanation("Explanation 2");
		step2.setInput("Input 2");
		step2.setInputComment("Input comment 2");
		step2.setResult("FAILED");
		tcResult2.addStep(step2);

		rsSuite.addTestCase(tcResult);
		rsSuite.addTestCase(tcResult2);

		ExcelExporter excel = new ExcelExporter();
		excel.createTestResult(rsSuite, "test-output/actual.xlsx");

		Workbook actual = new XSSFWorkbook("test-output/actual.xlsx");
		Workbook expected = new XSSFWorkbook("testcase/expect/expected.xlsx");
//		MatcherAssert.assertThat(actual, sameWorkbook(expected));

	}
}
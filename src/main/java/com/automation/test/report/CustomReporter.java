package com.automation.test.report;

import com.automation.test.define.TestResultComparator;
import com.automation.test.result.TestCaseResult;
import com.automation.test.result.TestSuiteResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class CustomReporter implements IReporter {
	
	@Override
	public void generateReport(List<XmlSuite> arg0, List<ISuite> suites, String outputDir) {
		// create html and customize
		File file = new File(outputDir + "\\TestExecutionSuite.html");

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			String htmlHead = "<!DOCTYPE html><html><head><style>body { background-color:#F7F9F9 } table { width:50%; } table, th, td { border: 1px solid black; border-collapse: collapse; }"
					+ " th, td { padding: 5px;text-align: left; }"
					+ " table tr:nth-child(even) { background-color: #D4E6F1; }"
					+ " table tr:nth-child(odd) { background-color:#E6E6FA; } table th { background-color: #87CEFA; color: Black; }</style>"
					+ "</head><body><div align=\"center\">" + "<h1>Automation Test Suite Execution Report</h1>"
					+ "<table><tr bgcolor=#87CEFA>" + "<th><center><b>No</b></center></th>"
					+ "<th><center><b>Suite Name</b></center></th>"
					+ "<th><center><b>Total Test Cases</b></center></th>" + "<th><center><b>Passed</b></center></th>"
					+ "<th><center><b>Failed</b></center></th>" + "<th><center><b>Skipped</b></center></th>"
					+ "<th><center><b>Start time</b></center></th>" + "<th><center><b>End Time</b></center></th>"
					+ "<th><center><b>Total Time</b></center></th>" + "<th><center><b>Result</b></center></th>"
					+ "<th><center><b>Details</b></center></th></tr>";
			bufferedWriter.write(htmlHead);

			int i = 0;
			for (ISuite suite : suites) {
				String suiteName = suite.getName();
				Object server = suite.getAttribute("server");
				String outputLocalFile = "";
				if (server != null)
					outputLocalFile = suiteName + "_Regression_Test_On_" + server.toString().toUpperCase() + ".xlsx";
				else
					outputLocalFile = suiteName + "_Regression_Test.xlsx";
				String outputFile = String.format("<a href=\"%s\">%s</a>", outputLocalFile, outputLocalFile);

				Map<String, ISuiteResult> suiteResults = suite.getResults();
				for (ISuiteResult suiteResult : suiteResults.values()) {
					ITestContext itextContext = suiteResult.getTestContext();
					int nPasses = itextContext.getPassedTests().getAllResults().size();
					int nFails = itextContext.getFailedTests().getAllResults().size();
					int nSkips = itextContext.getSkippedTests().getAllResults().size();
					int nTCs = nPasses + nFails + nSkips;
					Date start = itextContext.getStartDate();
					Date end = itextContext.getEndDate();
					DateFormat formater = new SimpleDateFormat("HH:mm:ss");
					long totalTime = end.getTime() - start.getTime();

					i++;
					String detailResultPosition = String.format("<a href=\"#%s\">%s</a>", suiteName, "Go to details");
					String htmlSuitesSummary = "<tr bgcolor=#E6E6FA> " + "<td><center><b>" + i + "</b></center></td>"
							+ "<td><center><b>" + suiteName + "</b></center></td>" + "<td><center><b>" + nTCs
							+ "</b></center></td>" + "<td><center><b>" + nPasses + "</b></center></td>"
							+ "<td><center><b>" + nFails + "</b></center></td>" + "<td><center><b>" + nSkips
							+ "</b></center>" + "<td><center><b>" + formater.format(start) + "</b></center></td>"
							+ "<td><center><b>" + formater.format(end) + "</b></center></td>" + "<td><center><b>"
							+ DurationFormatUtils.formatDuration(totalTime, "HH:mm:ss.SSS") + "</b></center></td>"
							+ "<td><center><b>" + outputFile + "</b></center></td>" + "<td><center><b>"
							+ detailResultPosition + "</b></center></td></tr>";
					bufferedWriter.append(htmlSuitesSummary);
				}
			}
			bufferedWriter.append("</table>");

			for (ISuite theSuite : suites) {
				String suiteName = theSuite.getName();
				Object server = theSuite.getAttribute("server");
				Object buildNum = theSuite.getAttribute("buildNum");

				String outputLocalFile = "";
				if (server != null)
					outputLocalFile = suiteName + "_Regression_Test_On_" + server.toString().toUpperCase() + ".xlsx";
				else
					outputLocalFile = suiteName + "_Regression_Test.xlsx";
				TestSuiteResult rsSuite = new TestSuiteResult();

				Map<String, ISuiteResult> testsWithResult = theSuite.getResults();
				String suiteNameId = theSuite.getName();
				rsSuite.setSuiteName(suiteNameId);
				rsSuite.setReportTime(LocalDateTime.now());
				if (server != null)
					rsSuite.setSapInfo(server.toString().toUpperCase());
				if (buildNum != null)
					rsSuite.setBuildNum(buildNum.toString());

				List<ITestNGMethod> methodList = theSuite.getAllMethods();
				if (methodList.size() > 0) {
					String goToSummaryPath = "<a href=\"#top\">Go to summary</a>";
					String eachSuiteHeaderText = "<br /><br /><br /><table id=\"" + suiteNameId
							+ "\"><tr bgcolor=#87CEFA>" + "<th><center><b>" + theSuite.getName() + "</b></center></th>"
							+ "<th><center><b>" + goToSummaryPath + "</b></center></th>" + "</tr>";
					bufferedWriter.append(eachSuiteHeaderText);

					String eachSuiteHeaderCol = "<tr bgcolor=#87CEFA>"
							+ "<td width=\"7%\"><center><b>No</b></center></td>"
							+ "<td width=\"50%\"><center><b>TC Name</b></center></td>"
							+ "<td width=\"10%\"><center><b>Status</b></center></td>"
							+ "<td width=\"10%\"><center><b>Duration</b></center></td>" + "</tr>";
					bufferedWriter.append(eachSuiteHeaderCol);
				}

				for (ISuiteResult result : testsWithResult.values()) {
					ITestContext testContext = result.getTestContext();

					Set<ITestResult> allTestResult = new HashSet<ITestResult>();
					// Failed test result
					Set<ITestResult> failedResult = testContext.getFailedTests().getAllResults();
					allTestResult.addAll(failedResult);

					// Passed test result
					Set<ITestResult> passResult = testContext.getPassedTests().getAllResults();
					allTestResult.addAll(passResult);

					// Skipped test result
					Set<ITestResult> skipResult = testContext.getSkippedTests().getAllResults();
					allTestResult.addAll(skipResult);

					rsSuite.setPassNo(passResult.size());
					rsSuite.setFailNo(failedResult.size());
					rsSuite.setSkipNo(skipResult.size());
					rsSuite.setTotal(passResult.size() + failedResult.size() + skipResult.size());

					String status = null;
					i = 0;
					
					List<ITestResult> results = new ArrayList<>(allTestResult);
					Collections.sort(results, new TestResultComparator());
					
					for (ITestResult it : results) {
						Object objResult = it.getAttribute("result");
						if (objResult != null) {
							TestCaseResult tcResult = (TestCaseResult) objResult;

							String resultFormat = "";
							if (it.getStatus() == 1) {
								status = "PASSED";
								resultFormat = "<td><center><font color=green><b>" + status
										+ "</b></font></center></td>";
							} else if (it.getStatus() == 2) {
								status = "FAILED";
								resultFormat = "<td><center><font color=red><b>" + status + "</b></font></center></td>";
							} else if (it.getStatus() == 3) {
								status = "SKIPPED";
								resultFormat = "<td><center><font color=black><b>" + status
										+ "</b></font></center></td>";
							}

							long duration = ((it.getEndMillis() - it.getStartMillis()));
							tcResult.setStatus(status);
							tcResult.setTime(duration);
							rsSuite.addTestCase(tcResult);
							i++;
							String eachTestCase = "<tr bgcolor=#E6E6FA>" + "<td><center><b>" + i + "</b></center></td>"
									+ "<td><center><b>" + tcResult.getDescription() + "</b></center></td>"
									+ resultFormat + "<td><center><b>"
									+ DurationFormatUtils.formatDuration(duration, "HH:mm:ss.SSS")
									+ "</b></center></td>" + "</tr>";
							bufferedWriter.append(eachTestCase);
						}

					}
					ExcelExporter excel = new ExcelExporter();
					String fileName = String.format("%s/%s", outputDir, outputLocalFile);
					excel.createTestResult(rsSuite, fileName);
				}
				bufferedWriter.append("</table>");
				bufferedWriter.append("</div></body></html>");

				if (buildNum != null) {
					// copy to folder /test-archive/build_<number>
					File srcDir = new File(outputDir);
					File destDir = new File("test-archive\\build_" + buildNum);
					FileUtils.copyDirectory(srcDir, destDir);
				}
			}

			bufferedWriter.flush();
			fileWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}

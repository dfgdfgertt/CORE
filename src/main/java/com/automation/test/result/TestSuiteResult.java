package com.automation.test.result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestSuiteResult {
	private List<TestCaseResult> testCases;
	private String suiteName;
	private LocalDateTime reportTime;
	private String sapInfo;
	private int passNo;
	private int failNo;
	private int skipNo;
	private int total;
	private String buildNum;

	public LocalDateTime getReportTime() {
		return reportTime;
	}

	public void setReportTime(LocalDateTime reportTime) {
		this.reportTime = reportTime;
	}

	public String getSapInfo() {
		return sapInfo;
	}

	public void setSapInfo(String sapInfo) {
		this.sapInfo = sapInfo;
	}

	public String getBuildNum() {
		return buildNum;
	}

	public void setBuildNum(String buildNum) {
		this.buildNum = buildNum;
	}
	
	public int getPassNo() {
		return passNo;
	}

	public void setPassNo(int passNo) {
		this.passNo = passNo;
	}

	public int getFailNo() {
		return failNo;
	}

	public void setFailNo(int failNo) {
		this.failNo = failNo;
	}

	public int getSkipNo() {
		return skipNo;
	}

	public void setSkipNo(int skipNo) {
		this.skipNo = skipNo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public TestSuiteResult() {
		testCases = new ArrayList<TestCaseResult>();
	}

	public List<TestCaseResult> getTestCases() {
		return testCases;
	}

	public void addTestCase(TestCaseResult tc) {
		testCases.add(tc);
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
}

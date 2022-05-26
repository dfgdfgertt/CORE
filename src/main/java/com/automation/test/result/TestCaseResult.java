package com.automation.test.result;

import java.util.ArrayList;
import java.util.List;

public class TestCaseResult {
	private String name;
	private String description;
	private String status;
	private long time;
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	private List<TestStepResult> steps;
	
	public TestCaseResult(String name, String description) {
		this.name = name;
		this.description = description;
		steps = new ArrayList<TestStepResult>();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<TestStepResult> getSteps() {
		return steps;
	}

	public String isStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSteps(List<TestStepResult> steps) {
		this.steps = steps;
	}

	public void addStep(TestStepResult step) {
		steps.add(step);
	}
	
	public void addSteps(List<TestStepResult> moreSteps) {
		steps.addAll(moreSteps);
	}
	
	@Override
	public String toString() {
		return name;
	}
}

package com.automation.test;

import com.automation.test.result.TestStepResult;
import com.automation.test.reader.Reader;
import com.automation.test.reader.SimpleReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class TestLoop<T> extends TestStep {
	
	private String description;
	private List<TestStepResult> results;
	private TestVerification<?> verification;
	
	private Reader<List<T>> sourceReader;
	private SimpleReader<T> runtimeReader;
	
	private Integer expectedLoop;
	
	public TestLoop(String description, Reader<List<T>> sourceReader) {
		super();
		this.description = description;
		this.sourceReader = sourceReader;
		
		results = new ArrayList<TestStepResult>();
		runtimeReader = new SimpleReader<>();
	}

	public Reader<T> getRuntimeReader() {
		return runtimeReader;
	}
	
	public Integer getExpectedLoop() {
		return expectedLoop;
	}

	public void setExpectedLoop(Integer expectedLoop) {
		this.expectedLoop = expectedLoop;
	}

	@Override
	public List<TestStepResult> getResults() {
		return results;
	}
	
	private String getActualString(Object actual) {
		if (actual != null) {
			if (actual instanceof JsonArray) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				actual = gson.toJson(actual);
			}
			return String.format("%s", actual);
		} else {
			return "";
		}
	}
	
	public void setVerification(TestVerification<?> verification) {
		this.verification = verification;
	}

	@Override
	public void run() {
		TestStepResult result = new TestStepResult();
		result.setDescription(description);
		
		if (verification == null) {
			String errString = "Verification was not set";
			result.setActual(errString);
			result.setResult("FAILED");
			results.add(result);
			
			Assert.fail(errString);
		}
		
		try {
			List<T> sources = sourceReader.getData();
			if (expectedLoop != null && sources.size() != expectedLoop) {
				result.setResult("FAILED");
				results.add(result);
				
				Assert.fail(String.format("Source list size mismatches. Expected: %d, actual: %d", expectedLoop, sources.size()));
			}
			
			for (int i = 0; i < sources.size(); i++) {
				T src = sources.get(i);
				
				runtimeReader.setData(src);
				
				boolean isPassed = true;
				
				try {
					isPassed = verification.isOk();
					Object actual = verification.getActual();
					result.setActual(getActualString(actual));
					result.setExplanation(verification.getExplanation());
					result.setExpected(verification.getExpectedDescription());
					
					if (isPassed == false) {
						Assert.fail(verification.getMessage());
					}
				} catch (Exception e) {
					result.setExpected(verification.getExpectedDescription());
					result.setActual(e.getMessage());
					isPassed = false;
					Assert.fail(e.getMessage());
				} finally {
					result.setResult(isPassed ? "PASSED" : "FAILED");
					results.add(result);
					verification.reset();
				}
				
				// Create for new verification result
				result = new TestStepResult();
			}
		}
		catch (Exception e) {
			result.setResult("FAILED");
			results.add(result);
			
			Assert.fail(e.getMessage());
		}
	}

}

package com.automation.test;

import com.automation.test.result.TestStepResult;
import com.automation.test.publisher.Publisher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * An automation action which can be verified by single or multiple verification
 * 
 * @author peter.tu.tran
 *
 */
public class TestAction extends TestStep {

	private Logger logger = Logger.getLogger(this.getClass());

	private String description;
	private Publisher<?> publisher;
	private List<TestVerification<?>> verifications;
	
	/**
	 * Action construction
	 * 
	 * @param description Explains what this action does
	 * @param publisher   Provides method to send out data or perform a specific
	 *                    action
	 */
	public TestAction(String description, Publisher<?> publisher) {
		results = new ArrayList<TestStepResult>();
		this.description = description;
		this.publisher = publisher;
		this.verifications = new ArrayList<TestVerification<?>>();
	}

	/**
	 * Add more verification
	 * 
	 * @param verification
	 */
	public void addVerification(TestVerification<?> verification) {
		verifications.add(verification);
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

	@Override
	public void run() {
		TestStepResult result = new TestStepResult();
		result.setDescription(description);

		if (publisher != null) {
			try {
				publisher.publish();
				result.setInput(String.format("%s", publisher.getInput()));
			} catch (Exception e) {
				result.setInput(e.getMessage());
				results.add(result);
				Assert.fail(e.getMessage());
			}
		}

		if (verifications.size() > 0) {
			for (TestVerification<?> verification : verifications) {
				logger.debug("Run verification:\n" + verification);
				boolean isPassed = true;

				try {
					isPassed = verification.isOk();
					Object actual = verification.getActual();
					result.setActual(getActualString(actual));
					result.setExplanation(verification.getExplanation());
					result.setExpected(verification.getExpectedDescription());
					result.setVerifiableInstruction(verification.getVerifiableInstruction());

					if (isPassed == false) {
						Assert.fail(verification.getMessage());
					}
				} catch (Throwable e) {
					result.setExpected(verification.getExpectedDescription());
					result.setExplanation(verification.getExplanation());
					isPassed = false;
					Assert.fail(e.getMessage());
				} finally {
					result.setResult(isPassed ? "PASSED" : "FAILED");
					results.add(result);
				}

				// Create for new verification result
				result = new TestStepResult();
			}
		}
		else {
			results.add(result);
		}
	}

}

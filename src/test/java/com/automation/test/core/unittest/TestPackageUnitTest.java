package com.automation.test.core.unittest;

import com.automation.test.TestAction;
import com.automation.test.TestCase;
import com.automation.test.TestVerification;
import com.automation.test.helper.FileHelper;
import com.automation.test.publisher.PlainTextFilePublisher;
import com.automation.test.reader.PlainTextFileReader;
import com.automation.test.report.CustomReporter;
import com.automation.test.verifier.SimpleVerifier;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

@Listeners(CustomReporter.class)
public class TestPackageUnitTest {
	@Test(expectedExceptions = AssertionError.class)
	public void testBadPublisher() {
		TestCase tc = new TestCase("Sample", "A long sample description");

		PlainTextFilePublisher publisher = new PlainTextFilePublisher("smb://@#$@$#@/input.txt");
		publisher.setInput("Sample input");
		TestAction step = new TestAction("Write some text to a text file", publisher);
		tc.addStep(step);

		tc.run();
	}

	@Test
	public void testSample() {
		TestCase tc = new TestCase("Sample", "A long sample description");
		assertEquals(tc.toString(), "Sample");

		PlainTextFilePublisher publisher = new PlainTextFilePublisher("input.txt");
		publisher.setInput("Sample input");
		TestAction step = new TestAction("Write some text to a text file", publisher);

		PlainTextFileReader reader = new PlainTextFileReader("input.txt");
		SimpleVerifier<String> verifier = new SimpleVerifier<>();
		verifier.setExpected("Sample input");
		TestVerification<?> verification = new TestVerification<>(reader, verifier);
		verification.setDelayTime(500);
		verification.setVerifiableInstruction("Simple verifier");
		assertEquals(verification.getVerifiableInstruction(), "Simple verifier");
		assertEquals(verification.getVerifier().getClass(), SimpleVerifier.class);
		assertNull(verification.getMessage());
		assertEquals(verification.getExpectedDescription(), "Simple verifier\nSample input");
		step.addVerification(verification);
		tc.addStep(step);
		tc.run();

		FileHelper helper = new FileHelper();
		helper.deleteFilesWithPattern("./", "input.txt");
	}

	@Test()
	public void testBadVerification() {
		TestVerification<?> outVerify = new TestVerification<>(null, null);
		assertEquals(outVerify.getMessage(), "Verifier was not set");
	}
}

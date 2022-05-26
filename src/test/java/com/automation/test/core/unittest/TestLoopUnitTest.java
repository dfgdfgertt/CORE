package com.automation.test.core.unittest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.automation.test.TestCase;
import com.automation.test.TestLoop;
import com.automation.test.TestVerification;
import com.automation.test.reader.FilePatternListing;
import com.automation.test.reader.FileReader;
import com.automation.test.reader.TextFileReader;
import com.automation.test.verifier.RuntimeExpectable;
import com.automation.test.verifier.SimpleVerifier;

public class TestLoopUnitTest {

	private Path tmpDirPath;

	@BeforeClass
	public void init() throws IOException {
		tmpDirPath = Files.createTempDirectory("looptest_");

		for (int i = 0; i < 2; i++) {
			com.google.common.io.Files
					.asCharSink(Paths.get(tmpDirPath.toString(), "\\", i + ".txt").toFile(), StandardCharsets.UTF_8)
					.write(String.valueOf(i));
		}
	}

	@AfterClass
	public void cleanUp() throws IOException {
		FileUtils.deleteDirectory(tmpDirPath.toFile());
	}

	@Test
	public void testLoop() {
		TestCase tc = new TestCase("TestLoop", "Test Loop");

		FilePatternListing fileListing = new FilePatternListing(tmpDirPath.toString() + "\\", ".*");
		TestLoop<String> step = new TestLoop<>("Verify output files", fileListing);
		FileReader fileReader = new FileReader(step.getRuntimeReader());
		TextFileReader textReader = new TextFileReader(fileReader);
		SimpleVerifier<String> outVerifier = new SimpleVerifier<String>();

		List<String> strs = new ArrayList<String>(Arrays.asList("0", "1"));
		outVerifier.setRuntimeExpected(new RuntimeExpectable<String>() {
			@Override
			public String getExpected() throws Exception {
				String str = strs.get(0);
				strs.remove(0);
				return str;
			}
		});
		TestVerification<?> outVerify = new TestVerification<>(textReader, outVerifier);
		step.setVerification(outVerify);
		tc.addStep(step);

		tc.run();
	}

//	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "null")
	public void testBadSource() {
		TestCase tc = new TestCase("TestLoop", "Test bad source");

		TestLoop<String> step = new TestLoop<>("Verify output files", null);
		FileReader fileReader = new FileReader(step.getRuntimeReader());
		TextFileReader textReader = new TextFileReader(fileReader);
		SimpleVerifier<String> outVerifier = new SimpleVerifier<String>();
		TestVerification<?> outVerify = new TestVerification<>(textReader, outVerifier);
		step.setVerification(outVerify);
		tc.addStep(step);

		tc.run();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Source list size mismatches. Expected: 1, actual: 2")
	public void testLessExpectedLoop() {
		TestCase tc = new TestCase("TestLoop", "Test less expectation loop");

		FilePatternListing fileListing = new FilePatternListing(tmpDirPath.toString() + "\\", ".*");
		TestLoop<String> step = new TestLoop<>("Verify output files", fileListing);
		step.setExpectedLoop(1);

		FileReader fileReader = new FileReader(step.getRuntimeReader());
		TextFileReader textReader = new TextFileReader(fileReader);
		SimpleVerifier<String> outVerifier = new SimpleVerifier<String>();

		TestVerification<?> outVerify = new TestVerification<>(textReader, outVerifier);
		outVerify.setVerifiableInstruction("Test less expectation loop. Expected loop: " + step.getExpectedLoop());
		step.setVerification(outVerify);
		tc.addStep(step);

		tc.run();
	}

//	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "null")
	public void testBadVerification() {
		TestCase tc = new TestCase("TestLoop", "Test bad verification");

		FilePatternListing fileListing = new FilePatternListing(tmpDirPath.toString() + "\\", ".*");
		TestLoop<String> step = new TestLoop<>("Verify output files", fileListing);

		TestVerification<?> outVerify = new TestVerification<>(null, null);
		step.setVerification(outVerify);
		tc.addStep(step);

		tc.run();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Verification was not set")
	public void testNoVerification() {
		TestCase tc = new TestCase("TestLoop", "Test no verification");

		FilePatternListing fileListing = new FilePatternListing(tmpDirPath.toString() + "\\", ".*");
		TestLoop<String> step = new TestLoop<>("Verify output files", fileListing);
		step.setVerification(null);
		tc.addStep(step);

		tc.run();
	}
}

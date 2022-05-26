package com.automation.test.core.unittest;

import com.automation.test.verifier.FileExistenceVerifier;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileExistenceVerifierUnitTest {

	@Test
	public void testLocalFileExist() {
		FileExistenceVerifier verifier = new FileExistenceVerifier(
				Paths.get(tmpDirPath.toString(), "\\0.txt").toString(), true, 2000);
		Assert.assertTrue(verifier.isOk(null));
	}

	@Test
	public void testLocalFileNotExist() {
		FileExistenceVerifier verifier = new FileExistenceVerifier(
				Paths.get(tmpDirPath.toString(), "\\1.txt").toString(), true, 2000);
		Assert.assertFalse(verifier.isOk(null));
	}

	@Test
	public void testBadPath() {
		FileExistenceVerifier verifier = new FileExistenceVerifier("smb://@$@#$@#%$@#%", true,
				2000);
		Assert.assertFalse(verifier.isOk(null));
	}

	private Path tmpDirPath;

	@BeforeClass
	public void init() throws IOException {
		tmpDirPath = Files.createTempDirectory("unittest_");
		com.google.common.io.Files
				.asCharSink(Paths.get(tmpDirPath.toString(), "\\0.txt").toFile(), StandardCharsets.UTF_8).write("1");
	}

	@AfterClass
	public void cleanUp() throws IOException {
		FileUtils.deleteDirectory(tmpDirPath.toFile());
	}
}

package com.automation.test.core.unittest;

import com.automation.test.exception.TestIOException;
import com.automation.test.reader.PlainTextFileReader;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;


public class PlainTextFileReaderUnitTest {
	private Path tmpDirPath;

	@Test
	public void testReader() throws Exception {
		PlainTextFileReader txtReader = new PlainTextFileReader(tmpDirPath.toString() + "\\0.txt");
		assertEquals(txtReader.read(), "0");
	}

	@Test
	public void testNoFile() throws Exception {
		PlainTextFileReader txtReader = new PlainTextFileReader(tmpDirPath.toString() + "\\1.txt");
		assertThrows(TestIOException.class, () -> txtReader.read());
	}

	@BeforeClass
	public void init() throws IOException {
		tmpDirPath = Files.createTempDirectory("unittest_");
		com.google.common.io.Files
				.asCharSink(Paths.get(tmpDirPath.toString(), "\\0.txt").toFile(), StandardCharsets.UTF_8).write("0");
	}

	@AfterClass
	public void cleanUp() throws IOException {
		FileUtils.deleteDirectory(tmpDirPath.toFile());
	}

}

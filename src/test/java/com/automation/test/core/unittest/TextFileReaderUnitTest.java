package com.automation.test.core.unittest;

import com.automation.test.exception.TestIOException;
import com.automation.test.reader.FileReader;
import com.automation.test.reader.SimpleStringBuilder;
import com.automation.test.reader.TextFileReader;
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
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class TextFileReaderUnitTest {
	private Path tmpDirPath;

	@Test
	public void testReader() throws Exception {
		FileReader reader = new FileReader(new SimpleStringBuilder(tmpDirPath.toString() + "\\0.txt"));
		TextFileReader txtReader = new TextFileReader(reader);
		assertEquals(txtReader.read(), "0");
		assertTrue(reader.toString()
				.contains("Reading data from input stream given by com.automation.test.reader.SimpleStringBuilder"));
	}

	@Test
	public void testNoFile() throws Exception {
		FileReader reader = new FileReader(new SimpleStringBuilder(tmpDirPath.toString() + "\\1.txt"));
		TextFileReader txtReader = new TextFileReader(reader);
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

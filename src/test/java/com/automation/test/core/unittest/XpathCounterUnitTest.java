package com.automation.test.core.unittest;

import com.automation.test.exception.TestIOException;
import com.automation.test.reader.FileReader;
import com.automation.test.reader.SimpleStringBuilder;
import com.automation.test.reader.XPathCounter;
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

public class XpathCounterUnitTest {
	private Path tmpDirPath;

	@Test
	public void testCounter() throws Exception {
		FileReader reader = new FileReader(new SimpleStringBuilder(tmpDirPath.toString() + "\\0.xml"));
		XPathCounter counter = new XPathCounter(reader, "//rows/row");
		assertEquals(counter.read(), 5.0);
	}
	
	@Test
	public void testBadXPath() throws Exception {
		FileReader reader = new FileReader(new SimpleStringBuilder(tmpDirPath.toString() + "\\0.xml"));
		XPathCounter counter = new XPathCounter(reader, "#@%$#$%#%%#$");
		assertThrows(TestIOException.class, () -> counter.read());
	}

	@BeforeClass
	public void init() throws IOException {
		tmpDirPath = Files.createTempDirectory("unittest_");
		com.google.common.io.Files
				.asCharSink(Paths.get(tmpDirPath.toString(), "\\0.xml").toFile(), StandardCharsets.UTF_8)
				.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><rows><row><field1>'ID103'</field1><field2>'THIS IS VALID TEST ID103'</field2><field3>120</field3></row><row><field1>ID10</field1><field2>WILL</field2><field3>TEST</field3></row><row><field1>ID12</field1><field2>JONAH</field2><field3>TEST</field3></row><row><field1>ID11</field1><field2>PETER</field2><field3>TEST</field3></row><row><field1>id1</field1><field2>khang</field2><field3>test1</field3></row></rows></response>");
	}

	@AfterClass
	public void cleanUp() throws IOException {
		FileUtils.deleteDirectory(tmpDirPath.toFile());
	}

}

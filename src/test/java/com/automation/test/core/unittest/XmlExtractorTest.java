package com.automation.test.core.unittest;

import com.automation.test.exception.TestIOException;
import com.automation.test.reader.SimpleStringBuilder;
import com.automation.test.reader.XmlExtractor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlExtractorTest {
	private final String XML = "<person><name>Nobody</name><friend><name>None</name></friend></person>";
	
	@Test
	public void testXpathTextCorrect() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(XML));
		reader.setExtractOptions("//friend/name/text()");
		Assert.assertEquals(reader.getData(), "None");
		
		reader.setExtractOptions("//friend/name/text()", true);
		Assert.assertEquals(reader.getData(), "None");
	}
	
	@Test(expectedExceptions={TestIOException.class})
	public void testXpathTextIncorrect() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(XML));
		reader.setExtractOptions("//friend/name/t()");
		reader.getData();
	}
	
	@Test
	public void testXpathNodeCorrect() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(XML));
		reader.setExtractOptions("//friend", true);
		Assert.assertEquals(reader.getData(), "<friend><name>None</name></friend>");
	}
	
	@Test(expectedExceptions={TestIOException.class})
	public void testXpathNull() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(XML));
		reader.getData();
	}
	
	@Test(expectedExceptions={TestIOException.class})
	public void testSourceNull() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(null));
		reader.setExtractOptions("");
		reader.getData();
	}

	@Test(expectedExceptions={TestIOException.class})
	public void testSourceEmpty() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(""));
		reader.setExtractOptions("");
		reader.getData();
	}
	
	@Test(expectedExceptions={TestIOException.class})
	public void testSourceInvalid() throws Exception {
		XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder("???"));
		reader.setExtractOptions("???");
		reader.getData();
	}
}

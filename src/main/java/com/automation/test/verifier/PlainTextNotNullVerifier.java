package com.automation.test.verifier;

import com.automation.test.reader.SimpleStringBuilder;
import com.automation.test.reader.XmlExtractor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Verify 2 text data are same
 * 
 * @author peter.tu.tran
 *
 */
public class PlainTextNotNullVerifier extends AbstractDataVerifier<String> {
	private String type;
	private String value;

	public PlainTextNotNullVerifier(String type, String value) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.value = value;
	}

	public boolean isMatched(String expected, String actual) {
		if (this.type == "xml") {
			try {
				XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(actual));
				reader.setExtractOptions(this.value, true);
				if (reader.getData().isEmpty()) {
					msg = "PlainTextNotNullVerifier: Verify strings was empty";
					return false;
				}
				else {
					msg =	String.format("PlainTextNotNullVerifier: Verify strings not null. Expected: %s", reader.getData());
					return true;
				}
			} catch (Exception e) {
				msg = String.format("PlainTextNotNullVerifier: Error when parse Xml data: %s", actual); ;
				return false;
			}
		}
		if (this.type == "xmlNode") {
			try {
				XmlExtractor reader = new XmlExtractor(new SimpleStringBuilder(actual));
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(actual));

				Document doc = builder.parse(src);
								
				String xpathValue = doc.getElementsByTagName(this.value).item(0).getTextContent();						
				reader.setExtractOptions(this.value);
				if(xpathValue.isEmpty()) {
					msg = "PlainTextNotNullVerifier: Verify strings was empty";
					return false;
				}
				else {
					msg =	String.format("PlainTextNotNullVerifier: Verify strings not null. Expected: %s", reader.getData());
					return true;
				}
			} catch (Exception e) {
				msg = String.format("PlainTextNotNullVerifier: Error when parse Xml data: %s", actual); ;
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isOk(String actual) {
		return isMatched(getExpected(), actual);
	}

}

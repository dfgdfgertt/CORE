package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;

public class XPathCounter extends AbstractDecoratorReader<InputStream, Number> {

	private String xpathString;
	
	public XPathCounter(Reader<InputStream> reader, String xpathString) {
		super(reader);
		this.xpathString = xpathString;
	}

	@Override
	public Number read() throws Exception {
		Document doc = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        try (InputStream is = parent.getData()) {
        	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        	doc = documentBuilder.parse(is);
        } catch (IOException e) {
			throw new TestIOException("Input stream is incorrupted", e);
		} catch (ParserConfigurationException | SAXException  e) {
			throw new TestIOException("Could not parse xml document from input stream", e);
		}
 
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
 
        XPathExpression expr;
		try {
			expr = xpath.compile(String.format("count(%s)", xpathString));
			return (Number) expr.evaluate(doc, XPathConstants.NUMBER);
		} catch (XPathExpressionException e) {
			String msg = String.format("Invalid xpath expression '%s'", xpathString);
			throw new TestIOException(msg, e);
		}
	}

}

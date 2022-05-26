package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlExtractor extends AbstractDecoratorReader<String, String> {

	private String xPathExpression;
	private boolean isFullExtraction = false;

	public XmlExtractor(Reader<String> parent) {
		super(parent);
	}

	public void setExtractOptions(String xPathExpression) {
		this.setExtractOptions(xPathExpression, false);
	}
	
	public void setExtractOptions(String xPathExpression, boolean isFullExtraction) {
		this.xPathExpression = xPathExpression;
		this.isFullExtraction = isFullExtraction;
	}

	@Override
	public String read() throws Exception {
		if (xPathExpression == null) {
			throw new TestIOException("XPath expression argument can not be null");
		}
		
		String xml = parent.getData();
		if (xml == null || xml.isEmpty()) {
			throw new TestIOException("XML source is null or empty");
		}

		InputSource input = new InputSource(new StringReader(xml));
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			if (isFullExtraction) {
				Node node = (Node) xPath.evaluate(xPathExpression, input, XPathConstants.NODE);
				if (node != null) {
					try (StringWriter sw = new StringWriter()) {
						Transformer t = TransformerFactory.newInstance().newTransformer();
						t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						t.transform(new DOMSource(node), new StreamResult(sw));
	
						return sw.toString();
					} catch (TransformerException e) {
						throw new TestIOException("Cannot transform xml node to string", e);
					}
				} else {
					return null;
				}
			}
			else {
				return xPath.evaluate(xPathExpression, input);
			}
		} catch (XPathExpressionException e) {
			throw new TestIOException(String.format("XPath expression '%s' is not valid", xPathExpression), e);
		}
	}

}

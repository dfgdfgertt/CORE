package com.automation.test.verifier;

import com.automation.test.reader.Reader;
import org.apache.log4j.Logger;

import java.util.List;

public class TextsExistenceVerifier extends AbstractVerifier<String, List<String>> {

	private Logger logger = Logger.getLogger(this.getClass());
	private boolean usePattern = false;
	private boolean oppositeLogic = false;

	public void setUsePatternText(boolean usePattern) {
		this.usePattern = usePattern;
	}
	
	public void setOppositeLogic(boolean oppositeLogic) {
		this.oppositeLogic = oppositeLogic;
	}
	
	private Reader<List<String>> expectedReader = null; // Actual reader to get expected value

	public void setExpectedReader(Reader<List<String>> expectedReader) {
		this.expectedReader = expectedReader;
	}

	@Override
	public List<String> getExpected() {
		List<String> result = null;

		if (expectedReader != null) {
			try {
				result = expectedReader.getData();
			} catch (Exception e) {
				logger.warn("Can NOT get expected data from reader ! " + e.getMessage());
			}
		} else {
			result = super.getExpected();
		}

		return result;
	}

	@Override
	public boolean isOk(String actual) {
		this.explanation = "See detail in comment";
		List<String> texts = getExpected();

		String content = (String) actual;

		if(oppositeLogic) {
			for (String text : texts) {
				if (usePattern) {
					if (content.matches(text)) {
						msg = String.format("See details:",
								text, content);
						return false;
					}
				} else {
					if (content.contains(text)) {
						msg = String.format("See details:",
								text, content);
						return false;
					}
				}
			}
			this.explanation = String.format("See details:",
					texts.toString());
		}
		else {
			for (String text : texts) {
				if (usePattern) {
					if (!content.matches(text)) {
						msg = String.format("See details:",
								text, content);
						return false;
					}
				} else {
					if (!content.contains(text)) {
						msg = String.format("See details:",
								text, content);
						return false;
					}
				}
			}
			this.explanation = String.format("See details:",
					texts.toString());
		}
		
		return true;
	}

}

package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.util.ArrayList;
import java.util.List;

public class XmlVerifier extends AbstractDataVerifier<String> {
	
	private List<String> ignoredNodes;
	
	public XmlVerifier() {
		ignoredNodes = new ArrayList<String>();
	}
	
	public XmlVerifier(List<String> ignoredNodes) {
		this.ignoredNodes = ignoredNodes;
		if (this.ignoredNodes == null) {
			this.ignoredNodes = new ArrayList<>();
		}
	}

	@Override
	public boolean isOk(String actual) throws TestIOException {
		String expected = getExpected();
		Diff diff = DiffBuilder.compare(Input.fromString(expected)).withTest(Input.fromString(actual))
				.withNodeFilter(node -> !ignoredNodes.contains(node.getNodeName()))
				.checkForSimilar()
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
				.ignoreWhitespace()
				.build();
		msg = diff.toString();
		return !diff.hasDifferences();
	}

}

package com.automation.test.verifier;

import com.automation.test.exception.TestIOException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;

import java.util.Collection;

public class CollectionVerifier<T> extends AbstractDataVerifier<Collection<T>> {

	private Equator<T> equator;
	
	public CollectionVerifier(Equator<T> equator) {
		super();
		this.equator = equator;
	}

	@Override
	public boolean isOk(Collection<T> actual) throws TestIOException {
		Collection<T> expected = getExpected();
		msg = String.format("Compare 2 collections, expected: %s, actual: %s", expected, actual);
		return CollectionUtils.isEqualCollection(expected, actual, equator);
	}

}

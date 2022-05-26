package com.automation.test.verifier;

public class NumberCompareVerifier extends AbstractDataVerifier<Long> {
	protected CompareMethod compareMethod;

	public NumberCompareVerifier(CompareMethod compareMethod) {
		super();
		this.compareMethod = compareMethod;
	}

	@Override
	public boolean isOk(Long actual) {
		Long expected = getExpected();
		boolean ret = false;

		msg = String.format("NumberCompareVerifier: Compare number %s. Expected: %s, actual: %s", compareMethod.toString(), expected, actual);

		if (compareMethod == CompareMethod.Equal) {
			ret = (actual.equals(expected));
		} else if (compareMethod == CompareMethod.GreaterOrEqual) {
			ret = (actual >= expected);
		} else if (compareMethod == CompareMethod.Greater) {
			ret = (actual > expected);
		} // else future compare type

		return ret;
	}

}

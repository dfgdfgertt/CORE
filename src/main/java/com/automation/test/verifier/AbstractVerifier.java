package com.automation.test.verifier;

/**
 * Verify different data types
 * @author peter.tu.tran
 *
 * @param <ACT> Actual data type
 * @param <EXP> Expected data type
 */
public abstract class AbstractVerifier<ACT, EXP> implements Verifier<ACT, EXP> {

	protected String explanation;
	private EXP expected;
	private RuntimeExpectable<EXP> runtimeExpected;
	protected String msg;
	
	public EXP getExpected() {
		if (expected != null) {
			return expected;
		}
		else if (runtimeExpected != null) {
			try {
				return expected = runtimeExpected.getExpected();
			} catch (Exception e) {
				msg = e.getMessage();
				return null;
			}
		}
		else {
			return null;
		}
	}

	/**
	 * Set expected data
	 * @param expected Expected data
	 */
	public void setExpected(EXP expected) {
		this.expected = expected;
	}
	
	@Override
	public void resetCache() {
		if (runtimeExpected != null) {
			expected = null;
		}
	}

	public void setRuntimeExpected(RuntimeExpectable<EXP> runtimeExpected) {
		this.runtimeExpected = runtimeExpected;
	}

	@Override
	public String getMessage() {
		return msg;
	}

	@Override
	public String getExplanation() {
		return explanation;
	}
}

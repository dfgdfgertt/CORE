package com.automation.test.core.unittest;

import com.automation.test.verifier.CompareMethod;
import com.automation.test.verifier.NumberCompareVerifier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NumberCompareVerifierUnitTest {

	@Test
	public void testEqual() {
		NumberCompareVerifier verifier = new NumberCompareVerifier(CompareMethod.Equal);
		verifier.setExpected(2L);
		Assert.assertTrue(verifier.isOk(2L));
		Assert.assertFalse(verifier.isOk(1L));
		Assert.assertFalse(verifier.isOk(3L));
	}
	
	@Test
	public void testGreater() {
		NumberCompareVerifier verifier = new NumberCompareVerifier(CompareMethod.Greater);
		verifier.setExpected(2L);
		Assert.assertTrue(verifier.isOk(3L));
		Assert.assertFalse(verifier.isOk(2L));
		Assert.assertFalse(verifier.isOk(1L));
	}
	
	@Test
	public void testGreaterOrEqual() {
		NumberCompareVerifier verifier = new NumberCompareVerifier(CompareMethod.GreaterOrEqual);
		verifier.setExpected(2L);
		Assert.assertTrue(verifier.isOk(3L));
		Assert.assertTrue(verifier.isOk(2L));
		Assert.assertFalse(verifier.isOk(1L));
	}
	
}

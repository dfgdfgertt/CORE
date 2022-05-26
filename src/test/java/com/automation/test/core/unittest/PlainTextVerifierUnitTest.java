package com.automation.test.core.unittest;

import com.automation.test.verifier.PlainTextVerifier;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class PlainTextVerifierUnitTest {

	@Test
	public void test() throws Exception {
		PlainTextVerifier verifier = new PlainTextVerifier();
		assertFalse(verifier.isOk("david"));
		verifier.setExpected("david");
		assertTrue(verifier.isOk("david"));
		assertFalse(verifier.isOk(null));
	}

}

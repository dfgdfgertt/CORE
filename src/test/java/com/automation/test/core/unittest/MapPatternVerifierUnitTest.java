package com.automation.test.core.unittest;

import com.automation.test.verifier.MapPatternVerifier;
import com.google.common.collect.ImmutableMap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MapPatternVerifierUnitTest {

	@Test
	public void testMap() {
		MapPatternVerifier verifier = new MapPatternVerifier();		
		verifier.setExpected(ImmutableMap.of("Time", "\\d{12}"));
		Assert.assertTrue(verifier.isOk(ImmutableMap.of("Time", "191217170806")));
		verifier.setExpected(ImmutableMap.of("a1", "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}"));
		Assert.assertTrue(verifier.isOk(ImmutableMap.of("a1", "920c5e19-20b4-11ea-8f9c-0000003056ce")));
		Assert.assertFalse(verifier.isOk(ImmutableMap.of("b", "920c5e19-20b4-11ea-8f9c-0000003056ce")));
		Assert.assertFalse(verifier.isOk(ImmutableMap.of("a1", "213156253-20b4-11ea-8f9c-0000003056ce")));
	}

}

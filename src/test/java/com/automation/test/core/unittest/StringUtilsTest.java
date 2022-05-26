package com.automation.test.core.unittest;

import com.automation.test.helper.StringUtils;
import com.google.common.base.Strings;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringUtilsTest {
	@Test
	public void testStringUtils() {
		String str = "R5";
		String regex = "\\d+";
		
		String out = StringUtils.replace(str, regex, m -> Strings.padStart(m.group(), 3, '0'));
		
		Assert.assertEquals(out, "R005");
	}
}

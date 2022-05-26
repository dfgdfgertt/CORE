package com.automation.test.define;

import com.automation.test.helper.StringUtils;
import com.google.common.base.Strings;
import org.testng.ITestResult;

import java.util.Comparator;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class TestResultComparator implements Comparator<ITestResult> {
	
	private Pattern pattern;
	private Function<MatchResult, String> callback;
	
	public TestResultComparator() {
		pattern = Pattern.compile("\\d+");
		callback = m -> Strings.padStart(m.group(), 3, '0');
	}
	
	@Override
	public int compare(ITestResult r1, ITestResult r2) {
		// Don't check null here cause I think it never happens
		Object[] params1 = r1.getParameters();
		Object[] params2 = r2.getParameters();
		if (params1.length > 0 && params2.length > 0) {
			String name1 = params1[0].toString();
			String name2 = params2[0].toString();
			
			name1 = StringUtils.replace(name1, pattern, callback);
			name2 = StringUtils.replace(name2, pattern, callback);
			
			
			return name1.compareTo(name2);
		}
		
		return 0;
	}
	
}

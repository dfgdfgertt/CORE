package com.automation.test.publisher.helper;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String replace(CharSequence str, String regex, Function<MatchResult, String> callback) {
		return replace(str, Pattern.compile(regex), callback);
	}
	
	public static String replace(CharSequence str, Pattern p, Function<MatchResult, String> callback) {
	    Matcher m = p.matcher(str);
	    StringBuffer sb = new StringBuffer();
	    while (m.find()) {
	        m.appendReplacement(sb, callback.apply(m.toMatchResult()));
	    }
	    m.appendTail(sb);
	    return sb.toString();
	}
}

package com.automation.test.core.unittest;

import com.automation.test.reader.IPv4StringBuilder;
import com.automation.test.reader.SimpleStringBuilder;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;


public class IPv4StringBuilderUnitTest {
	@Test
	public void testCorrectIPv4() throws Exception {
		IPv4StringBuilder reader = new IPv4StringBuilder(new SimpleStringBuilder("11.111.111.11"));
		assertEquals(reader.read(), "11.111.111.11");
	}

	@Test
	public void testWrongIP() throws Exception {
		IPv4StringBuilder reader = new IPv4StringBuilder(new SimpleStringBuilder("#$%#$%#$%#$"));
		assertEquals(reader.read(), "");
	}
	
	@Test
	public void testConvertible() throws Exception {
		IPv4StringBuilder reader = new IPv4StringBuilder(new SimpleStringBuilder("0000:0000:0000:0000:0000:ffff:0b6f:6f0b"));
		assertEquals(reader.read(), "11.111.111.11");
		
		reader = new IPv4StringBuilder(new SimpleStringBuilder("::ffff:0b6f:6f0b"));
		assertEquals(reader.read(), "11.111.111.11");
	}
	
	@Test
	public void testNotConvertible() throws Exception {
		IPv4StringBuilder reader = new IPv4StringBuilder(new SimpleStringBuilder("0000:0000:3242:0000:0000:ffff:0b6f:6f0b"));
		assertEquals(reader.read(), "");
	}
}

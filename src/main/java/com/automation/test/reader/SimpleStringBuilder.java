package com.automation.test.reader;

public class SimpleStringBuilder extends AbstractReader<String> {
	
	private String str;
	
	public SimpleStringBuilder(String str) {
		this.str = str;
	}
	
	@Override
	public String read() {
		return str;
	}

}

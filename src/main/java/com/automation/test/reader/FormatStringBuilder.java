package com.automation.test.reader;

public class FormatStringBuilder extends AbstractDecoratorReader<String, String> {

	private String format;
	
	public FormatStringBuilder(Reader<String> parent) {
		super(parent);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String read() throws Exception {
		String raw = parent.getData();
		if (format == null) {
			return raw;
		}
		else {
			return String.format(format, raw);
		}
	}

}

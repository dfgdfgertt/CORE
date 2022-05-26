package com.automation.test.reader;

public class BinaryToStringConverter extends AbstractDecoratorReader<byte[], String> {

	
	public BinaryToStringConverter(Reader<byte[]> parent) {
		super(parent);
	}

	@Override
	public String read() throws Exception {
		byte[] data = parent.getData();
		if (data != null) {
			return new String(data);
		}
		else {
			return null;
		}
	}

}

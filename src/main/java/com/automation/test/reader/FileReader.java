package com.automation.test.reader;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Create input stream from a shared file
 * 
 * @author peter.tu.tran
 *
 */
public class FileReader extends AbstractDecoratorReader<String, InputStream> {

	/**
	 * Constructor
	 * 
	 * @param parent Reader which provides link to shared file
	 */
	public FileReader(Reader<String> parent) {
		super(parent);
	}

	@Override
	public InputStream read() throws Exception {
		String filepath = parent.getData();
		return new FileInputStream(filepath);
	}
	
	@Override
	public String toString() {
		return String.format("Reading data from input stream given by %s", parent);
	}

}

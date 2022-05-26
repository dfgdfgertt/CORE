package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class BinaryFileReader extends AbstractDecoratorReader<InputStream, byte[]> {

	/**
	 * Constructor
	 * 
	 * @param parent Reader which provides input stream
	 */
	public BinaryFileReader(Reader<InputStream> parent) {
		super(parent);
	}

	@Override
	public byte[] read() throws Exception {
		byte[] arr = null;

		try (InputStream is = parent.getData()) {
			arr = IOUtils.toByteArray(is);
		} catch (IOException e) {
			throw new TestIOException("Input stream is incorrupted", e);
		}

		return arr;
	}

}

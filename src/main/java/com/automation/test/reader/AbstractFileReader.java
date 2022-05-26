package com.automation.test.reader;

/**
 * File reader
 * @author peter.tu.tran
 *
 * @param <T> Return data type
 */
public abstract class AbstractFileReader<T> extends AbstractReader<T> {

	protected String filepath;
	
	public AbstractFileReader(String filepath) {
		this.filepath = filepath;
	}

}

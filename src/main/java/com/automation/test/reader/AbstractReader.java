package com.automation.test.reader;

/**
 * A reader which can have a previous reader to get input
 * 
 * @author peter.tu.tran
 * @param <T> Return data type
 */
public abstract class AbstractReader<T> implements Reader<T> {

	private T data;

	public T getData() throws Exception {
		if (data == null) {
			data = this.read();
		}

		return data;
	}

	@Override
	public void resetCache() {
		data = null;
	}

}

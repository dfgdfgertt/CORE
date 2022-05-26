package com.automation.test.reader;

public class SimpleReader<T> extends AbstractReader<T> {

	T data;
	
	public void setData(T data) {
		this.data = data;
	}

	@Override
	public T read() {
		return data;
	}

}

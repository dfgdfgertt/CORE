package com.automation.test.publisher;

public abstract class AbstractFilePublisher<T> extends AbstractPublisher<T> {

	protected String filepath;
	
	public AbstractFilePublisher(String filepath) {
		this.filepath = filepath;
	}
	
}

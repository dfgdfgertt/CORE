package com.automation.test.publisher;

import org.apache.log4j.Logger;

/**
 * Common publisher
 * @author peter.tu.tran
 *
 */
public abstract class AbstractPublisher<T> implements Publisher<T> {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected T input;

	public T getInput() {
		return input;
	}

	/**
	 * Set input data
	 * @param input
	 */
	public void setInput(T input) {
		this.input = input;
	}
	
	// TODO: Remove this and implement in other class
	@Override
	public String getAction() {
		return null;
	}

}

package com.automation.test.reader;

public abstract class AbstractDecoratorReader<IN, OUT> extends AbstractReader<OUT> {
	protected Reader<IN> parent;

	public AbstractDecoratorReader(Reader<IN> parent) {
		super();
		this.parent = parent;
	}
	
	@Override
	public void resetCache() {
		super.resetCache();
		parent.resetCache();
	}
	
}

package com.automation.test.reader;

import com.automation.test.exception.TestIOException;

import java.util.List;

public class IndexExtractor<T> extends AbstractDecoratorReader<List<T>, T> {

	private int index = 0;
	
	public IndexExtractor(Reader<List<T>> parent, int index) {
		super(parent);
		this.index = index;
	}

	@Override
	public T read() throws Exception {
		List<T> list = parent.getData();
		if (list == null) {
			throw new TestIOException("Input list is null");
		}
		if (list.size() > index) {
			return list.get(index);
		}
		else {
			String msg = String.format("Input list is too small. Expected index: %d, list size: %d", index, list.size());
			throw new TestIOException(msg);
		}
	}

}

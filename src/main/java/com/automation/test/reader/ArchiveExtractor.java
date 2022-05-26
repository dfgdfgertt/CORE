package com.automation.test.reader;

public abstract class ArchiveExtractor extends AbstractDecoratorReader<byte[], byte[]>{

	public ArchiveExtractor(Reader<byte[]> binaryReader) {
		super(binaryReader);
	}
	
}

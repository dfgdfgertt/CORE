package com.automation.test.reader;

import com.automation.test.exception.TestIOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PlainTextFileReader extends AbstractFileReader<String> {

	public PlainTextFileReader(String filepath) {
		super(filepath);
	}

	@Override
	public String read() throws TestIOException {
		File file = new File(filepath);

		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				return reader.readLine();
			} catch (IOException e) {
				throw new TestIOException(String.format("Could not read file '%s', please check the permission!", filepath), e);
			}
		} else {
			throw new TestIOException(String.format("File '%s' is not found!", filepath));
		}
	}

}

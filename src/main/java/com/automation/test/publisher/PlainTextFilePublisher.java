package com.automation.test.publisher;

import com.automation.test.exception.TestIOException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PlainTextFilePublisher extends AbstractFilePublisher<String> {

	public PlainTextFilePublisher(String filepath) {
		super(filepath);
	}

	@Override
	public void publish() throws TestIOException {
		if (input != null) {
			String str = input.toString();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
				writer.write(str);
			} catch (IOException e) {
				throw new TestIOException(String.format(
						"Could not create a text file '%s', please check the permission or correct file path!",
						filepath), e);
			}
		}
	}

}

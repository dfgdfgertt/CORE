package com.automation.test.reader;

import com.automation.test.report.CustomReporter;
import org.testng.annotations.Listeners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Listeners(CustomReporter.class)
public class TextRemoval extends AbstractDecoratorReader<String, String> {

	private List<String> removingTexts;
	
	public TextRemoval(Reader<String> parent) {
		super(parent);
	}
	
	public void setRemovingTexts(List<String> removingTexts) {
		this.removingTexts = removingTexts;
	}

	@Override
	public String read() throws Exception {
		if (removingTexts != null && removingTexts.size() > 0) {
			String line;
			try (
				StringWriter sw = new StringWriter();
				BufferedReader reader = new BufferedReader(new StringReader(parent.getData()));
				BufferedWriter writer = new BufferedWriter(sw)) {
				
				while ((line = reader.readLine()) != null) {
					final String l = line;
					if (!removingTexts.stream().anyMatch(s -> l.contains(s))) {
						writer.write(line);
						writer.newLine();
					}
				}
				writer.flush();
				
				return sw.toString();
			}
		}
		else {
			return parent.getData();
		}
	}

}

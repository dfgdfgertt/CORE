package com.automation.test.reader;

import com.automation.test.exception.TestIOException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Read all text from input stream
 *
 * @author peter.tu.tran
 *
 */
public class TextFileReader extends AbstractDecoratorReader<InputStream, String> {

	private boolean isRemoveText;
	private String removedText;

	/**
	 * Constructor
	 *
	 * @param parent Reader which provides input stream
	 */
	public TextFileReader(Reader<InputStream> parent) {
		super(parent);
	}

	public void isRemoveText(boolean isRemoveText){this.isRemoveText = isRemoveText;}
	public void setRemoveText(String removedText){this.removedText = removedText;}

	@Override
	public String read() throws Exception {
		String str = null;

		try (InputStream is = parent.getData()) {
			str = IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new TestIOException("Input stream is incorrupted", e);
		}

		if(isRemoveText){
			str = str.replace(removedText,"");
		}

		return str;
	}

}

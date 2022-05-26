package com.automation.test.reader;

import com.automation.test.exception.TestIOException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePatternListing extends AbstractReader<List<String>> {

	protected String folderPath;
	protected String pattern;
	
	public FilePatternListing(String folderPath, String pattern) {
		super();
		this.folderPath = folderPath;
		this.pattern = pattern;
	}
	
	protected String[] list() throws TestIOException {
        File folder = new File(folderPath);
        return folder.list();
    }

	@Override
	public List<String> read() throws TestIOException {
		List<String> matchingFiles = new ArrayList<String>();
		String[] filenames = list();
		for (String filename : filenames) {
			if (filename.matches(pattern)) {
				matchingFiles.add(folderPath + filename);
			}
		}
		
		return matchingFiles;
	}

}

package com.automation.test.publisher.helper;

import com.automation.test.exception.TestIOException;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHelper {
	public FileHelper(){
	}
	
	public void deleteFilesWithPattern(String path, String prefixName) {
		final File folder = new File(path);
		final File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.matches(prefixName+".*");
			}
		});
		for (final File file : files) {
			if (!file.delete()) {
				System.err.println("Can't remove " + file.getAbsolutePath());
			}
		}
	}
	
	public void createNewFileWithContent(String path, String content) throws IOException {
		Files.asCharSink(new File(path), StandardCharsets.UTF_8).write(content);
	}


	public String getFileContent(String filePath){
		try {
			return FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Can not read file content: \n" + filePath,e);
		}
	}

	/**
	 * This helper to replace a value in text file with a new value
	 * @param filePath
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void writeToFile(String filePath, String key, String value) throws IOException {
		try {
			File tmpFile = File.createTempFile("buffer", ".txt");
			FileWriter fileWriter = new FileWriter(tmpFile);

			FileReader fileReader = new FileReader(new File(filePath));
			BufferedReader bufferReader = new BufferedReader(fileReader);

			String parseLine;
			while ((parseLine = bufferReader.readLine()) != null) { // Open stream byte then modify _Id_ with unique content
				String newline = parseLine.replaceAll(key, value);
				fileWriter.write(newline);
			}
			fileWriter.close();
			bufferReader.close();
			fileReader.close();

			Files.move(tmpFile, new File(filePath));

			System.out.println(String.format("Current file content is: \n %s", getFileContent(filePath)));
		}catch (Exception e){
			throw new TestIOException(String.format("Failed to write value \"%s\" to key \"%s\" in file %s", value, key, filePath));
		}
	}

	public void copyFile(String sourcePath, String destinationPath){
		try{
			Files.copy(new File(sourcePath), new File(destinationPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void modifyFile(String filePath, String oldString, String newString) {
		File fileToBeModified = new File(filePath);

		String oldContent = "";

		BufferedReader reader = null;

		FileWriter writer = null;

		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));

			//Reading all the lines of input text file into oldContent

			String line = reader.readLine();

			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();

				line = reader.readLine();
			}

			//Replacing oldString with newString in the oldContent

			String newContent = oldContent.replaceAll(oldString, newString);

			//Rewriting the input text file with newContent

			writer = new FileWriter(fileToBeModified);

			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//Closing the resources

				reader.close();

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

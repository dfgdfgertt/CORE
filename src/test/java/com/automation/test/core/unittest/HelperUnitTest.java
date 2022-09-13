package com.automation.test.core.unittest;

import com.automation.test.publisher.helper.FileHelper;
import com.automation.test.reader.PlainTextFileReader;
import com.automation.test.verifier.FileExistenceVerifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static org.testng.AssertJUnit.assertEquals;


public class HelperUnitTest {


	@Test
	public void testFileHelper() throws Exception {
		FileHelper helper = new FileHelper();
		helper.createNewFileWithContent("0.txt", "0");
		PlainTextFileReader reader = new PlainTextFileReader("0.txt");
		assertEquals(reader.read(), "0");

		helper.deleteFilesWithPattern("./", "0.txt");
		FileExistenceVerifier verifier = new FileExistenceVerifier(Paths.get("0.txt").toString(), true, 500);
		Assert.assertFalse(verifier.isOk(null));
	}
}

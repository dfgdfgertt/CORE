package com.automation.test.core.unittest;

import com.automation.test.reader.JsonExtractor;
import com.automation.test.reader.SimpleReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

public class JsonExtractorUnitTest {

	@Test
	public void testReader() throws Exception {
		JsonArray jArr = new JsonArray();
		JsonObject jObj = new JsonObject();
		jObj.addProperty("message_id", "id");
		jObj.addProperty("timestamp", "time");
		jObj.addProperty("log", "log");
		jArr.add(jObj);
		SimpleReader<JsonArray> reader = new SimpleReader<JsonArray>();
		reader.setData(jArr);
		JsonExtractor extractor = new JsonExtractor(reader, "message_id");
		assertEquals(extractor.read(), "id");
		
		//not found
		extractor = new JsonExtractor(reader, "@#@#%@#%");
		assertNull(extractor.read());
	}
	
}

package com.automation.test.core.unittest;

import com.automation.test.reader.SimpleReader;
import com.automation.test.reader.TextLogReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;


public class TextLogReaderUnitTest {

	@Test
	public void testReader() throws Exception {
		JsonArray jArr = new JsonArray();
		JsonObject jObj = new JsonObject();
		jObj.addProperty("message_id", "id");
		jObj.addProperty("timestamp", "time");
		
		JsonArray logs = new JsonArray();
		logs.add("message1");
		logs.add("message2");
		jObj.add("logs", logs);
		jArr.add(jObj);
		SimpleReader<JsonArray> reader = new SimpleReader<JsonArray>();
		reader.setData(jArr);
		TextLogReader extractor = new TextLogReader(reader);
		assertEquals(extractor.read(), "message1message2");	
	}
	
}

package com.automation.test.reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonExtractor extends AbstractDecoratorReader<JsonArray, String> {
	
	private String key;
	
	public JsonExtractor(Reader<JsonArray> parent, String key) {
		super(parent);
		this.key = key;
	}
	
	@Override
	public String read() throws Exception {
		Object preObj = parent.getData();
		JsonArray json = (JsonArray)preObj;
		for (JsonElement ele : json) {
            JsonObject jObj = (JsonObject) ele;
            if (jObj.has(key)) {
                return jObj.get(key).getAsString();
            }
        }
		
		return null;
	}

}

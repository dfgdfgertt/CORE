package com.automation.test.reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TextLogReader extends AbstractDecoratorReader<JsonArray, String> {

	public TextLogReader(Reader<JsonArray> parent) {
		super(parent);
	}
	
	@Override
	public String read() throws Exception {
		JsonArray jsonArray = parent.getData();
		String text = "";

		for (JsonElement actualEle : jsonArray) {
			JsonObject message = actualEle.getAsJsonObject();
			if (message.has("logs")) {
				JsonArray logs = message.get("logs").getAsJsonArray();
				for (JsonElement logEle : logs) {
					text += logEle.getAsString();
				}
			}
			//else continue (skip when there is no 'logs' fields)
		}
		return text;
	}

}

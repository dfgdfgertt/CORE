package com.automation.test.core.unittest;

import com.automation.test.verifier.BinaryVerifier;
import com.automation.test.verifier.JsonFormatVerifier;
import com.automation.test.verifier.JsonSizeVerifier;
import com.automation.test.verifier.TextsExistenceVerifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class DataVerifierUnitTest {

	@Test
	public void testTextExistence() {
		TextsExistenceVerifier verifier = new TextsExistenceVerifier();
		verifier.setExpected(Arrays.asList("quick", "fox", "dog"));
		Assert.assertTrue(verifier.isOk("A quick brown fox jumps over a lazy dog"));
		Assert.assertFalse(verifier.isOk("He is a quick guy"));
	}

	@Test
	public void testBinary() {
		BinaryVerifier verifier = new BinaryVerifier();
		verifier.setExpected("David".getBytes());
		Assert.assertTrue(verifier.isOk("David".getBytes()));
		Assert.assertFalse(verifier.isOk("Jimmy".getBytes()));
	}

	@Test
	public void testJson() throws Exception {
		JsonArray jArr = new JsonArray();
		JsonObject jObj = new JsonObject();
		jObj.addProperty("message_id", "id");
		jObj.addProperty("timestamp", "time");

		JsonArray logs = new JsonArray();
		logs.add("message1");
		logs.add("message2");
		jObj.add("logs", logs);
		jArr.add(jObj);
		JsonFormatVerifier verifier = new JsonFormatVerifier();
		Assert.assertTrue(verifier.isOk(jArr.toString()));
		Assert.assertTrue(verifier.isOk(
				"{\"company\":{\"name\":\"automation International, LLC\",\"products\":{\"software\":[{\"name\":\"AS2 Adapter\",\"sapCertified\":\"true\"},{\"name\":\"AWS Adapter\",\"sapCertified\":\"true\"},{\"name\":\"SFTP Adapter\",\"sapCertified\":\"true\"},\"PGP Solution\",\"PI Monitor\"]}}}"));
		Assert.assertFalse(verifier.isOk(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><name>automation International, LLC</name><products><software><sapCertified>true</sapCertified><name>AS2 Adapter</name></software><software><sapCertified>true</sapCertified><name>AWS Adapter</name></software><software><sapCertified>true</sapCertified><name>SFTP Adapter</name></software><software>PGP Solution</software><software>PI Monitor</software></products></company>"));

		JsonSizeVerifier sizeVerifier = new JsonSizeVerifier();
		sizeVerifier.setExpected(1);
		Assert.assertTrue(sizeVerifier.isOk(jArr));
	}
}

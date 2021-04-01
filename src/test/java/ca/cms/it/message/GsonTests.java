package ca.cms.it.message;

import java.lang.invoke.MethodHandles;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import ca.cms.it.domain.AuthResponse;

public class GsonTests {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Test
	public void testGson() {
		String jsonString = "{\"user\":{\"firstName\":\"Dustin\",\"lastName\":\"Zhang\",\"email\":\"dike.zhang@gmail.com\",\"id\":\"6064bdece7c0eb6a4faa7f6b\"},\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJEdXN0aW4iLCJsYXN0TmFtZSI6IlpoYW5nIiwiZW1haWwiOiJkaWtlLnpoYW5nQGdtYWlsLmNvbSIsImlkIjoiNjA2NGJkZWNlN2MwZWI2YTRmYWE3ZjZiIiwiaWF0IjoxNjE3MjE3MjY4LCJleHAiOjE2MTcyMTczNTR9.UquBSopr0t5CBoKhfJ_6Fi7IXIPkp8fqtSjwkg2nZG8\"}";
		Gson gson = new Gson();
		AuthResponse response = gson.fromJson(jsonString, AuthResponse.class);
		Assertions.assertEquals("Dustin", response.user.firstName);
		
		String userJsonString = gson.toJson(response.user);
		JSONObject request = new JSONObject();
		request.put("title", "Quick Overview of Object Spread");
		request.put("content", "The fundamental idea of the object spread operator is to create a new plain object using the own properties of an existing object. So {...obj} creates a new object with the same properties and values as obj. ");
		request.put("user", userJsonString);
		
		logger.debug("request body={}", request.toString());
		
	}
}

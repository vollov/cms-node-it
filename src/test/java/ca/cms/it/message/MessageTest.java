package ca.cms.it.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.cms.it.auth.AuthClient;
import ca.cms.it.domain.JSONResponse;
import ca.cms.it.utils.RequestHelper;
import ca.cms.it.utils.RestClient;

@SpringBootTest
public class MessageTest {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	AuthClient authClient;

	@Autowired
	RestClient restClient;

	@Value("${service.url}")
	private String serviceUrl = "service-url";

	// > login {"user":{"firstName":"Dustin","lastName":"Zhang","email":"dike.zhang@gmail.com","id":"6064bdece7c0eb6a4faa7f6b"}}
	// > submit message (title, content, user-id, )
	// > list submitted message 
	@Test
	public void submit_message() throws UnsupportedEncodingException {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);
		String userId = restClient.getUserId(authResponse);
		// submit message
		JSONObject request = new JSONObject();
		request.put("title", "Quick Overview of Object Spread");
		request.put("content", "The fundamental idea of the object spread operator is to create a new plain object using the own properties of an existing object. So {...obj} creates a new object with the same properties and values as obj. ");
		
		StringEntity payload = new StringEntity(request.toString());
		JSONResponse msgResponse = restClient.post(serviceUrl + "/messages/", headers, payload);
		logger.debug("submit message response, json={}", msgResponse.json);
		Assertions.assertEquals(200, msgResponse.httpStatus);

		DocumentContext msgContext = JsonPath.parse(msgResponse.json);
		String id = msgContext.read("$._id");
		Assertions.assertNotNull(id);
		
		// delete message

		msgResponse = restClient.delete(serviceUrl + "/messages/" + id, headers);
		msgContext = JsonPath.parse(msgResponse.json);
		logger.debug("delete message response, json={}", msgResponse.json);
		Assertions.assertEquals(200, msgResponse.httpStatus);
		// TODO: verify response.body = write result
		int result = msgContext.read("$.ok");
		Assertions.assertEquals(1, result);
		
		// list message
		
		msgResponse = restClient.get(serviceUrl + "/messages/user/"+userId, headers);
		msgContext = JsonPath.parse(msgResponse.json);
		logger.debug("get message response, json={}", msgResponse.json);
		Assertions.assertEquals(200, msgResponse.httpStatus);
		// TODO: verify response.body = write result
//		int result = msgContext.read("$.ok");
//		Assertions.assertEquals(1, result);
	}
	
	@Test
	public void get_messages() {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);
		String userId = restClient.getUserId(authResponse);
		
		JSONResponse msgResponse = restClient.get(serviceUrl + "/messages/user/"+userId, headers);
		DocumentContext msgContext = JsonPath.parse(msgResponse.json);
		logger.debug("get message response, json={}", msgResponse.json);
		Assertions.assertEquals(200, msgResponse.httpStatus);
		
	}
	
	@Test
	public void send_message() throws UnsupportedEncodingException {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);
		String userId = restClient.getUserId(authResponse);
		// submit message
		JSONObject request = new JSONObject();
		request.put("title", "Quick Overview of Object Spread");
		request.put("content", "The fundamental idea of the object spread operator is to create a new plain object using the own properties of an existing object. So {...obj} creates a new object with the same properties and values as obj. ");
		
		StringEntity payload = new StringEntity(request.toString());
		JSONResponse msgResponse = restClient.post(serviceUrl + "/messages/", headers, payload);
		logger.debug("submit message response, json={}", msgResponse.json);
		Assertions.assertEquals(200, msgResponse.httpStatus);
	}
	
}

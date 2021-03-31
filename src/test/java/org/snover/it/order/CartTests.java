package org.snover.it.order;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
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
import ca.cms.it.utils.RestClient;

/**
 * Test a customer order process. Empty the dbcart 
 * @author Dustin Zhang (2021-03-31)
 *
 */
@SpringBootTest
public class CartTests {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	AuthClient authClient;

	@Autowired
	RestClient restClient;

	@Value("${service.url}")
	private String serviceUrl = "service-url";
	
	// > read empty order
	// > place order
	// > read order and verify
	// > update order
	// > read order
	// > delete order
	// > read empty order
	
	@Test
	public void purchase_process() throws UnsupportedEncodingException {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);

		// read empty cart
		JSONResponse cartResponse = restClient.get(serviceUrl + "/orders/cart", headers);
		logger.debug("query empty cart response, json={}", cartResponse.json);
		Assertions.assertEquals(200, cartResponse.httpStatus);
		Assertions.assertEquals("{}",cartResponse.json);
		
		// add cart item
		JSONArray request = new JSONArray();
		JSONObject bat = new JSONObject();
		bat.put("sku", "BAT0001");
		bat.put("quantity", 2);
		request.add(bat);
		
		StringEntity payload = new StringEntity(request.toString());
		JSONResponse addResponse = restClient.post(serviceUrl + "/orders/cart", headers, payload);
		DocumentContext addContext = JsonPath.parse(addResponse.json);
		logger.debug("add cart item response, json={}", addResponse.json);
		Assertions.assertEquals(200, addResponse.httpStatus);
		// TODO: verify response.body = write result
		int result = addContext.read("$.ok");
		Assertions.assertEquals(1, result);
		
		// read cart item after add
		cartResponse = restClient.get(serviceUrl + "/orders/cart", headers);
		DocumentContext cartContext = JsonPath.parse(cartResponse.json);
		logger.debug("after add query cart response, json={}", cartResponse.json);
		Assertions.assertEquals(200, cartResponse.httpStatus);
		int quantity = cartContext.read("$.BAT0001.quantity");
		Assertions.assertEquals(2, quantity);
		
		// delete cart items
		request = new JSONArray();
		request.add("BAT0001");
		payload = new StringEntity(request.toString());
		cartResponse = restClient.post(serviceUrl + "/orders/cart/delete", headers, payload);
		cartContext = JsonPath.parse(cartResponse.json);
		logger.debug("delete cart item response, json={}", cartResponse.json);
		Assertions.assertEquals(200, cartResponse.httpStatus);
		// TODO: verify response.body = write result
		result = cartContext.read("$.ok");
		Assertions.assertEquals(1, result);
		
		// read cart item after delete
		cartResponse = restClient.get(serviceUrl + "/orders/cart", headers);
		logger.debug("after delete query cart response, json={}", cartResponse.json);
		Assertions.assertEquals(200, cartResponse.httpStatus);
		Assertions.assertEquals("{}",cartResponse.json);
	}
	
	
}

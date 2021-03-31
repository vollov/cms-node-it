package org.snover.it.order;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class OrderTests {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	AuthClient authClient;

	@Autowired
	RestClient restClient;

	@Value("${service.url}")
	private String serviceUrl = "service-url";

	//TODO: 
	// > delete item from an empty cart

	
//	@BeforeAll
//    public static void setup() {
//		JSONResponse authResponse = authClient.login();
//		token = restClient.getToken(authResponse);
//    }
	
	@Test
	@Order(3)
	public void get_cart_orders() {

		JSONResponse authResponse = authClient.login();
		
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);

		JSONResponse cartResponse = restClient.get(serviceUrl + "/orders/cart", headers);
		DocumentContext cartContext = JsonPath.parse(cartResponse.json);
		logger.debug("get_cart_orders response, json={}", cartResponse.json);
		Assertions.assertEquals(200, cartResponse.httpStatus);

		int quantity = cartContext.read("$.BAT0001.quantity");
		Assertions.assertNotNull(quantity);

	}

	
	@Test
	@Order(2)
	public void update_items() {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);

		JSONArray request = new JSONArray();
		JSONObject bat = new JSONObject();
		bat.put("quantity", 4);
		request.add(bat);
		logger.debug("update_items request, json string={}", request);
		try {
			StringEntity payload = new StringEntity(request.toString());
			JSONResponse cartResponse = restClient.put(serviceUrl + "/orders/cart/BAT0001", headers, payload);
			DocumentContext cartContext = JsonPath.parse(cartResponse.json);
			logger.debug("update_items response, json={}", cartResponse.json);
			Assertions.assertEquals(200, cartResponse.httpStatus);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//
//    @Test
//    fun delete_item(){
//        //MOT0001
//        val response: JSONResponse = authHelper.login()
//        logger.debug("login_success response, json={}", response.json)
//
//        val jsonContext: DocumentContext = JsonPath.parse(response.json)
//        val token: String = jsonContext.read("$.token")
//
//        var request = JSONArray()
//        var bat = JSONObject()
//        bat.put("sku", "MOT0001")
//        bat.put("quantity", 2)
//        request.add(bat)
//        logger.debug("add cart item request, json string=$request")
//        val payload = StringEntity(request.toString())
//
//        var headers  = restClient.getHeaders(token)
//        val cartResponse: JSONResponse = restClient.post("$serviceUrl/orders/cart", headers, payload)
//
//        logger.debug("add cart item, response, json=$cartResponse.json")
//        Assertions.assertEquals(200, cartResponse.httpStatus)
//
//        var delRequest = JSONArray()
//        delRequest.add("MOT0001")
//        logger.debug("delete item in cart request, json string=$delRequest")
//        val delPayload = StringEntity(delRequest.toString())
//        val deleteResponse: JSONResponse = restClient.post("$serviceUrl/orders/cart/delete", headers, delPayload)
//        logger.debug("delete cart item, response, json=$deleteResponse.json")
//        Assertions.assertEquals(200, deleteResponse.httpStatus)
//    }
//
	
	// POST /orders/cart/delete
	@Test
	public void remove_cart_item() {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);

		JSONArray request = new JSONArray();
		request.add("BAT0001");

		logger.debug("remove cart item request, json string={}", request);
		try {
			StringEntity payload = new StringEntity(request.toString());
			JSONResponse cartResponse = restClient.post(serviceUrl + "/orders/cart/delete", headers, payload);
			DocumentContext cartContext = JsonPath.parse(cartResponse.json);
			logger.debug("place_order response, json={}", cartResponse.json);
			Assertions.assertEquals(200, cartResponse.httpStatus);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(1)
	public void place_order() {
		JSONResponse authResponse = authClient.login();
		String token = restClient.getToken(authResponse);
		List<Header> headers = restClient.getHeaders(token);

		JSONArray request = new JSONArray();
		JSONObject bat = new JSONObject();
		bat.put("sku", "BAT0001");
		bat.put("quantity", 2);
		request.add(bat);
		logger.debug("place_order request, json string={}", request);
		try {
			StringEntity payload = new StringEntity(request.toString());
			JSONResponse cartResponse = restClient.post(serviceUrl + "/orders/cart", headers, payload);
			DocumentContext cartContext = JsonPath.parse(cartResponse.json);
			logger.debug("place_order response, json={}", cartResponse.json);
			Assertions.assertEquals(200, cartResponse.httpStatus);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

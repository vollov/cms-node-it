package org.snover.it.product;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.cms.it.domain.JSONResponse;
import ca.cms.it.utils.RequestHelper;
import ca.cms.it.utils.RestClient;

@SpringBootTest
public class ProductTests {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired 
	RequestHelper requestHelper;
	
    @Autowired 
    RestClient restClient;

	@Value("${service.url}")
	private String serviceUrl = "service-url";
	
    /**
     * read product don't need authentication
     */
    @Test
    public void find_by_sku_success() {
//    	JSONResponse authResponse = authClient.login();
//    	String token = restClient.getToken(authResponse);
    
    	List<Header> headers = requestHelper.getHeaders();
    	
    	JSONResponse productsResponse = restClient.get(serviceUrl+"/products/BAT0001", headers);
		DocumentContext productContext= JsonPath.parse(productsResponse.json);
		logger.debug("find_by_sku_success response, json={}", productsResponse.json);
		Assertions.assertEquals(200, productsResponse.httpStatus);

		String imageUrl = productContext.read("$.imageUrl");
        Double price = productContext.read("$.price");
        Assertions.assertNotNull(imageUrl);
        Assertions.assertNotNull(price);
	}

    @Test
    public void list_success() {
    	
//    	JSONResponse authResponse = authClient.login();
//    	String token = restClient.getToken(authResponse);
        
    	List<Header> headers = requestHelper.getHeaders();
    	
    	JSONResponse productsResponse = restClient.get(serviceUrl+"/products", headers);
		DocumentContext productContext= JsonPath.parse(productsResponse.json);
		logger.debug("list product success response, json={}", productsResponse.json);
		Assertions.assertEquals(200, productsResponse.httpStatus);

		String imageUrl = productContext.read("$.[0]imageUrl");;
        Assertions.assertNotNull(imageUrl);

    }
}

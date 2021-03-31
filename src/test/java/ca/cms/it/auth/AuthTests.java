package ca.cms.it.auth;

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.cms.it.auth.AuthClient;
import ca.cms.it.domain.JSONResponse;

@SpringBootTest
public class AuthTests{

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	AuthClient authClient;
	
	/**
	 * return a user object and jwt token after login
	 */
    @Test
    public void login_success(){
        JSONResponse response = authClient.login();
        logger.debug("login_success response, json={}", response.json);
        Assertions.assertEquals(200, response.httpStatus);
        
        DocumentContext jsonContext = JsonPath.parse(response.json);
        String token = jsonContext.read("$.token");
        String email = jsonContext.read("$.user.email");
        Assertions.assertEquals("dike.zhang@gmail.com", email);
        Assertions.assertNotNull(token);
    }
}
package ca.cms.it.auth;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.cms.it.domain.JSONResponse;
import ca.cms.it.utils.RequestHelper;
import ca.cms.it.utils.RestClient;
import net.minidev.json.JSONObject;

/**
 * Mock a client login
 * @author Dustin Zhang (2021-03-30)
 *
 */
@Component
public class MockAuthClient implements AuthClient {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Value("${service.url}")
	private String serviceUrl = "service-url";

	@Value("${auth.firstName}")
	private String firstName = "Gerry";

	@Value("${auth.lastName}")
	private String lastName = "Chalkley";

	@Value("${auth.email}")
	private String email = "gc@abc.com";
	
	@Autowired
	RestClient restClient;
    @Autowired 
    RequestHelper httpClient;
    
	/**
	 * login response don't have token in the header 
	 */
	@Override
	public JSONResponse login() {
		List<Header> headers = httpClient.getHeaders();

		JSONObject request = new JSONObject();
	    request.put("firstName", firstName);
	    request.put("lastName", lastName);
	    request.put("email", email);
	    StringEntity payload;
		try {
			payload = new StringEntity(request.toString());
			logger.debug("calling login() payload={}, url={}", payload.toString(), serviceUrl + "/auth/login");

			JSONResponse response =  restClient.post(serviceUrl + "/auth/login", headers, payload);
			logger.debug("login() return response = {}", response.json);
			return response;
		} catch (UnsupportedEncodingException e) {
			logger.error("login request failed, error={}", e.getLocalizedMessage());
			return null;
		}
	    
	}

}

package ca.cms.it.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.cms.it.domain.Constants;
import ca.cms.it.domain.JSONResponse;

@Component
public class RestClient {
	@Autowired
	public RequestHelper client;

	/**
	 * fetch token from json body
	 * @return
	 */
	public String getToken(JSONResponse response) {
		DocumentContext jsonContext = JsonPath.parse(response.json);
        String token = jsonContext.read("$.token");
        return token;
	}
	
	public String getUserId(JSONResponse response) {
		DocumentContext jsonContext = JsonPath.parse(response.json);
        String id = jsonContext.read("$.user.id");
        return id;
	}
	
	public List<Header> getHeaders(String token) {
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Content-Type", Constants.APPLICATION_JSON));
		headers.add(new BasicHeader("Authorization", "Bearer " + token));
		return headers;
	}

	public JSONResponse get(String url, List<Header> list) {
		Header[] headers = list.toArray(new Header[0]);
		HttpResponse response = client.get(url, headers);
		return client.convertReponse(response);
	}

	public JSONResponse delete(String url, List<Header> list) {
		Header[] headers = list.toArray(new Header[0]);
		HttpResponse response = client.delete(url, headers);
		return client.convertReponse(response);
	}

	public JSONResponse put(String url, List<Header> list, StringEntity payload) {
		Header[] headers = list.toArray(new Header[0]);
		HttpResponse response = client.put(url, headers, payload);
		return client.convertReponse(response);
	}

	public JSONResponse post(String url, List<Header> list, StringEntity payload) {
		Header[] headers = list.toArray(new Header[0]);
		HttpResponse response = client.post(url, headers, payload);
		return client.convertReponse(response);
	}

}

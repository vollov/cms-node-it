package ca.cms.it.utils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import ca.cms.it.domain.Constants;
import ca.cms.it.domain.ErrorCodes;
import ca.cms.it.domain.JSONResponse;

/**
 * 
 * @author Dustin Zhang
 *
 * 2021-03-29
 * 
 * Handle apache HttpResponse object
 */
@Component
public class RequestHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired 
	CloseableHttpClient client;

	public List<Header> getHeaders() {
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Content-Type", Constants.APPLICATION_JSON));
		return headers;
	}
	
	/**
	 * fetch token from response and put it in next request header
	 */
	public List<Header> nextHeader(JSONResponse response){
		List<Header> headers = getHeaders();
        if(response.token == null){
            return headers;
        } else {
            headers.add(new BasicHeader("Authorization", "Bearer " + response.token));
            return headers;
        }
    }
    
	public JSONResponse convertReponse(HttpResponse response){
		// new token is for service response which have renewed token in the http header 
		// String newToken = response.getFirstHeader("Authorization").getValue();

		try {
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			int statusCode = response.getStatusLine().getStatusCode();
	        return new JSONResponse(json, statusCode);
		} catch (ParseException | IOException e) {
			logger.error("convert http response to json failed, error={}", e.getLocalizedMessage());
			// e.printStackTrace();
			return null;
		} 
        
    }
	
    public HttpResponse postForm(String url, Header[] headers, UrlEncodedFormEntity payload) {
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeaders(headers);
        httpPost.setEntity(payload);
        try {
          
            return client.execute(httpPost);
        } catch (IOException e){
            logger.error("postForm request failed, error={}", e.getLocalizedMessage());
            throw new RestClientException(ErrorCodes.REST_REQUEST_ERROR);
        } finally{
            // DO NOT close socket!!
            //client.close()
        }
    }

    public HttpResponse post(String url, Header[] headers, StringEntity payload) {
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeaders(headers);
        httpPost.setEntity(payload);
        try {
          
            return client.execute(httpPost);
        } catch (IOException e){
            logger.error("post request failed, error={}", e.getLocalizedMessage());
            throw new RestClientException(ErrorCodes.REST_REQUEST_ERROR);
        } finally{
            // DO NOT close socket!!
            //client.close()
        }
    }

    public HttpResponse put(String url, Header[] headers, StringEntity payload) {
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);

        httpPut.setHeaders(headers);
        httpPut.setEntity(payload);
        try {
          
            return client.execute(httpPut);
        } catch (IOException e){
            logger.error("put request failed, error={}", e.getLocalizedMessage());
            throw new RestClientException(ErrorCodes.REST_REQUEST_ERROR);
        } finally{
            // DO NOT close socket!!
            //client.close()
        }
    }


    public HttpResponse get(String url, Header[] headers) {
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(headers);

        try {
            return client.execute(httpGet);
        } catch (IOException e){
            logger.error("get request failed, error={}", e.getLocalizedMessage());
            throw new RestClientException(ErrorCodes.REST_REQUEST_ERROR);
        } finally{
            // DO NOT close socket!!
            //client.close()
        }
    }
    
    public HttpResponse delete(String url, Header[] headers) {
    	
        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeaders(headers);

        try {
          
            return client.execute(httpDelete);
        } catch (IOException e){
            logger.error("delete request failed, error={}", e.getLocalizedMessage());
            throw new RestClientException(ErrorCodes.REST_REQUEST_ERROR);
        } finally{
            // DO NOT close socket!!
            //client.close()
        }
    }
}

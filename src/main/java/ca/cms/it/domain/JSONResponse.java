package ca.cms.it.domain;

/**
 * 
 * @author Dustin Zhang 2021-03-29
 *
 */
public class JSONResponse {

	public String json;
	public int httpStatus;
	public String token;

	public JSONResponse(String json, int httpStatus) {
		super();
		this.json = json;
		this.httpStatus = httpStatus;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}

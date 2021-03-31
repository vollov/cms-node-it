package ca.cms.it.auth;

import ca.cms.it.domain.JSONResponse;

/**
 * 
 * @author Dustin Zhang (2021-03-30)
 *
 */
public interface AuthClient {
	public JSONResponse login();
	
}

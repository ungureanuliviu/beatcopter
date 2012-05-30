package com.liviu.apps.beatcopter.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.liviu.apps.beatcopter.utils.Console;

import android.content.Context;

public class BaseAPI {
	
	// Constants 
	private final String TAG = "MAIN_API";
	protected final String API_URL = "http://wp8.medworldhub.com/API";
	public static final String SESSION_TYPE_MOBILE = "MOBILE";
	
	// API messages
	/* the auth_token which was sent to app is not valid: it can be expired we should try to get another one from Google servers */
	public static final String MSG_INVALID_AUTH_TOKEN = "INVALID_AUTH_TOKEN"; 	
	
	/* the device was found after the user make an call to start a new session */
	public static final String MSG_DEVICE_OK = "DEVICE_OK";
	
	/* The device cannot be added at the moment */
	public static final String MSG_DEVICE_NOT_ADDED = "DEVICE_NOT_ADDED";
	
	/* A new device was added when the user start a session */
	public static final String MSG_DEVICE_ADDED = "DEVICE_ADDED";		
	
	/* This message will be set when the app_key or email address cannot be found on system.
	 * In this case, the application should re-authenticate the user.
	 */
	public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
	
	
	// Data
	private HttpClient client; 	
	private ClientConnectionManager	cm;
    private HttpPost post;	  
	private static HttpContext httpContext;
	private	HttpParams params;
	private User user;
	private String mLastAPIMessage;
	private Context mContext;
	 
	public BaseAPI(Context pContext){
		
		params 	= new BasicHttpParams();
		mContext = pContext;
        user = User.getInstance(mContext);       
        mLastAPIMessage = "";

        // Create local HTTP context
        if(null == httpContext){
	        CookieStore cookieStore = new BasicCookieStore();	        
	        httpContext = new BasicHttpContext();
	        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }        
        
        ConnManagerParams.setMaxTotalConnections(params, 300);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);                          
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80) );
        
        cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        client = new DefaultHttpClient(cm, params);               
	}
		
	/**
	 * Make a post call to give url
	 * @param url			- API end-point URL
	 * @param jsonParams	- params to pass
	 * @return a {@link JSONResponse} with server's response
	 */
	protected synchronized JSONResponse doRequest(String url, JSONObject jsonParams){		
	    try {	       
	        post = new HttpPost(url);       
	        
	        // prepare parameters
	        JSONObject params = null;  
	        if(null == jsonParams){  
	        	params = new JSONObject();
	        } else {
	        	params = jsonParams;
	        }
	        
	        if( !url.contains( "/session/start/" )){
		        if(user.getAppKey() != null){
		        	params.put("app_key", user.getAppKey());
		        } else {
		        	mLastAPIMessage = INVALID_CREDENTIALS;
		        	return new JSONResponse();
		        }
		        
		        if(user.getAuthName() != null){
		        	params.put("authname", user.getAuthName());
		        } else {
		        	mLastAPIMessage = INVALID_CREDENTIALS;
		        	return new JSONResponse();
		        }
	        }
	        
	        params.put("type", SESSION_TYPE_MOBILE);
	        
	        Console.debug(TAG, "doRequest: " + url + " params: " + (null != params ? params.toString().replaceAll(",", "\n") : null), Console.Liviu);
	        
            StringEntity en = new StringEntity(params.toString());	            
	        post.setEntity(en);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");	    
	        
            HttpResponse responsePOST = client.execute(post, httpContext);  
            HttpEntity resEntity = responsePOST.getEntity();
            String apiResponse = EntityUtils.toString(resEntity);           
			
            Console.debug(TAG, "api response: " + apiResponse, Console.Liviu);
            
            JSONResponse response = new JSONResponse(apiResponse);
            if(!response.isSuccess()){
            	mLastAPIMessage = response.getString("message");
            }
	        return response;
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();	    	
	    } catch (IOException e) {
	    	e.printStackTrace();	    	
	    } catch (JSONException e) {
			e.printStackTrace();			
		}
	    
	    return new JSONResponse();
	}		
	
	/**
	 * Create a new user
	 * @param pAppKey	: The app_key generated by server and received in /session/start/
	 * @param pEmail	: user's email
	 * @return 	:if success - an {@link User} object with all details (including {@link User#ID} <br />
	 * 		 	:if fails - null
	 */
	public synchronized User startSession(String pEmail, String pAuthToken){				
		try{
			JSONObject params = new JSONObject();			
			params.put("type", SESSION_TYPE_MOBILE);
			params.put("auth_token", pAuthToken);
			params.put("authname", pEmail);
			params.put("device_id", user.getDeviceDetails().getDeviceId());
			params.put("device_name", user.getDeviceDetails().getDeviceName());
			
			JSONResponse jsonResponse = doRequest(API_URL + "/session/start/", params);
			if(jsonResponse.isSuccess()){			
				User tempUser = User.getInstance(mContext);				
				  
				JSONObject jUser = jsonResponse.getJSONObject("user");
				tempUser.setAuthName(jUser.getString("authname"))
						.setEmail(jUser.getString("email"))
						.setAppKey(jsonResponse.getString("app_key"))
						.setId(jUser.getLong("id"))
						.setName(jUser.getString("name"))
						.setAuthToken(pAuthToken)
						.commitChanges();
				return tempUser;
			}else{
				Console.error(TAG, "user not created.", Console.Liviu);								
			}			
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}		
	
	public String getLastErrorMessage(){
		return mLastAPIMessage;
	}
	
    /**
	    URL http://wp8.medworldhub.com/API/user/update_device_push_id/
	    @method POST
	    @param user_id                 - user's id
	    @param device_id               - the current id of device
	    @param new_device_push_id      - the new id of device                                        
	    @return
	        if success - {"is_success":1,"update":{"user_id":"1","device_id":"00000000-5ce1-6efb-ffff-ffff8ff061a3","message":"DEVICE_ID_UPDATED"}}
	        if fails   - {"is_success":0,"update":{"message":"DEVICE_ID_NOT_UPDATED"}}
	                   - if the new_device_id == device id the response will be the same as the one for failure because the mysql-ul have nothing to update.
	        if count(user's device) == 0 - {"is_success":0,"update":{"message":"NO_DEVICE_TO_UPDATE_TRY_TO_ADD_THIS_DEVICE"}}                                
	*/ 
	public void updateDevicePushId(String pRegistrationId) {
		if( user.isLoggedIn()){
			try{
				JSONObject jsonParams = new JSONObject();
				jsonParams.put("device_id", user.getDeviceDetails().getDeviceId());
				jsonParams.put("new_device_push_id", pRegistrationId);
				jsonParams.put("user_id", user.getId());
				doRequest(API_URL + "/user/update_device_push_id/", jsonParams);
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}		
}

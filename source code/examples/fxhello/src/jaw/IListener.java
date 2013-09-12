/* This class is exported to 3rd parties application and represents the list
 * of calls that an app is allowed to make to the browser 
 * 
 */

package jaw;

import java.util.LinkedHashMap;


public interface IListener {
	
	// Sent HTTP GET request
	public String httpGet(String url, LinkedHashMap<String, String> query);
	
	/**
	 * Send HTTP POST request
	 * 
	 * @param url
	 * @param data		parameters as (name:String, value:String) pairs
	 */
	public String httpPost(String url, LinkedHashMap<String, String> query, LinkedHashMap<String, String> data);

	// Take a String representation of a XML file and return a org.w3c.dom.Document
	public org.w3c.dom.Document stringToXmlDocument(String xml);
	
	// Take a String representation of a JSON file and return a JSONObject
	//public JSONObject stringToJson(String json);
	
	// Set the title for the window
	public void setTitle(String title);
	
	// Store a (key, value) pair on local storage
	public boolean store(String key, Object value);
	
	// Get value of key from local storage
	public Object retrieve(String key);
}

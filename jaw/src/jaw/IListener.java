/* This class is exported to 3rd parties application and represents the list
 * of calls that an app is allowed to make to the browser 
 * 
 */

package jaw;

import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;


public interface IListener {
	
	// Sent HTTP GET request
	public String httpGet(String url);
	
	/**
	 * Send HTTP POST request
	 * 
	 * @param url
	 * @param data		parameters as (name:String, value:String) pairs
	 */
	public String httpPost(String url, LinkedHashMap<String, String> data);
	
	// Set the title for the window
	public void setTitle(String title);
}

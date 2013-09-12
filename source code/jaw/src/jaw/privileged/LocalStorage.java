/* This class manages the browser local storage.
 * 
 * A LocalStorage is a set of (key, value) pairs that each app is allowed to store
 * on the client file system. The maximum size available for each app is 10MB
 */

package jaw.privileged;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import jaw.Commons;

public class LocalStorage implements Callable<Object> {
	
	/***** STATIC *****/
	
	
	protected static enum Action { STORE, RETRIEVE };
	
	// All data is stored here
	protected static final String PATH = "ls/";
	
	// File where data is actually stored in
	protected static final String FILE_NAME = "data";
	
	// Max LocalStorage size
	protected static final int MAX_JSON_SIZE = 10485760; // 10MB
	
	// Get the directory where the data for this app (= URL authority) is stored
	private static String getAppHome(URL url) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(url.getAuthority().getBytes("UTF-8"));
			
			return LocalStorage.PATH + new String(Commons.bytesToHex(md.digest())).replaceAll("(..)", "$1/");
	    
		} catch (Exception e) {
			return null;
		}
	}
	
	// Create the directory where to store data for this app (= URL authority) is stored
	public static String createAppHome(URL url) {
		try {
			
			String home = LocalStorage.getAppHome(url);
			
			if (home == null) return null;
			
			return Commons.createDirectory(home) ? home : null;
	    
		} catch (Exception e) {
			return null;
		}
	}
	
	// Read the data file and parse it into a JSON object
	protected static JSONObject loadJson(URL appUrl) {
		try {
			return	(JSONObject) new JSONParser()
								.parse(
									new String(
										Files.readAllBytes(Paths.get(LocalStorage.getAppHome(appUrl) + LocalStorage.FILE_NAME)),
										"UTF-8"
									)
								);
			
    } catch (Exception e) {
    	return new JSONObject();
    }
	}
	
	// Take a JSON object, serialize it and store it into file
	protected static boolean writeJson(URL appUrl, JSONObject json) {
		try {
			
			PrintWriter writer = new PrintWriter(LocalStorage.createAppHome(appUrl) + LocalStorage.FILE_NAME, "UTF-8");
			
			// Serialize the JSON object for storage
			String serialized = json.toJSONString();
			
			// Is the file gonna exceed the allowed limits?
			boolean validSize = serialized.length() <= LocalStorage.MAX_JSON_SIZE;
			
			if (validSize)
				writer.print(serialized);
			
			writer.close();
			
			return validSize;
    } catch (Exception e) {
    	e.printStackTrace();
    	return false;
    }
	}
	
	// Retrieve a value
	public static Object get(URL appUrl, String key) {
		return LocalStorage.loadJson(appUrl).get(key);
	}
	
	// Store a value
	public static boolean set(URL appUrl, String key, Object value) {
		try {
			JSONObject json = LocalStorage.loadJson(appUrl);
			json.put(key, value);
			
			return LocalStorage.writeJson(appUrl, json);
					
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isOk() {
		return Commons.createDirectory(LocalStorage.PATH);
	}
	
	
	/***** INSTANCE *****/
	
	protected LocalStorage.Action myAction;
	protected URL appUrl;
	protected String key;
	protected Object value;
	
	// Constructor for retrieving a value
	public LocalStorage(URL appUrl, String key) {
		this.myAction	= LocalStorage.Action.RETRIEVE;
		this.appUrl		= appUrl;
		this.key			= key;
		this.value		= null;
	}
	
	// Constructor for storing a value
	public LocalStorage(URL appUrl, String key, Object value) {
		this.myAction	= LocalStorage.Action.STORE;
		this.appUrl		= appUrl;
		this.key			= key;
		this.value		= value;
	}

	@Override
	public Object call() throws Exception {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		
		Object result = null;
		
		try {
			
			// Return the "value" for "key"
			if (this.myAction == LocalStorage.Action.RETRIEVE)
				result = LocalStorage.get(this.appUrl, this.key);
			
			// Return boolean if write was successful
			if (this.myAction == LocalStorage.Action.STORE)
				result = LocalStorage.set(this.appUrl, this.key, this.value);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
		return result;
	}
}

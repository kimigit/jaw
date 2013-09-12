/* Accept jaw.Listener requests from the app to convert a string to a DOM object.
 * 
 */

package jaw.privileged;

import java.util.concurrent.Callable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jaw.Commons;

public class StringToJson implements Callable<JSONObject> {

	protected String myJson;
	
	public StringToJson(String json) {
		this.myJson = json;
	}
	
	@Override
	public JSONObject call() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		JSONObject result = null;
		
		try {
			
			result = (JSONObject) new JSONParser().parse(this.myJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
		return result;
	}
	
	
	
}

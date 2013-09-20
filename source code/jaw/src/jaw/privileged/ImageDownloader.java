/* Accept jaw.Listener requests from the app to download a picture from the web.
 * 
 */

package jaw.privileged;

import java.util.concurrent.Callable;

import javafx.scene.image.Image;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jaw.Commons;

public class ImageDownloader implements Callable<Image> {

	protected String myUrl;
	
	public ImageDownloader(String url) {
		this.myUrl = url;
	}
	
	@Override
	public Image call() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		Image newImage = null;
		
		try {
			
			newImage = new Image(this.myUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
		return newImage;
	}
	
	
	
}

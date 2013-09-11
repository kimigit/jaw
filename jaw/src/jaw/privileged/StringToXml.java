/* Accept jaw.Listener requests from the app to convert a string to a DOM object.
 * 
 */

package jaw.privileged;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

import org.w3c.dom.Document;

import jaw.Commons;
import jaw.privileged.UrlFetcher.Method;

public class StringToXml implements Callable<org.w3c.dom.Document> {

	protected String myXml;
	protected Document myDocument;
	
	public StringToXml(String xml) {
		this.myXml = xml;
		this.myDocument = null;
	}
	
	@Override
	public Document call() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		String result = null;
		
		try {
			
			this.myDocument = Commons.stringToXmlDocument(this.myXml);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
		return this.myDocument;
	}
	
	
	
}

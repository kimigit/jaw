/* Download a file from HTTP and store it on file
 * 
 */

package jaw.privileged;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;

import jaw.Commons;

public class UrlDownloader implements Callable<String> {
	
	protected URL myUrl;							// File to download
	protected String myDestination;		// File to store the download into
	
	public UrlDownloader(String url) throws MalformedURLException {
		this.myUrl = new URL(Commons.normalizeUrl(url));
	}

	@Override
	public String call() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		
		String home = Cache.createAppHome(this.myUrl);
		
		if (home == null) return null;
		
		this.myDestination = home + "app";

		try {
			ReadableByteChannel rbc = Channels.newChannel(this.myUrl.openStream());
	    FileOutputStream fos = new FileOutputStream(this.myDestination);
	    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	    fos.close();
	    
	    return this.myDestination;
		} catch (Exception e) {
			return null;
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
	}
}
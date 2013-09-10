/* Download a file from HTTP and store it on file
 * 
 */

package jaw.privileged;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import jaw.Commons;

public class UrlDownloader implements Runnable {
	
	protected URL myUrl;							// File to download
	protected String myDestination;		// File to store the download into 
	protected String myAppHash;
	
	public UrlDownloader(String url, String appHash) throws MalformedURLException {
		this.myUrl = new URL(Commons.normalizeUrl(url));
		this.myAppHash = appHash;
	}

	@Override
	public void run() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		
		String home = Cache.createAppHome(this.myUrl);
		
		if (home == null) return;
		
		this.myDestination = home + "app";
		
		Commons.appHash.put(this.myAppHash, this.myDestination);

		try {
			ReadableByteChannel rbc = Channels.newChannel(this.myUrl.openStream());
	    FileOutputStream fos = new FileOutputStream(this.myDestination);
	    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	    fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
	}
}
/* This is the listener class implementing the interface jaw.IListener for
 * app-browser communication.
 */

package jaw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Listener implements IListener {

	private Site mySite;
	
	public Listener(Site aSite) {
		this.mySite = aSite;
	}
	
	@Override
	public String httpGet(String url) {
		Changeable<String> result = new Changeable<String>("");
		
		try {
			
			Thread t = new Thread(new jaw.privileged.UrlFetcher(url, result));
			t.start();
			t.join();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result.toString();
		}
	}

	@Override
	public String httpPost(String url, LinkedHashMap<String, String> data) {
		Changeable<String> result = new Changeable<String>("");
		
		try {
			
			Thread t = new Thread(new jaw.privileged.UrlFetcher(url, data, result));
			t.start();
			t.join();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result.toString();
		}
	}

	@Override
	public void setTitle(String title) {
		this.mySite.setTitle(title);
	}
	
}

/* Download a GET/POST HTTP request and return result as String
 * 
 */

package jaw.privileged;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import jaw.Changeable;
import jaw.Commons;

public class UrlFetcher  implements Runnable {
	
	protected enum Method { GET, POST };
	protected static final String USER_AGENT = "JAW";
	
	protected URL myUrl;
	protected LinkedHashMap<String, String> myPostData;
	protected Changeable<String> myResult;
	protected Method myMethod;
	
	// Constructor for a GET request
	public UrlFetcher(String url, Changeable<String> result) {
		try {
			this.myUrl = new URL(Commons.normalizeUrl(url));
			this.myResult = result;
			this.myMethod = Method.GET;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Constructor for a POST request
	public UrlFetcher(String url, LinkedHashMap<String, String> data, Changeable<String> result) {
		try {
			this.myUrl = new URL(Commons.normalizeUrl(url));
			this.myPostData = data;
			this.myResult = result;
			this.myMethod = Method.POST;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void get() throws Exception {
		HttpURLConnection con = (HttpURLConnection) this.myUrl.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", UrlFetcher.USER_AGENT);
		con.setRequestProperty("charset", "UTF-8");
 
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		
		in.close();
		
		this.myResult.changeTo(response.toString());
	}
	
	protected void post() throws Exception {
 
		HttpURLConnection con = (HttpURLConnection) this.myUrl.openConnection();
 
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", UrlFetcher.USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=1");
		con.setRequestProperty("charset", "UTF-8");
 
		String data = "";
		
		for (Map.Entry<String, String> someData : this.myPostData.entrySet())
			data += URLEncoder.encode(someData.getKey(), "UTF-8") + "&" + URLEncoder.encode(someData.getValue(), "UTF-8");
 
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		
		in.close();

		this.myResult.changeTo(response.toString());
	}
	
	@Override
	public void run() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);

		try {
			
			if (this.myMethod == Method.GET) this.get();
			if (this.myMethod == Method.POST) this.post();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
	}
}

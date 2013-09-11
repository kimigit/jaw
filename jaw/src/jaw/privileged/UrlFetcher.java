/* Download a GET/POST HTTP request and return result as String
 * 
 */

package jaw.privileged;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import jaw.Commons;

public class UrlFetcher implements Callable<String> {
	
	protected enum Method { GET, POST };
	protected static final String USER_AGENT = "JAW";
	
	protected String myUrl;
	protected LinkedHashMap<String, String> myUrlQuery;
	protected LinkedHashMap<String, String> myPostData;
	protected Method myMethod;
	
	// Constructor for a GET request
	public UrlFetcher(String url, LinkedHashMap<String, String> query) {
		try {
			this.myUrl = url;
			this.myUrlQuery = query;
			this.myMethod = Method.GET;
			
			this.buildUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Constructor for a POST request
	public UrlFetcher(String url, LinkedHashMap<String, String> query, LinkedHashMap<String, String> data) {
		try {
			this.myUrl = url;
			this.myUrlQuery = query;
			this.myPostData = data;
			this.myMethod = Method.POST;
			
			this.buildUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Create the query string "?key=value&key=value..."
	protected void buildUrl() throws Exception {
		if (this.myUrlQuery.size() < 1)
			return;
		
		this.myUrl += "?";
		
		for (Map.Entry<String, String> someData : this.myUrlQuery.entrySet())
			this.myUrl += URLEncoder.encode(someData.getKey(), "UTF-8") + "=" + URLEncoder.encode(someData.getValue(), "UTF-8") + "&";
		
		this.myUrl = Commons.normalizeUrl(this.myUrl.substring(0, this.myUrl.length() - 1));
	}
	
	// Create the POST data "key=value&key=value..."
	protected String buildPostData() throws Exception {
		String data = "";
		
		for (Map.Entry<String, String> someData : this.myPostData.entrySet())
			data += URLEncoder.encode(someData.getKey(), "UTF-8") + "=" + URLEncoder.encode(someData.getValue(), "UTF-8") + "&";
 
		return this.myUrl.substring(0, this.myUrl.length() - 1);
	}
	
	protected String get() throws Exception {
		HttpURLConnection con = (HttpURLConnection) (new URL(this.myUrl).openConnection());
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
		
		return response.toString();
	}
	
	protected String post() throws Exception {
 
		HttpURLConnection con = (HttpURLConnection) (new URL(this.myUrl).openConnection());
 
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", UrlFetcher.USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=1");
		con.setRequestProperty("charset", "UTF-8");
 
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(this.buildPostData());
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		
		in.close();

		return response.toString();
	}
	
	@Override
	public String call() {
		
		long threadId = Thread.currentThread().getId();
		Commons.allowedThreads.add(threadId);
		String result = null;
		
		try {
			
			if (this.myMethod == Method.GET) result = this.get();
			if (this.myMethod == Method.POST) result = this.post();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
		return result;
	}
}

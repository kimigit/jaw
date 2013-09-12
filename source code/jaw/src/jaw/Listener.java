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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;

import jaw.privileged.LocalStorage;
import jaw.privileged.StringToJson;
import jaw.privileged.StringToXml;
import jaw.privileged.UrlFetcher;

public class Listener implements IListener {

	private Site mySite;
	
	public Listener(Site aSite) {
		this.mySite = aSite;
	}
	
	@Override
	public String httpGet(String url, LinkedHashMap<String, String> query) {
		ExecutorService service;
    Future<String>  task;
    String result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new UrlFetcher(url, query));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public String httpPost(String url, LinkedHashMap<String, String> query, LinkedHashMap<String, String> data) {
		ExecutorService service;
    Future<String>  task;
    String result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new UrlFetcher(url, query, data));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public void setTitle(String title) {
		this.mySite.setTitle(title);
	}

	@Override
	public Document stringToXmlDocument(String xml) {
		ExecutorService service;
    Future<Document>  task;
    Document result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new StringToXml(xml));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public JSONObject stringToJson(String json) {
		ExecutorService service;
    Future<JSONObject>  task;
    JSONObject result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new StringToJson(json));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public boolean store(String key, Object value) {
		ExecutorService service;
    Future<Object>  task;
    boolean result = false;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new LocalStorage(this.mySite.getUrl(), key, value));

    try {
			result = (boolean) task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public Object retrieve(String key) {
		ExecutorService service;
    Future<Object>  task;
    Object result = false;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new LocalStorage(this.mySite.getUrl(), key));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}
	
}

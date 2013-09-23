/* This is the listener class implementing the interface jaw.IListener for
 * app-browser communication.
 */

package jaw;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.scene.image.Image;
import javafx.scene.web.WebView;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;

import jaw.privileged.ImageDownloader;
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


	@Override
	public HashMap<String, String> retrieveAll() {
		ExecutorService service;
    Future<Object>  task;
    HashMap<String, String> result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new LocalStorage(this.mySite.getUrl()));

    try {
			result = (HashMap<String, String>) task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}
	
	@Override
	public Image getImage(String url) {
		ExecutorService service;
    Future<Image>  task;
    Image result = null;

    service = Executors.newFixedThreadPool(1);        
    task    = service.submit(new ImageDownloader(url));

    try {
			result = task.get();
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	@Override
	public URL getUrl() {
		return this.mySite.getUrl();
	}

	@Override
	public void gotoUrl(String url) {
		this.mySite.gotoUrl(url);
	}

	@Override
	public Object getResource(String resourceName) {
		return this.mySite.getJarResource(resourceName);
	}

	@Override
	public Object getFxmlRoot(String resourceName, Object controller, String [] styleSheets) {
		return this.mySite.getFxmlRoot(resourceName, controller, styleSheets);
	}

	@Override
	public WebView getAWebview(String url) {
		return Main.getAWebview(url);
	}
	
}

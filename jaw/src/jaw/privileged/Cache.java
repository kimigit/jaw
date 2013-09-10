/* This class manages the browser cache.
 * 
 */

package jaw.privileged;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;

import jaw.Commons;

public class Cache implements Runnable {
	
	public static final String PATH = "cache/";
	
	protected static boolean createDirectory(String directoryName) {
		try {
			
			String [] dirs = directoryName.split("/");
			directoryName = "";
			File theDir = null;
			
			for (int i = 0, length = dirs.length; i < length; i++) {
				directoryName += dirs[i] + "/";
				theDir = new File(directoryName);
				if (!theDir.exists() && !theDir.mkdir())
					return false;
			}

			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return false;
		}
	}
	
	private static String getAppHome(URL url) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(url.toString().getBytes("UTF-8"));
			
			return Cache.PATH + new String(Commons.bytesToHex(md.digest())).replaceAll("(..)", "$1/");
	    
		} catch (Exception e) {
			
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static String createAppHome(URL url) {
		try {
			
			String home = Cache.getAppHome(url);
			
			if (home == null) return null;
			
			return Cache.createDirectory(home) ? home : null;
	    
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean isOk() {
		return Cache.createDirectory(Cache.PATH);
	}

	
	
	
	public enum Action {
		CreateApplicationHome
	}
	
	protected Action myAction;
	protected Object [] myOptions;
	
	public Cache(Action action, Object... args) {
		this.myAction = action;
		this.myOptions = args;
	}
	
	@Override
	public void run() {
		
		long threadId = Thread.currentThread().getId();
		
		Commons.allowedThreads.add(threadId);
		
		try {
			
			switch (this.myAction) {
			case CreateApplicationHome:
				
				break;
			}
			
		} catch (Exception e) {
			
		} finally {
			Commons.allowedThreads.remove(threadId);
		}
		
	}
	
}

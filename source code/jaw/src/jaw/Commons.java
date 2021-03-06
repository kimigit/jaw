/* This class contains static variables and functions that are used
 * across the whole application
 */

package jaw;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Commons {
	
	/* SecurityManager will check this value to see if the running thread
	 * as the right to perform some operation
	 */
	public static ArrayList<Long> allowedThreads = new ArrayList<Long>();
	
	public static final String TITLE_SHORT = "JAW";
	public static final String TITLE_LONG = "Just A Window";
	
	/* Take a URL and return a canonical version of it
	 * 
	 */
	public static String normalizeUrl(String url) {
		
		String protocol = null;
		url = url.trim();
		
		try {
			
			// This line throws an exception if url doesn't have a protocol
			protocol = new URL(url).getProtocol();
			
			switch (protocol.toLowerCase()) {
			case "http":
			case "https":
				return url;
			default:
				throw new Exception("Not a supported protocol: " + protocol);
			}
			
		} catch (Exception e) {
			return "http://" + url;
		}
	}
	
	// Generate a random alphanumeric string
	public static String randomString(int length) {
		if (length < 1) return "";
		
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		
		for (int i = 0; i < length; i++)
			sb.append(chars[random.nextInt(chars.length)]);
		
		return sb.toString();
	}
	
	public static char[] bytesToHex(byte [] bytes) {
		char [] base16 = {'0', '1', '2', '3',
											'4', '5', '6', '7',
											'8', '9', 'a', 'b',
											'c', 'd', 'e', 'f'};
		
    char [] hex = new char[bytes.length * 2];

    for (int i = 0, aByte, length = bytes.length; i < length; i++) {
    	aByte = bytes[i] & 0xFF;
    	
      hex[i * 2] = base16[aByte >>> 4];
      hex[i * 2 + 1] = base16[aByte & 0x0F];
    }
    
    return hex;
	}
	
	public static Document stringToXmlDocument(String xml) throws Exception {
		return
			DocumentBuilderFactory.newInstance()
			.newDocumentBuilder()
			.parse(new InputSource(new StringReader(xml)));
	}
	
	public static boolean createDirectory(String directoryName) {
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
			return false;
		}
	}
	
}

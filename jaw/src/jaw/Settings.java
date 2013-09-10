/* JAW browser settings
 */
package jaw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

public class Settings {
	
	protected static String file = "settings";
	
	public static void checkFile() {
		try {
			/* Atomically creates a new, empty file named by this abstract pathname if
			 * and only if a file with this name does not yet exist. The check for the
			 * existence of the file and the creation of the file if it does not exist
			 * are a single operation that is atomic with respect to all other filesystem
			 * activities that might affect the file. 
			 */
			new File(Settings.file).createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void load() {
		JsonReader reader = null;
		
		try {
			reader = Json.createReader(new FileReader(Settings.file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JsonStructure jsonst = reader.read();
	}
	
}

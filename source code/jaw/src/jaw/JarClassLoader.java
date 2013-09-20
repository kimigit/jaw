/* Read a JAW application .jar file from file system and load it into memory.
 * 
 * Note: the only classes allowed (and hence loaded) are
 *   - app.*
 */

package jaw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javafx.fxml.FXMLLoader;

public class JarClassLoader extends ClassLoader {
	
	// Reference to the jar file
	protected JarFile myJar;
	
	/* Packages within the app jar that must not be loaded.
	 * This is used to make sure only app.* files are loaded from the jar
	 */
	protected ArrayList<String> myUntrustedPackages;
	
	// List of Java classes in the app
	protected Hashtable<String, Class<?>> jarClasses;
	
	// List of resources (i.e. not Java classes) in the app
	protected Hashtable<String, Object> jarResources;

  public JarClassLoader(String jarFile) throws IOException {
  	super(JarClassLoader.class.getClassLoader());

  	this.jarClasses = new Hashtable<String, Class<?>>();
  	this.jarResources = new Hashtable<String, Object>();
  	this.myUntrustedPackages = new ArrayList<String>();
  	this.myJar = new JarFile(jarFile);
  	
  	this.getUntrustedPackages();
  }  
  
  /* For every application imported, only classes within the "app" package
   * are loaded. Therefore this method get a list of all packages in the
   * app jar and marks everything as untrusted except "app" package.
   * This list is useful because every time loadClass() is called we can
   * discriminate between classes that can be loaded and those who can't.  
   */
  protected void getUntrustedPackages() {
  	
  	Enumeration e = this.myJar.entries();
    JarEntry entry;
    String classPackage = "", className = "";
    int firstDot;
    
    while (e.hasMoreElements()) {
    	entry = (JarEntry) e.nextElement();
	    
    	className = entry.getName();
    	firstDot = className.indexOf('/'); 
    	
    	// There is a package
    	if (firstDot > 0) {
    		classPackage = className.substring(0, firstDot + 1);
    		
    		if (!classPackage.equals("jaw/") && !classPackage.equals("app/"))
  	    	this.myUntrustedPackages.add(classPackage);
    	}
    }
  }
  
  protected byte[] loadResource(String resourceName) {
  	
  	try {
  		
  		// Is resourceName in the valid "app" package?
  		if (this.myUntrustedPackages.contains(resourceName.substring(0, resourceName.indexOf('/'))))
  			throw new Exception("Can't load this resource");
  		
  		byte classByte[];
  	    
	    /* className is given as "package.className" but .getJarEntry() works with a
	     * directory-like syntax, i.e. "package/className".
	     * Note the file extension appended to the className
	     */
			JarEntry entry = this.myJar.getJarEntry(resourceName);
      InputStream is = this.myJar.getInputStream(entry);
      
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
      int nextValue = is.read();
      
      while (-1 != nextValue) {  
	      byteStream.write(nextValue);  
	      nextValue = is.read();  
      }  

      classByte = byteStream.toByteArray();
      
      return classByte;
  		
  	} catch (Exception e) {
  		return null;
  	}
  }
  
  // Load FXML from String
  protected void loadFXML(String fileName) {
  	try {
  		
	  	this.jarResources.put(
	  		fileName,
	  		new FXMLLoader().load(new ByteArrayInputStream(this.loadResource(fileName)))
	  	);
	  	
  	} catch (Exception e) {
  		
  	}
  }
  
  /**
   * @param className Class name of the form "package.ClassName"
   */
  @Override
  public Class<?> loadClass(String className) throws ClassNotFoundException {
  	
  	try {
  		// Is className in the valid "app" package?
  		if (!this.myUntrustedPackages.contains(className.substring(0, className.indexOf('.'))))
  			return findClass(className);
  		
  		return null;
  	} catch (Exception e) {
  		return null;
  	}
  }  
  
  /**
   * @param className Class name of the form "package.ClassName"
   */
  @Override
  public Class<?> findClass(String className) {
  	
  	byte classByte[];
		Class<?> result = null;  
		
		// Checks in cached classes
		result = (Class<?>) this.jarClasses.get(className);  
		if (result != null) return result;
		
		// Check a valid system class before we actually check into the jar
		try { return findSystemClass(className);} catch (Exception e) {}
		
		try {
	    
	    /* className is given as "package.className" but .getJarEntry() works with a
	     * directory-like syntax, i.e. "package/className".
	     * Note the file extension appended to the className
	     */
			JarEntry entry = this.myJar.getJarEntry(className.replace('.', '/') + ".class");
      InputStream is = this.myJar.getInputStream(entry);
      
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
      int nextValue = is.read();
      
      while (-1 != nextValue) {  
	      byteStream.write(nextValue);  
	      nextValue = is.read();  
      }  

      classByte = byteStream.toByteArray();
      result = defineClass(className, classByte, 0, classByte.length, null);
      
      this.jarClasses.put(className, result);
      
      return result;
      
    } catch (Exception e) {
    	return null;  
    }
	}

  // This function is called when the app is loading, and loads all jar files into memory.
  public void loadAll() {
  	
  	// List of jar files
    Enumeration<?> jarEntries = this.myJar.entries();
    String entryName, fileName, fileExtension;
    
    while (jarEntries.hasMoreElements()) {
    	entryName = ((JarEntry) jarEntries.nextElement()).getName();
    	
    	// Only load if in "app" package
    	if (entryName.indexOf("app/") == 0) {
    		
    		try {
	    		fileName = entryName.substring(0, entryName.lastIndexOf('.'));
	    		fileExtension = entryName.substring(entryName.lastIndexOf('.')).toLowerCase();
	    		
	    		// Check if this is a Java class, i.e. a file that ends with ".class"
	    		if (fileExtension.equals(".class"))
	    			try { this.loadClass(fileName.replace('/', '.')); } catch (Exception e) {}
	    		
	    		if (fileExtension.equals(".fxml"))
	    			try { this.loadFXML(entryName); } catch (Exception e) {}
	    		
    		} catch (Exception e) {
    			// Bad fileName or fileExtension
    		}
    	}
    }
  }
  
  public Hashtable<String, Class<?>> getJarClasses() {
  	return this.jarClasses;
  }
  
  public Hashtable<String, Object> getJarResources() {
  	return this.jarResources;
  }
} 
/* Read a JAW application .jar file from file system and load its class onto
 * memory.
 * 
 * Note: the only classes allowed (and hence loaded) are
 *   - jaw.IListener
 *   - app.*
 */

package jaw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {
	
	protected JarFile myJar;
	protected ArrayList<String> myUntrustedPackages;
	
	// Used to cache already defined classes
  protected Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();

  public JarClassLoader(String jarFile) throws IOException {
  	super(JarClassLoader.class.getClassLoader());

  	this.myJar = new JarFile(jarFile);
  	this.getUntrustedPackages();
  }  
  
  protected void getUntrustedPackages() {
  	this.myUntrustedPackages = new ArrayList<String>();
  	
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
  
  @Override
  public Class loadClass(String className) throws ClassNotFoundException {
  	
  	String classNameSlashed = className.replace('.', '/');
  	int firstDot = classNameSlashed.indexOf('/'); 
  	
  	// There isn't a package
  	if (firstDot < 1) return null;
  	
  	String classPackage = classNameSlashed.substring(0, firstDot + 1);
  	
		if (!this.myUntrustedPackages.contains(classPackage) || className.equals("jaw.IListener"))
			return findClass(className);
  	 
  	return null;
  }  

  public Class findClass(String className) {
  	
		byte classByte[];  
		Class<?> result = null;  
		
		// Checks in cached classes
		result = (Class<?>) classes.get(className);  
		if (result != null) return result;
		
		try { return findSystemClass(className); } catch (Exception e) {}
		
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
      
      classes.put(className, result);
      
      return result;
      
    } catch (Exception e) {
    	
    	// e.printStackTrace();
    	
    	return null;  
    }
	}
} 
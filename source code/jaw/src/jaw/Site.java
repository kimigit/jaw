/* A jaw.Site is created by jaw.Window when a new URL address is loaded
 * 
 */

package jaw;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Hashtable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Site {
	
	protected Window myWindow;
	protected JarClassLoader myClassLoader;
	protected Hashtable<String, Class<?>> myJarClasses;
	protected Hashtable<String, Object> myJarResources;
	protected URL myUrl;
	
	public Site(String url, String appPath, Window w) {

		try {
  		
			this.myUrl = new URL(url);
	  	this.myWindow = w;
	  	this.myClassLoader = new JarClassLoader(appPath);
	  	
	  	this.myClassLoader.loadAll();
	  	
	  	this.myJarClasses = this.myClassLoader.getJarClasses();
	  	this.myJarResources = this.myClassLoader.getJarResources();
	  	
	  	this.myJarClasses.get("app.Main")
	  		.getConstructor(IListener.class, BorderPane.class)
				.newInstance(new Listener(this), this.myWindow.getHostPanel());
  		
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
	
	public void setTitle(String newTitle) {
		this.myWindow.setTitle(newTitle);
	}
	
	public URL getUrl() {
		return this.myUrl;
	}
	
	public void gotoUrl(String url) {
		this.myWindow.gotoUrl(url);
	}
	
	public Object getJarResource(String fileName) {
		return this.myJarResources.get(fileName);
	}
	
	public Object getFxmlRoot(String resourceName, Object controller, String [] styleSheets) {
		try {
			
			FXMLLoader aFXMLLoader = new FXMLLoader();
			aFXMLLoader.setController(controller);
			Parent sceneRoot = (Parent) aFXMLLoader.load(new ByteArrayInputStream((byte[])this.myJarResources.get(resourceName)));
			
			for (String aCss : styleSheets)
				;//sceneRoot.getStyleClass().add(new String((byte[])this.myJarResources.get(aCss)));
			
			return sceneRoot;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

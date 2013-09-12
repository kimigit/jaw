/* A jaw.Site is created by jaw.Window when a new URL address is loaded
 * 
 */

package jaw;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Site {
	
	protected Listener myListener;
	protected Window myWindow;
	protected JarClassLoader myUrlClassLoader;
	protected BorderPane hostPanel;
	protected URL myUrl;
	
	public Site(String url, String appPath, Window w) {

		try {
  		
			this.myUrl = new URL(url);
			this.myListener = new Listener(this);
	  	this.myWindow = w;
	  	this.hostPanel = this.myWindow.getHostPanel();
	  	
  		new JarClassLoader(appPath)
  			.loadClass("app.Main")
  			.getConstructor(IListener.class, BorderPane.class)
  			.newInstance(this.myListener, this.hostPanel);
  		
		} catch (Exception e) {
		}
  }
	
	public void setTitle(String newTitle) {
		this.myWindow.setTitle(newTitle);
	}
	
	public URL getUrl() {
		return this.myUrl;
	}
	
}

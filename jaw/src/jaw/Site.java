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

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Site {
	
	public static boolean load = true;
	
	protected Listener myListener;
	protected Window myWindow;
	protected JarClassLoader myUrlClassLoader;
	protected BorderPane hostPanel;
	
	public Site(String appPath, Window w) {

		
		this.myListener = new Listener(this);
  	this.myWindow = w;
  	this.hostPanel = this.myWindow.getHostPanel();
  	
  	try {
  		
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
	
}

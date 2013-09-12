package jaw;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;

import jaw.privileged.Cache;
import jaw.privileged.LocalStorage;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

public class Main extends Application {
	
	protected final static String FXML = "Window.fxml";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (!Cache.isOk()) return;
		if (!LocalStorage.isOk()) return;
		Settings.checkFile();
		
		Commons.allowedThreads.add(Thread.currentThread().getId());
		
		System.setSecurityManager(new Sandbox());
		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		URL location = null;
		FXMLLoader fxmlLoader = null;;
		Parent root = null;
		Scene scene = null;
		Window fxmlController = null;
		
		try {
			
			location = getClass().getResource(Main.FXML);
			fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			root = (Parent) fxmlLoader.load(location.openStream());
			scene = new Scene(root);
			fxmlController = (Window) fxmlLoader.getController();
			
			fxmlController.setTitle(Commons.TITLE_LONG);
			stage.setTitle(Commons.TITLE_SHORT);
			stage.initStyle(StageStyle.UNDECORATED);
	    stage.setScene(scene);
	    
	    // fxmlController.maximize();
	    fxmlController.ready();
	    stage.show();
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


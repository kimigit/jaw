package jaw;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;

import jaw.privileged.Cache;
import jaw.privileged.LocalStorage;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
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
		
		//System.setSecurityManager(new Sandbox());
		
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
		}
	}

}


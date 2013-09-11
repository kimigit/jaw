/* This is the FXML controller for Window.fxml
 */
package jaw;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Window {

	// Keep coordinates while dragging
	protected static class Drag {
		public static double X, Y;
	}
	
	@FXML protected ResourceBundle resources;
  @FXML protected URL location;
  
  @FXML protected AnchorPane root;
  @FXML protected TextField address;
  @FXML protected GridPane titlePanel;
  @FXML protected BorderPane appPanel;
  @FXML protected Button buttonBack;
  @FXML protected Button buttonSettings;
  @FXML protected Button buttonClose;
  @FXML protected Button buttonMaximize;
  @FXML protected Label windowTitle;
  
  protected RecentHistory myRecentHistory;
  protected Stage myStage;
  protected BorderPane hostPanel;
  
  private void addDraggableNode(final Node node) {
  	node.setOnMousePressed(new EventHandler<MouseEvent>() {
  		@Override
  		public void handle(MouseEvent me) {
  			if (me.getButton() != MouseButton.MIDDLE) {
  				Window.Drag.X = me.getSceneX();
  				Window.Drag.Y = me.getSceneY();
  			}
  		}
  	});
  	
  	node.setOnMouseDragged(new EventHandler<MouseEvent>() {
  		@Override
  		public void handle(MouseEvent me) {
  			if (me.getButton() != MouseButton.MIDDLE) {
  				node.getScene().getWindow().setX(me.getScreenX() - Window.Drag.X);
  				node.getScene().getWindow().setY(me.getScreenY() - Window.Drag.Y);
  			}
  		}
  	});
  }
  
  @FXML
  protected void initialize() {
  	this.myRecentHistory = new RecentHistory();
  	Window.Drag.X = Window.Drag.Y = 0;
  	
  }
  
  public void ready() {
  	this.addDraggableNode(this.titlePanel);
  }
  
  protected boolean checkStage() {
  	this.myStage = (Stage) this.root.getScene().getWindow();
  			
  	return this.myStage != null;
  }
  
  @FXML
  protected void gotoUrl(ActionEvent event) {
  	this.myRecentHistory.push(this.address.getText());
  	this.buttonBack.setDisable(this.myRecentHistory.count() < 2);
  	
  	loadApp(downloadUrl(this.address.getText()));
  }
  
  @FXML
  void goBack(MouseEvent event) {
  	String previousAddress = this.myRecentHistory.pop();
  	this.buttonBack.setDisable(this.myRecentHistory.count() < 2);
  	loadApp(downloadUrl(previousAddress));
  }
  
  @FXML
  void close(MouseEvent event) {
  	Platform.exit();
  	System.exit(0);
  }
  
  @FXML
  void minimize(MouseEvent event) {
  	if (!this.checkStage()) return;
  	
  	this.myStage.setIconified(true);
  }
  
  @FXML
  void maximize(MouseEvent event) {
  	this.maximize();
  }
  
  @FXML
  void openSettings(MouseEvent event) {
  	loadApp("settings.jar");
  }
  
	protected String downloadUrl(String url) {
		
		try {
			
			String appHash = Commons.randomString(64);
			
			Thread t = new Thread(new jaw.privileged.UrlDownloader(url, appHash));
			t.start();
			t.join();
	    
			return Commons.appHash.containsKey(appHash) ? Commons.appHash.get(appHash) : null;
	    
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    
	}

	protected void loadApp(String appPath) {
		
		if (appPath == null) return;
		
		this.hostPanel = new BorderPane();
		
		this.appPanel.getChildren().clear();
		this.appPanel.setCenter(this.hostPanel);
		
		new Site(appPath, this);
	}
	
	public BorderPane getHostPanel() {
		return this.hostPanel;
	}
	
	public void setTitle(String newTitle) {
		// Set the title in the top bar
		this.windowTitle.setText(newTitle);
		
		// Set the title for the window. This will be shown in the system taskbar
		if (this.checkStage())
			this.myStage.setTitle(newTitle + " - " + Commons.TITLE_SHORT);
	}
	
	public void maximize() {
  	if (!this.checkStage()) return;
  	
  	try {
	  	Screen screen = Screen.getPrimary();
	  	Rectangle2D bounds = screen.getVisualBounds();
	  	
	  	double	x = bounds.getMinX(),
	  					y = bounds.getMinY(),
	  					width = bounds.getWidth(),
	  					height = bounds.getHeight();
	  	
	  	boolean isMaximized = (x == this.myStage.getX() && y == this.myStage.getY() && width == this.myStage.getWidth() && height == this.myStage.getHeight());
	  	
	  	// Toggle maximize window
	  	if (isMaximized) {
	  		
	  		this.myStage.setX(width / 6);
	  		this.myStage.setY(height / 6);
	  		this.myStage.setWidth(width * 2/3);
	  		this.myStage.setHeight(height * 2/3);
		  	
	  	} else {
	  		
	  		this.myStage.setX(x);
	  		this.myStage.setY(y);
	  		this.myStage.setWidth(width);
	  		this.myStage.setHeight(height);
	  	}
  	} catch (Exception e) {
  	}
	}
}

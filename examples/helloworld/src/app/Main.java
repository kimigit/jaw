package app;
	
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import jaw.IListener;

public class Main {
	
	protected IListener jawListener;
	protected Pane appPane;
	protected Label helloLabel;
	protected Button helloButton;
	
	public Main(IListener l, Pane p) {
		
		// Listener to communicate with the browser
		this.jawListener = l;
		
		// Pane where I can add UI components
		this.appPane = p;
		
		// Create a label
		this.helloLabel = new Label("Hello World");
		this.helloLabel.setFont(Font.font("Verdana", 48));
		
		// Create a button
		this.helloButton = new Button("write something");
		this.helloButton.setFont(Font.font("Verdana", 25));
		this.helloButton.setTranslateX(100);
		this.helloButton.setTranslateY(300);
		this.helloButton.setRotate(25);
		this.helloButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
	    	System.out.println("Hello World");
	    }
		});
		
		// Add everything to the panel
		this.appPane.getChildren().add(this.helloLabel);
		this.appPane.getChildren().add(this.helloButton);
		
		// Add a title to the window
		this.jawListener.setTitle("Hello World");
		
		// Here we add a TimerTask to change colors every second
		new Timer().scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
		  	try {
		  		
		  		Random rnd = new Random();
			  	
			  	appPane.setStyle("-fx-background-color: rgb(" + rnd.nextInt(256) + "," + rnd.nextInt(256) + "," + rnd.nextInt(256) + ");");
			  	helloLabel.setTextFill(Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()));			  	
			  	
		  	} catch (Exception e) {
		  		e.printStackTrace();
		  	}
		  }
		}, 0, 1000);

		
	}
}

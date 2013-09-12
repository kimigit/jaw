// This app makes a simple search at DBPedia and display the first results

package app;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sun.misc.IOUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jaw.IListener;

public class Main {
	
	protected IListener jawListener;
	protected BorderPane appPane;
	protected Label helloLabel;
	protected Button helloButton;

	protected BorderPane container;
	
	protected TextField input;
	
	protected ScrollPane spResults;
	protected VBox results;
	
	public Main(IListener l, BorderPane p) {
		
		// Listener to communicate with the browser
		this.jawListener = l;
		
		// Pane where I can add UI components
		this.appPane = p;
		
		// A container for our UI components
		container = new BorderPane();
		
		// Component for results (with a scoller bar)
		spResults = new ScrollPane();
		results = new VBox();
		
		spResults.setContent(results);
		spResults.setFitToWidth(true);
		
		// Create the input text box
		input = new TextField("Search: ");
		
		input.setOnAction(new EventHandler<ActionEvent>() {
	    @Override public void handle(ActionEvent e) {
	    	searchDbpedia(input.getText());
	    }
		});
		
		// Add everything to the container...
		container.setTop(input);
		container.setCenter(spResults);
		
		// ... and the container to the browser
		this.appPane.setCenter(container);
		
		// And set a nice title :)
		this.jawListener.setTitle("Search DBPedia");
	}
	
	protected void searchDbpedia(String tags) {
		try {
			
			// Clear the list of results
			results.getChildren().clear();
			
			// Build the query string for the URL
			LinkedHashMap<String, String> query = new LinkedHashMap<String, String>();
			query.put("MaxHits", "32");
			query.put("QueryString", tags);
			
			// Query DBPedia and get the XML in return
			String xml = this.jawListener.httpGet("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch", query);
			
			// Get a DOM
			Document doc = this.jawListener.stringToXmlDocument(xml);
			
			// And now we're ready to traverse the DOM
			
			NodeList nList = doc.getElementsByTagName("Result");
			
			for (int temp = 0, length = nList.getLength(); temp < length; temp++) {
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					Label name = new Label(eElement.getElementsByTagName("Label").item(0).getTextContent().trim());
					name.setWrapText(true);
					name.setStyle("-fx-padding: 10px 10px 5px 10px; -fx-font-weight: bold;");
					
					Label description = new Label(eElement.getElementsByTagName("Description").item(0).getTextContent().trim());
					description.setWrapText(true);
					description.setStyle("-fx-padding: 0px 10px 30px 10px;");
					
					results.getChildren().addAll(name, description);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package jaw;

import java.awt.Container;
import java.util.regex.Pattern;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.JPanel;

public class UntrustedPane extends Pane {
	
	public UntrustedPane() {
		super();
	}
	
	/*
	@Override
	public Parent getParent() {
		
		long tId = Thread.currentThread().getId();
		
		StackTraceElement [] e = Thread.currentThread().getStackTrace();
		
		boolean allow = false;
		
		for (StackTraceElement i : e) {
			if (allow)
				if (i.getClassName().indexOf("app.") == 0)
					return null;
				else
					break;
			
			if (!allow && i.getClassName().equals("jaw.UntrustedJPanel"))
				allow = true;
		}
		
		return super.getParent();
	}
	*/
}

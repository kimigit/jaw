package jaw;

import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;


public interface IListener {
	public String httpGet(String url);
	public String httpPost(String url, LinkedHashMap<String, String> data);
	public void setTitle(String title);
}

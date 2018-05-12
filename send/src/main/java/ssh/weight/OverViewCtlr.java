package ssh.weight;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import ssh.client.conn;
import ssh.log.PanelLog;

public class OverViewCtlr {
	
	protected static final Logger logger = LoggerFactory.getLogger(OverViewCtlr.class);
	
	private static OverViewCtlr instance; 
	public static int i = 0;
	public static boolean flag = true;
	private static Thread thread = null;
	PanelLog panelLog;
	
	public static final String CSS_LOG_BASE = "  -fx-font-size:13px ";

	@FXML
	private SplitPane splitPane;

	@FXML
	public Button spButton;
	
	@FXML
	public ProgressBar progressBar;
	

	@FXML
	public void initialize() {
		instance = this;
		panelLog = new PanelLog(CSS_LOG_BASE);
		splitPane.getItems().add(0, panelLog);

	}
	 
	 public static void setLogData(String value) throws InterruptedException {
		 //logger.debug("setLogData : " + value);
		 String Result = Serial.GetResult();
			if(Result !="E") {
		 StringTokenizer tokens = new StringTokenizer(Result,"^");
		 Result = tokens.nextToken();

			}
	 }


	 @FXML
	 public void AutoSearch() {
		if(flag == true) {
		progressBar.setProgress(-1.0f);
		setspButton(true);
		 try {
			
			Runnable conn = new conn();
			thread = new Thread(conn);
 	    	thread.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else { progressBar.setProgress(0f); thread.interrupt(); setspButton(false);}
	 }
	 
	 public static void setspButton(boolean b) {
		 if(b == true)
		 {instance.spButton.setText("STOP"); flag = false; }
		 else {instance.spButton.setText("AUTO SEND"); flag=true; logger.debug("STOP Sucessful");}
	 }
}

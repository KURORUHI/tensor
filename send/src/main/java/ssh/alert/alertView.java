package ssh.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class alertView {
	public static void alView(AlertType at,String titleString,String headerString,String infoString) {
	Alert alert = new Alert(at);
	alert.setTitle(titleString);
	alert.setHeaderText(headerString);
	String s =  infoString;
	alert.setContentText(s);
	alert.show();
	}
	
}

package ssh.alert;

import javafx.scene.control.Alert.AlertType;


public class alertLevel {

	public static void showalertLevel(int lv) {
		switch(lv) {
		case 4: alertView.alView(AlertType.INFORMATION,"About..","SSH TransferProgram","Kangnam.sae");break;
		case 6: alertView.alView(AlertType.INFORMATION,"STOP","STOP", "Stop Sucessful.");break;
		}
	}
}

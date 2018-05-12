package ssh.weight;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;

import javafx.util.Pair;
import ssh.client.conn;

public class RootLayoutCtlr {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static RootLayoutCtlr instance; 
	protected static Thread thread;
	protected String PortNumber;
	volatile boolean shutdown = false;
	private int flag = 1;
	private int logflag = 1;
	
	public static Future<Boolean> portIsOpen(final ExecutorService es,final String ip, final int port,final int timeout){ // ExecutorService API�� Future API
		return es.submit(new Callable<Boolean>() {
			@Override public Boolean call() {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(ip,port),timeout);
				socket.close();
				return true;
			}
			catch(Exception e) {
				return false;
			}
		}
		});
		
	}
	
	@FXML
	private void AboutDialog() {
		ssh.alert.alertLevel.showalertLevel(4);
	}
	
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	@FXML
	private void Login() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("SSH Server Login");
		dialog.setHeaderText("Please Login");
		
		ButtonType loginButtonType = new  ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType,ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		
		grid.add(new Label("Username"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
		    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		    conn.id = usernamePassword.getKey().toString();
		    conn.passwd = usernamePassword.getValue().toString();
		});

	}
	
	@FXML
	private void handleStop() throws Exception {
		if(logflag == 1) {
		logflag = 0;
		ssh.alert.alertLevel.showalertLevel(6);
		}
		else ssh.alert.alertLevel.showalertLevel(7);
	}

	public int getFlag() {
		return flag;
	}
	public static void setFlag(int flag) {
		instance.flag = flag;
	}
	
	public static int getlogFlag() {
		return instance.logflag; 
	}

	

}

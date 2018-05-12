package ssh.log;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class PanelLog extends AnchorPane {
	
	public final int MAXIMUM_LENGTH = 1000;
	
	private static TextArea textArea= null;
	private ScrollPane scrollPane = null;
	
	private static PanelLog instance = null;

	public PanelLog(String style) {
		instance = this;
		textArea = new TextArea() {
			 @Override
	            public void replaceText(int start, int end, String text) {
	                super.replaceText(start, end, text);
	                while(getText().split("\n", -1).length > MAXIMUM_LENGTH) {
	                    int fle = getText().indexOf("\n");
	                    super.replaceText(0, fle+1, "");
	                }
	                positionCaret(getText().length());
	            }
		};
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setStyle("-fx-font-size:15px");
		scrollPane = new ScrollPane();
		scrollPane.setContent(textArea);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		
		AnchorPane.setTopAnchor(scrollPane, 0.0);
		AnchorPane.setLeftAnchor(scrollPane, 0.0);
		AnchorPane.setRightAnchor(scrollPane, 0.0);
		AnchorPane.setBottomAnchor(scrollPane, 0.0);
		
		getChildren().addAll(scrollPane);
	}
	
	private void logMessage(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg);
		});
	}
	
	public static void logMessages(String msg) {
		if ( instance != null ) {
			instance.logMessage(msg);
		}
	}
}


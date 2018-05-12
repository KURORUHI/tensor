package ssh.main;

import java.io.IOException;

import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Image Uploader v0.1"); //ver
		initRootLayout();
		showOverView();
		
	}
	private void initRootLayout() { 
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/ssh/view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(event->System.exit(0));
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	private void showOverView()  { 
		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("/ssh/view/OverView.fxml"));
		AnchorPane OverView=(AnchorPane) loader.load();
		rootLayout.setCenter(OverView); // Data TOP SET
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public static void main(String[] args) {
		System.setProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY,"conf/log4j2.xml");
		launch(args);
	}
	

}

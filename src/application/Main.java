package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Principal.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Processamento digital de imagens");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// Loading the OpenCV core library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
package main.java.fx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class GUIMain extends Application {
	
	public static String password;
	
	private static Stage primaryStage;
	private static Stage passStage;

	private static AnchorPane mainLayout;
	private static AnchorPane passLayout;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		GUIMain.primaryStage = primaryStage;
		GUIMain.primaryStage.setTitle("Cryptography App");
		showMainView();
		showEntryPassStage();
	}

	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Main.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene (mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showEntryPassStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Pass.fxml"));
		passLayout = loader.load();
		passStage = new Stage();
		passStage.setTitle("Insert Password");
		passStage.initModality(Modality.WINDOW_MODAL);
		passStage.initOwner(primaryStage);
		Scene scene = new Scene(passLayout);
	
		passStage.setScene(scene);
		passStage.show();
	}


	public static void start(String[] args) {
		launch(args);
		
	}
}

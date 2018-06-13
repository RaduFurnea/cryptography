package main.java.windows;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Ui extends Application {
	
	public static String password;
	
	private static Stage stage1;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage1 = primaryStage;
		stage1.setTitle("Cryptography App");
		showMainView();
		showEntryPassStage();
	}

	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Main.fxml"));
		Scene scene = new Scene(loader.load());
		stage1.setScene(scene);
		stage1.show();
	}
	
	private void showEntryPassStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Password.fxml"));
		Stage passwordStage = new Stage();
		passwordStage.setTitle("Password");
		passwordStage.initModality(Modality.WINDOW_MODAL);
		passwordStage.initOwner(stage1);
		Scene scene = new Scene(loader.load() ,275,91);
		
		passwordStage.setScene(scene);
		passwordStage.show();
	}


	public static void start(String[] args) {
		launch(args);
		
	}
}

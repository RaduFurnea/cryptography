package main.java.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.Main;

public class UiMain extends Application {
	private static Stage primaryStage;
	private static BorderPane mainLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		UiMain.primaryStage = primaryStage;
		UiMain.primaryStage.setTitle("Cryptography App");
		showMainView();
	}

	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/MainWindow.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene (mainLayout,500,500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showLogin() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("login/Login.fxml"));
		BorderPane login = new BorderPane();
		mainLayout.setCenter(login);
	}

	public static void start(String[] args) {
		launch(args);
		
	}
}

package main.java.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class UiMain extends Application {
	
	public static String password;
	
	private static Stage primaryStage;
	private static Stage entryPassStage;

	private static AnchorPane mainLayout;
	private static AnchorPane entryPassLayout;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		UiMain.primaryStage = primaryStage;
		UiMain.primaryStage.setTitle("Cryptography App");
		showMainView();
		showEntryPassStage();
	}

	private void showMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/MainWindow.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene (mainLayout,290,305);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showEntryPassStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/PassEntryWindow.fxml"));
		entryPassLayout = loader.load();
		entryPassStage = new Stage();
		entryPassStage.setTitle("Insert Password");
		entryPassStage.initModality(Modality.WINDOW_MODAL);
		entryPassStage.initOwner(primaryStage);
		Scene scene = new Scene(entryPassLayout,275,91);
		
		entryPassStage.setScene(scene);
		entryPassStage.show();
	}
	

	
	

	public static void start(String[] args) {
		launch(args);
		
	}
}

package main.java.windows;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainWindowController {
	
	private static Stage fileStage;
	
	private static AnchorPane keysLayout;
	private static Stage keysStage;
	
	public void showKeysStage() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/KeysWindow.fxml"));
		keysLayout = loader.load();
		keysStage = new Stage();
		keysStage.setTitle("Select Paths for Keys");
		keysStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(keysLayout,302,177);
		
		keysStage.setScene(scene);
		keysStage.show();
	}


	@FXML private Button loadFile;
	
	@FXML private Button createKey;
	
	@FXML private Button generateKey;
	
	@FXML private Button sign;
	
	@FXML private Button verify;
	
	@FXML private TextField filePath;
	
	@FXML private Label actions;
	
	@FXML
	public void onButtonClick(ActionEvent event) throws IOException{
		if(event.getSource()==loadFile) {
			start(fileStage);
		}
		if(event.getSource()==generateKey) {
			showKeysStage();
		}
	}
	
	private void setPath(String string) {
		filePath.setText(string);
	}
	

	public void start(final Stage stage) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			setPath(file.getPath());
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No file!");
			alert.setHeaderText("No file selected!");
			alert.setContentText("Please select a valid file.");
			alert.showAndWait();
		}
		
	}

	
}

package main.java.ui;

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
import main.java.crypto.Crypto;


public class MainWindowController {
	
	File mainFile;
	File privateF;
	File publicF;
	File signatureF;
	
	private static Stage fileStage;
	private static AnchorPane keysLayout;
	private static Stage keysStage;
	
	public static String path;

	
	public void showKeysStage() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/KeysWindow.fxml"));
		keysLayout = loader.load();
		keysStage = new Stage();
		keysStage.setTitle("Select Paths for Keys");
		keysStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(keysLayout,302,286);
		
		keysStage.setScene(scene);
		keysStage.show();
	}

	@FXML private Button loadFile;
		
	@FXML private Button generateKey;
	
	@FXML private Button sign;
	
	@FXML private Button verify;
	
	@FXML private TextField filePath;
	
	@FXML private Label actions;
	
	@FXML private Button loadPrivate;
	
	@FXML private Button loadPublic;
	
	@FXML private Button loadSignature;
	
	@FXML
	public void onButtonClick(ActionEvent event) throws Exception {
		if(event.getSource()==loadFile) {
			mainFile = start(fileStage, true);
		}
		if(event.getSource()==generateKey) {
			showKeysStage();
		}
		if(event.getSource()==loadPrivate) {
			privateF = start(fileStage, false);
		}
		if(event.getSource()==loadPublic) {
			publicF = start(fileStage, false);
		}
		if(event.getSource()==loadSignature){
			signatureF = start(fileStage, false);
		}
		if(event.getSource()==sign) {
			if(privateF == null || mainFile == null || signatureF == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No files.");
				alert.setHeaderText("Some files not selected.");
				alert.setContentText("Please select all the files needed.");
				alert.showAndWait();
			}
			else 
				new Crypto(UiMain.password).signDocument(privateF.getPath(), mainFile.getPath(), signatureF.getPath()); 
		}
		if(event.getSource()==verify) {
			//System.out.println(publicF.getAbsolutePath());
			if(publicF.getPath().equals(null) || mainFile.getPath().equals(null)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No files.");;
				alert.setHeaderText("Some files not selected.");
				alert.setContentText("Please select all the files needed.");
				alert.showAndWait();
			}
			else {
				if(new Crypto(UiMain.password).verifySigniature(publicF.getPath(), mainFile.getPath(), signatureF.getPath())){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Verification Complete");
					alert.setHeaderText("null");
					alert.setContentText("The verification test is positive."); //exprimare corecta?
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Verification Complete");
					alert.setHeaderText("null");
					alert.setContentText("The verification test is negative."); //exprimare corecta?
					alert.showAndWait();
				}
			}
		}

	}
	
	private void setPath(String string) { //sets text on TextField for display
		filePath.setText(string);
	}
	
	public File start(final Stage stage, Boolean b) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			System.out.println(file.getPath());
			if(b)
				setPath(file.getPath());
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No file!");
			alert.setHeaderText("No file selected!");
			alert.setContentText("Please select a valid file.");
			alert.showAndWait();
		}
		return file;	
	}	
}

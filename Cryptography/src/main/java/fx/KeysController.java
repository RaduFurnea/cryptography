package main.java.fx;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.backend.Messages;

public class KeysController {
	
	
	private static Stage fileStage;
	
	String privatePathString;
	String publicPathString;
	
	String privateFileName;
	String publicFileName;

	@FXML private Button privatePath;
	
	@FXML private Button publicPath;
	
	@FXML private Label privateTextPath;
	
	@FXML private Label publicTextPath;
	
	@FXML private TextField privateName;
	
	@FXML private TextField publicName;
	
	@FXML private Button submit;
	
	@FXML
	public void onButtonClick(ActionEvent event) throws Exception {
		try {
		if(event.getSource()==privatePath) {
			privatePathString = start(fileStage);
			setPrivatePath(privatePathString);
		}
		if(event.getSource()==publicPath) {
			publicPathString = start(fileStage);
			setPublicPath(publicPathString);
		}
		if(event.getSource()==submit)
			if(publicPathString != null && privatePathString != null) {
				new Messages(GUIMain.password).generateKeys(publicPathString, privatePathString);
				
				Stage stage = (Stage) submit.getScene().getWindow();
				stage.close();
			}
			else {
				MainController.alert("No directory!", "No directory selected!", "One or both directories haven't been selected!", AlertType.WARNING);
			}
		}
		catch (Exception e){
			e.printStackTrace();
			MainController.alert("Error", "Error", "Invalid data", AlertType.WARNING);

		}
	}
	
	private void setPrivatePath(String string) { 
		privateTextPath.setText(string);
	}
	
	private void setPublicPath(String string) { 
		publicTextPath.setText(string);
	}
	
	public String start(final Stage stage) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);
		
		return file.getAbsolutePath();
	}
}

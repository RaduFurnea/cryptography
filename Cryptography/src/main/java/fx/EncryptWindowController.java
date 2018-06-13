package main.java.fx;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.backend.Messages;

public class EncryptWindowController {
	
	private static Stage fileStage;
	
	String path;
	String fileName;
	

	@FXML private Button choosePath;
	
	@FXML private Button encrypt;
	
	@FXML private TextField encryptedName;
	
	@FXML private TextField encryptedTextPath;
	
	@FXML
	private void onButtonClick(ActionEvent event) throws Exception {
		try {
		if(event.getSource() == choosePath){
			path = new KeysWindowController().start(fileStage);
			if(path != null)
				setPath(path);			
			else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No directory!");
				alert.setHeaderText("No directory selected!");
				alert.setContentText("Please select a valid directory!");
				alert.showAndWait();
			}
		}
		if(event.getSource() == encrypt) {
			if(path.equals(null)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No directory!");
				alert.setHeaderText("No directory selected!");
				alert.setContentText("Please select a valid directory!");
				alert.showAndWait();
			}
			else { 
				if(encryptedName.getText().equals(null))
					fileName = "encryptedFile";
				else fileName = encryptedName.getText();
				if(MainWindowController.path.equals(null)) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No file!");
					alert.setHeaderText("No file selected!");
					alert.setContentText("Please select a valid file!");
					alert.showAndWait();
				}
				else {
				new Messages(UiMain.password).performDecrypt(MainWindowController.path, path.concat(fileName));
				Stage stage = (Stage) encrypt.getScene().getWindow();
				stage.close();
					}
				}
		}
		}
		catch(Exception e) {}
	}
	
	private void setPath(String string) {
		encryptedTextPath.setText(string);
	}
	
}

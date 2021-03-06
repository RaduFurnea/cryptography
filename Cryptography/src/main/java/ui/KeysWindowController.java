package main.java.ui;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.java.crypto.Crypto;

public class KeysWindowController {
	
	
	private static Stage directoryStage;
	
	String privatePathString;
	String publicPathString;
	
	String privateFileName;
	String publicFileName;

	@FXML private Button privatePath;
	
	@FXML private Button publicPath;
	
	@FXML private TextField privateTextPath;
	
	@FXML private TextField publicTextPath;
	
	@FXML private TextField privateName;
	
	@FXML private TextField publicName;
	
	@FXML private Button submit;
	
	@FXML
	public void onButtonClick(ActionEvent event) throws Exception {
		try {
		if(event.getSource()==privatePath) {
			privatePathString = start(directoryStage);
			setPrivatePath(privatePathString);
		}
		if(event.getSource()==publicPath) {
			publicPathString = start(directoryStage);
			setPublicPath(publicPathString);
		}
		if(event.getSource()==submit)
			if(publicPathString != null && privatePathString != null) {
				if (privateName.getText().equals(""))
					privateFileName = "yourPrivateKey"; //default file name
				else privateFileName = privateName.getText();
				
				if (publicName.getText().equals(""))
					publicFileName = "yourPublicKey"; //default file name
				else publicFileName = publicName.getText();
				
				privatePathString = privatePathString.concat(privateFileName);
				publicPathString = publicPathString.concat(publicFileName);
				
				new Crypto(UiMain.password).generateKeys(publicPathString, privatePathString); 
				
				Stage stage = (Stage) submit.getScene().getWindow();
				stage.close();
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No directory!");
				alert.setHeaderText("No directory selected!");
				alert.setContentText("One or both directories haven't been selected!");
				alert.showAndWait();
			}
		}
		catch (Exception e)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Error");
			alert.showAndWait();
		}
	}
	
	private void setPrivatePath(String string) { //sets text on TextField for display
		privateTextPath.setText(string);
	}
	
	private void setPublicPath(String string) { //sets text on TextField for display
		publicTextPath.setText(string);
	}
	
	public String start(final Stage stage) throws IOException {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		File file = directoryChooser.showDialog(stage);
		
		return file.getAbsolutePath();
	}
}

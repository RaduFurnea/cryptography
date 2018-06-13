package main.java.fx;

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
import main.java.backend.Messages;

public class MainController {
	
	public static void alert(String title, String headerText, String contentText, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	File mainFile;
	File privateF;
	File publicF;
	File signatureF;

	private static AnchorPane generateLayout;
	private static AnchorPane encryptLayout;
	private static AnchorPane decryptLayout;
	private static AnchorPane signaturePathLayout;
	

	private static Stage fileStage;
	private static Stage decryptStage;
	private static Stage generateStage;
	private static Stage encryptStage;
	private static Stage signaturePathStage;
	
	public static String path;
	public static String privateKeyPath;

	public void showKeys() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Keys.fxml"));
		generateLayout = loader.load();
		generateStage = new Stage();
		generateStage.setTitle("Select Paths for Keys");
		generateStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(generateLayout);

		generateStage.setScene(scene);
		generateStage.show();
	}

	public void showEncrypt() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Encrypt.fxml"));
		encryptLayout = loader.load();
		encryptStage = new Stage();
		encryptStage.setTitle("Select Path for Encrypted File");
		encryptStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(encryptLayout, 285, 153);

		encryptStage.setScene(scene);
		encryptStage.show();
	}

	public void showDecrypt() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Decrypt.fxml"));
		decryptLayout = loader.load();
		decryptStage = new Stage();
		decryptStage.setTitle("Select Paths for Decrypted File");
		decryptStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(decryptLayout, 285, 153);

		decryptStage.setScene(scene);
		decryptStage.show();
	}

	public void signaturePath() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Sig.fxml"));
		signaturePathLayout = loader.load();
		signaturePathStage = new Stage();
		signaturePathStage.setTitle("Select Path for Signature");
		signaturePathStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(signaturePathLayout,285,153);
		
		signaturePathStage.setScene(scene);
		signaturePathStage.show();
	}

	@FXML
	private Button loadFile;

	@FXML
	private Button generateKey;

	@FXML
	private Button sign;

	@FXML
	private Button verify;

	@FXML
	private TextField filePath;

	@FXML
	private Label actions;

	@FXML
	private Button loadPrivate;

	@FXML
	private Button loadPublic;

	@FXML
	private Button loadSignature;

	@FXML
	private Button encrypt;

	@FXML
	private Button decrypt;

	@FXML
	private Label sigFileName;

	@FXML
	private Label pvtKeyPath;

	@FXML
	private Label pblKeyPath;

	@FXML
	public void onButtonClick(ActionEvent event) throws Exception {
		try {
			if (event.getSource() == loadFile) {
				mainFile = start(fileStage, true);
			}
			if (event.getSource() == generateKey) {
				showKeys();
			}
			if(event.getSource()==loadPrivate) {
				privateF = start(fileStage, false);
				privateKeyPath = privateF.getPath();
				pvtKeyPath.setText(privateF.getPath());
			}
			if (event.getSource() == encrypt) {
				if(path != null)
					showEncrypt();
				else
					alert("No files.", "Some files not selected.", "Please select all the files needed.", Alert.AlertType.WARNING);
					
			}
			if (event.getSource() == decrypt) {
				if(path != null)
					showDecrypt();
				else
					alert("No files.", "Some files not selected.", "Please select all the files needed.", Alert.AlertType.WARNING);
			}
			if(event.getSource()==loadPublic) {
				publicF = start(fileStage, false);
				pblKeyPath.setText(publicF.getPath());
			}
			if(event.getSource()==loadSignature){
				signatureF = start(fileStage, false);
				sigFileName.setText(signatureF.getPath());
			}
			if(event.getSource()==sign) {
				if(path != null && privateF != null)
					signaturePath(); 
				else 
					alert("No files.", "Some files not selected.", "Please select all the files needed.", Alert.AlertType.WARNING);
			}
			if (event.getSource() == verify) {
				if (publicF == null || path == null || signatureF == null) {
					alert("No files.", "Some files not selected.", "Please select all the files needed.", Alert.AlertType.WARNING);
				} else {
					if (new Messages(GUIMain.password).verify(mainFile.getPath(), publicF.getPath(),
							signatureF.getPath())) {
						alert("Verification Complete", null, "File is valid!", Alert.AlertType.INFORMATION);
					} else {
						alert("Verification Complete", null, "File was altered!", Alert.AlertType.INFORMATION);
					}
				}
			}
		} catch (Exception e) {
			MainController.alert("Error", "Something went wrong", e.getLocalizedMessage(), Alert.AlertType.ERROR);
		}
	}

	private void setPath(String string) {
		filePath.setText(string);
	}

	public File start(final Stage stage, Boolean bool) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			if (bool) {
				path = file.getAbsolutePath();
				setPath(file.getPath());
			}

		} else {
			alert("No file!","No file selected!","Please select a valid file.", Alert.AlertType.WARNING);
			return null;
		}
		return file;
	}
}

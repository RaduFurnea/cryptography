package main.java.windows;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.CryptographyServices;

public class MainWindow {

	File mainFile;
	File privateF;
	File publicF;
	File signatureF;

	private static Stage fileStage;


	public static String path;
	public static String privateKeyPath;

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
	private void verifyAdded() {
		try {
			if (mainFile != null && privateF != null)
				sign.setDisable(false);
			if (mainFile != null && publicF != null && signatureF != null)
				verify.setDisable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void onButtonClick(ActionEvent event) throws Exception {
		try {
			if (event.getSource() == loadFile) {
				mainFile = start(fileStage, true);

				if (!path.equals(null)) {
					encrypt.setDisable(false);
					decrypt.setDisable(false);
				}
				verifyAdded();
			}
			if (event.getSource() == generateKey) {
				showKeysStage();
			}
			if (event.getSource() == loadPrivate) {
				privateF = start(fileStage, false);
				verifyAdded();
				privateKeyPath = privateF.getPath();
				pvtKeyPath.setText(privateF.getPath());
			}
			if (event.getSource() == encrypt) {
				showEncryptStage();
			}
			if (event.getSource() == decrypt) {
				showDecryptStage();
			}
			if (event.getSource() == loadPublic) {
				publicF = start(fileStage, false);
				verifyAdded();
				pblKeyPath.setText(publicF.getPath());
			}
			if (event.getSource() == loadSignature) {
				signatureF = start(fileStage, false);
				verifyAdded();
				sigFileName.setText(signatureF.getPath());
			}
			if (event.getSource() == sign) {
				signaturePathStage();
			}
			if (event.getSource() == verify) {
				if (publicF.getPath().equals(null) || mainFile.getPath().equals(null)) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No files.");
					;
					alert.setHeaderText("Some files not selected.");
					alert.setContentText("Please select all the files needed.");
					alert.showAndWait();
				} else {
					if (new CryptographyServices(Ui.password).checkSigniature(publicF.getPath(), mainFile.getPath(),
							signatureF.getPath())) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Verification Complete");
						alert.setHeaderText(null);
						alert.setContentText("File is valid."); // exprimare corecta?
						alert.showAndWait();
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Verification Complete");
						alert.setHeaderText(null);
						alert.setContentText("File was altered."); // exprimare corecta?
						alert.showAndWait();
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public void showKeysStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/KeyGenerator.fxml"));
		Stage keysStage = new Stage();
		keysStage.setTitle("Select Paths for Keys");
		keysStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene( loader.load());

		keysStage.setScene(scene);
		keysStage.show();
	}

	public void showEncryptStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Encrypt.fxml"));
		Stage encryptStage = new Stage();
		encryptStage.setTitle("Select Path for Encrypted File");
		encryptStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(loader.load());

		encryptStage.setScene(scene);
		encryptStage.show();
	}

	public void showDecryptStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Decrypt.fxml"));
		Stage decryptStage = new Stage();
		decryptStage.setTitle("Select Paths for Decrypted File");
		decryptStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(loader.load());

		decryptStage.setScene(scene);
		decryptStage.show();
	}

	public void signaturePathStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../../resources/Signature.fxml"));
		Stage signaturePathStage = new Stage();
		signaturePathStage.setTitle("Select path");
		signaturePathStage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(loader.load());

		signaturePathStage.setScene(scene);
		signaturePathStage.show();
	}

	void setPath(String string) { // sets text on TextField for display
		filePath.setText(string);
	}

	public File start(final Stage stage, Boolean b) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			if (b) {
				path = file.getAbsolutePath();
				setPath(file.getPath());
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No file!");
			alert.setHeaderText("No file selected!");
			alert.setContentText("Please select a valid file.");
			alert.showAndWait();
			return null;
		}
		return file;
	}
}

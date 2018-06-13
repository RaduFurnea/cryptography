package main.java.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.backend.Messages;
import main.java.crypto.Crypto;

public class SignatureWindowController {

	private static Stage fileStage;

	String path;
	String fileName;

	@FXML
	private Button choosePath;

	@FXML
	private Button sign;

	@FXML
	private TextField signatureName;

	@FXML
	private TextField signatureTextPath;

	@FXML
	private void onButtonClick(ActionEvent event) throws Exception {
		try {
			if (event.getSource() == choosePath) {
				path = new KeysWindowController().start(fileStage);
				if (path != null)
					setPath(path);
				else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No directory!");
					alert.setHeaderText("No directory selected!");
					alert.setContentText("Please select a valid directory!");
					alert.showAndWait();
				}
			}
			if (event.getSource() == sign) {
				if (path.equals(null)) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No directory!");
					alert.setHeaderText("No directory selected!");
					alert.setContentText("Please select a valid directory!");
					alert.showAndWait();
				} else {
					if (signatureName.getText().equals(null))
						fileName = "signature";
					else
						fileName = signatureName.getText();
					if (MainWindowController.path.equals(null)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("No file!");
						alert.setHeaderText("No file selected!");
						alert.setContentText("Please select a valid file!");
						alert.showAndWait();
					} else {
						new Messages(UiMain.password).signDocument(MainWindowController.privateKeyPath,
								MainWindowController.path, path.concat(fileName));
						Stage stage = (Stage) sign.getScene().getWindow();
						stage.close();
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private void setPath(String string) {
		signatureTextPath.setText(string);
	}

}
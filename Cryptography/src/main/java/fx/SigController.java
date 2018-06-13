package main.java.fx;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.backend.Messages;

public class SigController {

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
				path = new KeysController().start(fileStage);
				if (path != null)
					setPath(path);
				else {
					MainController.alert("No directory!", "No directory selected!",
							"Please select a valid directory!", AlertType.WARNING);
				}
			}
			if (event.getSource() == sign) {
				if (path.equals(null)) {
					MainController.alert("No directory!", "No directory selected!",
							"Please select a valid directory!", AlertType.WARNING);
				} else {

					if (MainController.path.equals(null)) {
						MainController.alert("No file!", "No file selected!", "Please select a valid file!",
								AlertType.WARNING);
					} else {

						if (!path.endsWith("\\") || path.endsWith("/")) {
							path += File.separator;
						}

						new Messages(GUIMain.password).sign(MainController.path,
								MainController.privateKeyPath, path);
						Stage stage = (Stage) sign.getScene().getWindow();
						stage.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Something went wrong");
			alert.setContentText("Please check that configuration is right");
			alert.showAndWait();
		}
	}

	private void setPath(String string) {
		signatureTextPath.setText(string);
	}

}
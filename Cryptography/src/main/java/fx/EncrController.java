package main.java.fx;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.backend.Messages;

public class EncrController {

	private static Stage fileStage;

	String path;
	String fileName;

	@FXML
	private Button choosePath;

	@FXML
	private Button encrypt;

	@FXML
	private TextField encryptedName;

	@FXML
	private TextField encryptedTextPath;

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
			if (event.getSource() == encrypt) {
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
						new Messages(GUIMain.password).performEncrypt(MainController.path, path);
						Stage stage = (Stage) encrypt.getScene().getWindow();
						stage.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainController.alert("Error", "Something went wrong", "Please check that configuration is right", Alert.AlertType.ERROR);
		}
	}

	private void setPath(String string) {
		encryptedTextPath.setText(string);
	}

}

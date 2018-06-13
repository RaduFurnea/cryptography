package main.java.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.backend.Messages;

public class DecrController {

	private static Stage fileStage;

	String path;
	String fileName;

	@FXML
	private Button choosePath;

	@FXML
	private Button decrypt;

	@FXML
	private TextField decryptedName;

	@FXML
	private TextField decryptedTextPath;

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
			if (event.getSource() == decrypt) {
				if (path.equals(null)) {
					MainController.alert("No directory!", "No directory selected!",
							"Please select a valid directory!", AlertType.WARNING);
				} else {
					if (MainController.path.equals(null)) {
						MainController.alert("No file!", "No file selected!", "Please select a valid file!",
								AlertType.WARNING);
					} else {
						new Messages(GUIMain.password).performDecrypt(MainController.path, path);
						Stage stage = (Stage) decrypt.getScene().getWindow();
						stage.close();
					}
				}
			}
		} catch (Exception e) {
			MainController.alert("Error", "Something went wrong", "Please check that configuration is right", Alert.AlertType.ERROR);
			e.printStackTrace();
		}
	}

	private void setPath(String string) {
		decryptedTextPath.setText(string);
	}

}

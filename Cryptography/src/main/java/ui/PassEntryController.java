package main.java.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//For the start-up password verification

public class PassEntryController {

	@FXML private Button submit;
	
	@FXML private TextField password;
	
	@FXML 
	public void onButtonClick(ActionEvent event) {
		if(event.getSource() == submit)
			setPass(password.getText());
	}
	
	@FXML
	public void onEnter(ActionEvent e) {
		setPass(password.getText());
	}
	
	private void setPass(String pass) {
		UiMain.password = pass;
		Stage stage = (Stage) submit.getScene().getWindow();
		stage.close();
	}
	
}

package com.mopp.login.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ForgotIDController implements Initializable {

	@FXML
	private TextField name;

	@FXML
	private TextField email;

	@FXML
	private TextField yourId;

	@FXML
	private AnchorPane forgotIdPane;
	
	@FXML
	private HBox nameHB;
	@FXML
	private HBox emailHB;
	
	public static String searcIDResult;
	
	DatabaseJob dbJob = new DatabaseJob();

	public void next(ActionEvent e) throws Exception {
		String trimName = name.getText().trim();
		String trimEmail = email.getText().trim();
		if(!trimName.equals("") && !trimEmail.equals("")) {
			searcIDResult = dbJob.searchID(name.getText(), email.getText());
			
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/ForgotID_result.fxml")));
			Stage primaryStage = (Stage) forgotIdPane.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			if(trimName.equals("")) {
				nameHB.setVisible(true);
			} else if(trimEmail.equals("")) {
				emailHB.setVisible(true);
			} else {
				nameHB.setVisible(true);
				emailHB.setVisible(true);
			}
		}
	}

	// back to previous page
	@FXML
	private void back(ActionEvent e) throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
		Stage primaryStage = (Stage) forgotIdPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	

}

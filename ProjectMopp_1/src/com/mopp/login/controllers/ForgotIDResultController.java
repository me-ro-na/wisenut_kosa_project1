package com.mopp.login.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ForgotIDResultController implements Initializable {

	@FXML
	public AnchorPane loginResultPane;

	@FXML
	public Label yourId;
	

	public void goLoginPage(ActionEvent e) throws Exception {
		Stage primaryStage = (Stage) loginResultPane.getScene().getWindow();

		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		yourId.setText(ForgotIDController.searcIDResult);

	}

}

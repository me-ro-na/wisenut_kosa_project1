package com.mopp.login.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ForgotPWController implements Initializable {

	@FXML
	private TextField id;

	@FXML
	private TextField name;

	@FXML
	private AnchorPane forgotPwPane;

	DatabaseJob dbJob = new DatabaseJob();

	public void next(ActionEvent event) throws Exception {
		ForgotPWResultController.memNo = dbJob.checkIdandName(id.getText(), name.getText());

		if (ForgotPWResultController.memNo != 0) {
			Stage primaryStage = (Stage) forgotPwPane.getScene().getWindow();
			Scene scene = new Scene(
					FXMLLoader.load(getClass().getResource("/com/mopp/login/views/ForgotPW_result.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			Alert alert = new Alert(null);
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Mopp 비밀번호 변경");
			alert.setHeaderText("변경이 실패했습니다.");
			alert.setContentText("검색조건에 해당되는 결과가 존재하지 않습니다.");
			alert.showAndWait();
		}
	}

	// back to previous page
	@FXML
	private void back(ActionEvent e) throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
		Stage primaryStage = (Stage) forgotPwPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}

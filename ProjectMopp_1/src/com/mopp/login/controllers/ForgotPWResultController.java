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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ForgotPWResultController implements Initializable {

	@FXML
	public AnchorPane searchResultPane;

	@FXML
	public Label yourId;

	@FXML
	public TextField password;

	@FXML
	public TextField confirm;

	static public int memNo = 0;

	DatabaseJob dbJob = new DatabaseJob();

	public void resetPw(ActionEvent e) throws Exception {
		Alert alert = new Alert(null);
		// 입력한 두 값이 일치 하지 않으면 종료
		if (!password.getText().trim().contentEquals(confirm.getText().trim())) {
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Mopp 비밀번호 변경");
			alert.setHeaderText("변경이 실패했습니다.");
			alert.setContentText("입력한 두 값이 일치하지 않습니다.");
			alert.showAndWait();
			return;
		}

		// 비밀번호 reset
		Cryptogram sha256 = new Cryptogram();
		String cryptoPWD = sha256.encrypt(password.getText());
		if (dbJob.passwordReset(memNo, cryptoPWD)) {
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Mopp 비밀번호 변경");
			alert.setHeaderText("변경이 성공했습니다.");
			alert.setContentText("로그인 페이지로 이동합니다.");
			alert.showAndWait();

			Stage primaryStage = (Stage) searchResultPane.getScene().getWindow();

			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	// back to previous page
	@FXML
	private void back(ActionEvent e) throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
		Stage primaryStage = (Stage) searchResultPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}

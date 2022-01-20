package com.mopp.login.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML
	private AnchorPane loginPane;

	@FXML
	private Hyperlink SignUP;

	@FXML
	private Hyperlink login;

	@FXML
	private Hyperlink ForgotID;

	@FXML
	private Hyperlink ForgotPW;

	@FXML
	private Label lblStatus;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;

	public void login(ActionEvent event) throws Exception {
		Cryptogram sha256 = new Cryptogram();
		
		String inputId = txtUserName.getText(); // input field로부터 ID를 받아옴
		String inputPassword = sha256.encrypt(txtPassword.getText()); // input field로부터 password를 받아옴

		DatabaseJob dbJob = new DatabaseJob(); // DB 작업을 위한 객체 생성
		if (dbJob.getLoginAuthority(inputId, inputPassword)) { // id와 password를 전달해서 로그인권한 획득 시도
			// 로그인이 성공하면
			Stage primaryStage = (Stage) loginPane.getScene().getWindow();

			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/verticalmenu/views/main_view.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} else {
			// 로그인이 실패하면
			lblStatus.setText("Login Failed");
		}
	}

	public void signup(ActionEvent event) throws Exception {
		Stage primaryStage = (Stage) loginPane.getScene().getWindow();

		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/SignUp.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void forgotid(ActionEvent event) throws Exception {
		Stage primaryStage = (Stage) loginPane.getScene().getWindow();

		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/ForgotID.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void forgotpw(ActionEvent event) throws Exception {
		Stage primaryStage = (Stage) loginPane.getScene().getWindow();

		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/ForgotPW.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}

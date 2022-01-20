package com.mopp.login.controllers;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignUPController implements Initializable {

	@FXML
	private AnchorPane signUpPane;

	@FXML
	private TextField txtMemberUserId;

	@FXML
	private PasswordField txtMemberPwd;

	@FXML
	private TextField txtMemberName;

	@FXML
	private TextField txtMemberTel;

	@FXML
	private TextField txtMemberEmail1;

	@FXML
	private MenuButton buttonMemberEmail2;

	@FXML
	private RadioButton male;

	@FXML
	private Label emailLabel;

	@FXML
	private Hyperlink submit;

	@FXML
	private CheckBox Egg;

	@FXML
	private CheckBox Flour;

	@FXML
	private CheckBox Oyster;

	@FXML
	private DatePicker birthDay;

	DatabaseJob databaseJob = new DatabaseJob();

	public void next(ActionEvent event) throws Exception {
		Alert alert = new Alert(null);

		LocalDate today = LocalDate.now();
		String memUserId = txtMemberUserId.getText();
		String memPwd = txtMemberPwd.getText();
		String memName = txtMemberName.getText();
		String memTel = txtMemberTel.getText();
		String memEmail1 = txtMemberEmail1.getText();
		String memEmail2 = buttonMemberEmail2.getText();
		String memGender = male.isSelected() ? "M" : "F";
		String memBirth = birthDay.getValue().toString();
		String memReg = String.valueOf(today.getYear()).substring(2) + "/" + today.getMonthValue() + "/"
				+ today.getDayOfMonth();
		String memIsAdmin = "F";
		System.out.println(memBirth);

		// 제약조건 체크
		alert.setAlertType(AlertType.INFORMATION);
		alert.setTitle("Mopp 사용자 등록");
		alert.setContentText("올바른 값을 입력하세요.");
		if (memUserId.contentEquals("")) {
			alert.setHeaderText("ID를 입력하지 않았습니다.");
			alert.showAndWait();
			txtMemberUserId.requestFocus();
		} else if (memPwd.contentEquals("")) {
			alert.setHeaderText("비밀번호를 입력하지 않았습니다.");
			alert.showAndWait();
			txtMemberPwd.requestFocus();
		} else if (memName.contentEquals("")) {
			alert.setHeaderText("이름을 입력하지 않았습니다.");
			alert.showAndWait();
			txtMemberName.requestFocus();
		} else if (memTel.contentEquals("")) {
			alert.setHeaderText("전화번호를 입력하지 않았습니다.");
			alert.showAndWait();
			txtMemberTel.requestFocus();
		} else if (memEmail1.contentEquals("")) {
			alert.setHeaderText("E-mail ID를 입력하지 않았습니다.");
			alert.showAndWait();
			txtMemberEmail1.requestFocus();
		} else if (memEmail2.contentEquals("선택")) {
			alert.setHeaderText("E-mail 홈페이지를 입력하지 않았습니다.");
			alert.showAndWait();
			buttonMemberEmail2.requestFocus();
		} else {
			Map<Integer, String> memberInfo = new HashMap<Integer, String>();
			memberInfo.put(2, memUserId);
			memberInfo.put(3, memName);
			memberInfo.put(4, pwdTrans(memPwd));
			memberInfo.put(5, memTel);
			memberInfo.put(6, memGender);
			memberInfo.put(7, memEmail1 + "@" + memEmail2);
			memberInfo.put(8, memBirth);
			memberInfo.put(9, memReg);
			memberInfo.put(10, memIsAdmin);

			if (databaseJob.createMember(memberInfo)) {
				alert.setAlertType(AlertType.CONFIRMATION);
				alert.setTitle("Mopp 사용자 등록");
				alert.setHeaderText("등록이 성공했습니다.");
				alert.setContentText(memName + "님, 반갑습니다!");
				alert.showAndWait();

				Stage primaryStage = (Stage) signUpPane.getScene().getWindow();
				Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
				primaryStage.setScene(scene);
				primaryStage.show();

			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Mopp 사용자 등록");
				alert.setHeaderText("등록이 실패했습니다.");
				alert.setContentText("관리자에게 문의하시기 바랍니다.");
				alert.showAndWait();
			}
		}
	}

	private String pwdTrans(String pwd) throws NoSuchAlgorithmException {
		Cryptogram sha256 = new Cryptogram();
		String result = sha256.encrypt(pwd);

		return result;
	}

	// back to previous page
	@FXML
	private void back(ActionEvent e) throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml")));
		Stage primaryStage = (Stage) signUpPane.getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LocalDate today = LocalDate.now();
		birthDay.setValue(today);
	}

	public void setEmail2(ActionEvent event) {
		// email2에 등록된 naver, google, hanmail 중 어떤 item인지 체크
		MenuItem item = (MenuItem) event.getSource();

		// email2 button에 등록
		buttonMemberEmail2.setText(item.getText());
	}
}

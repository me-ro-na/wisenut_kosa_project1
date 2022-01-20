package com.mopp.verticalmenu.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.mopp.login.controllers.DatabaseJob;
import com.mopp.login.controllers.MainController;
import com.mopp.profile.model.Member;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ProfileController implements Initializable {

	@FXML
	private AnchorPane profilePane;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DatabaseJob dbJob = new DatabaseJob();
		try {
			Member userInfo = dbJob.getUserInfo(MainController.memNo);

			Date birthDayUser = userInfo.getBirthday();
			LocalDate birth = LocalDate.of(birthDayUser.getYear()+1900, birthDayUser.getMonth()+1, birthDayUser.getDate());

			txtMemberUserId.setText(userInfo.getMemId());
			txtMemberName.setText(userInfo.getName());
			txtMemberTel.setText(userInfo.getTel());
			txtMemberEmail1.setText(userInfo.getEmail1());
			buttonMemberEmail2.setText(userInfo.getEmail2());
			birthDay.setValue(birth);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

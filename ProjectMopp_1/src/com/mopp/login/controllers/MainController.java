package com.mopp.login.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mopp.prescription.dao.PrescriptionHistoryDao;
import com.mopp.prescription.model.Prescription;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	private AnchorPane homePane;

	@FXML
	private TableView<Prescription> rxList;

	@FXML
	private TableColumn<Prescription, Integer> rxNo;

	@FXML
	private TableColumn<Prescription, String> rxDate;

	@FXML
	private TableColumn<Prescription, String> rxMedicineName;

	@FXML
	private TableColumn<Prescription, String> rxSymptoms;

	public static int memNo;

	DatabaseJob dbJob = new DatabaseJob();
	PrescriptionHistoryDao dao = new PrescriptionHistoryDao();

	final WebView browser = new WebView();
	final WebEngine web = browser.getEngine();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rxNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		rxDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
		rxMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicine"));
		rxSymptoms.setCellValueFactory(new PropertyValueFactory<>("symptoms"));

		List<Prescription> preList = getPre(memNo);
		for (Prescription pre : preList) {
			rxList.getItems().add(pre);
		}
	}

	// back to previous page
	@FXML
	private void over_to_supplement(ActionEvent e) throws IOException {
//		VBox vbox = new VBox();
//		Scene scene = new Scene(vbox);
//		Stage primaryStage = (Stage) homePane.getScene().getWindow();
//		web.load("https://pilly.kr/");
//		vbox.getChildren().add(browser);
//		VBox.setVgrow(browser, Priority.ALWAYS);

		Stage primaryStage = (Stage) homePane.getScene().getWindow();

		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/survey/views/root.fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// init시 prescription history 출력
	private List<Prescription> getPre(int memNo) {
		List<Prescription> preList = new ArrayList<>();
		preList = dao.getPreMemList(memNo);
		return preList;
	}
	

}

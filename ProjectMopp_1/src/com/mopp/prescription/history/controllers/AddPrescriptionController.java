package com.mopp.prescription.history.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mopp.login.controllers.MainController;
import com.mopp.prescription.dao.PrescriptionDao;
import com.mopp.prescription.dao.PrescriptionHistoryDao;
import com.mopp.prescription.model.Medicine;
import com.mopp.prescription.model.Prescription;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class AddPrescriptionController implements Initializable {

	@FXML
	private StackPane parentContainer;
	@FXML
	private AnchorPane container;
	@FXML
	private HBox submitButton;
	@FXML
	private HBox addMedButton;
	@FXML
	private HBox backButton;

	// 약품
	@FXML
	private ComboBox<String> med1;
	@FXML
	private ComboBox<String> med2;
	@FXML
	private ComboBox<String> med3;

	@FXML
	private HBox saHB;
	@FXML
	private HBox meHB;
	@FXML
	private HBox dtHB;
	@FXML
	private HBox dtHB2;

	@FXML
	private TextField day;
	@FXML
	private TextField time;

	@FXML
	private TextArea saTxt;

	ObservableList<String> list = FXCollections.observableArrayList();
	PrescriptionDao dao = new PrescriptionDao();
	PrescriptionHistoryDao historyDao = new PrescriptionHistoryDao();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		getMedInfo();
		med1.setItems(list);
		med2.setItems(list);
		med3.setItems(list);
	}

	private void getMedInfo() {
		List<Medicine> medList = dao.allMedicine();
		String name = "";
		list.add("Select");
		for (Medicine med : medList) {
			name = med.getMedNo() + "  " + med.getMedName();
			list.add(name);
		}
	}

	// go to submit alert
	@FXML
	private void open_submit_add_prescription_form(MouseEvent event) throws IOException {
		if (medIsNull() == true) {
			meHB.setVisible(true);
			if (saTxt.getText().length() == 0) {
				saHB.setVisible(true);
			} else {
				saHB.setVisible(false);
			}
		} else if (saTxt.getText().length() == 0) {
			meHB.setVisible(false);
			saHB.setVisible(true);
		} else {
			meHB.setVisible(false);
			saHB.setVisible(false);
		}

		if (!day.getText().equals("") || !time.getText().equals("")) {
			dtHB.setVisible(false);
		} else {
			dtHB.setVisible(true);
		}
		if (isInt(day.getText()) == true || isInt(time.getText()) == true) {
			dtHB2.setVisible(true);
		} else {
			dtHB2.setVisible(false);
		}

		if (!saHB.isVisible() && !meHB.isVisible() && !dtHB.isVisible() && !dtHB2.isVisible()) {

			List<Integer> medList = new ArrayList<Integer>();
			Prescription pre = new Prescription();
			pre.setMemNo(MainController.memNo);
			pre.setSymptoms(saTxt.getText());

			int medNo = 0;
			if (!med1.getValue().equals("Select")) {
				medNo = Integer.parseInt(med1.getValue().substring(0, 3).trim());
				medList.add(medNo);
			}
			if (!med2.getValue().equals("Select")) {
				medNo = Integer.parseInt(med2.getValue().substring(0, 3).trim());
				medList.add(medNo);
			}
			if (!med3.getValue().equals("Select")) {
				medNo = Integer.parseInt(med3.getValue().substring(0, 3).trim());
				medList.add(medNo);
			}

			dao.createPre(pre, medList);

			alert(historyDao.getMemName(MainController.memNo), "처방전", "create");
		}
	}

	// alert(고객 이름, 어떤 작업인지, CRUD택1)
	private void alert(String name, String mode, String how) throws IOException {
		String headerText = "";
		String contextText = "";
		headerText += name + "님, " + mode;
		Alert alert = null;
		switch (how) {
		case "create":
			alert = new Alert(AlertType.CONFIRMATION);
			headerText += " 저장이 완료되었습니다.";
			contextText += "추가하시겠습니까?";
			break;
		case "update":
			alert = new Alert(AlertType.CONFIRMATION);
			headerText += " 수정이 완료되었습니다.";
			contextText += "확인하시겠습니까?";
			break;
		case "delete":
			alert = new Alert(AlertType.CONFIRMATION);
			headerText += " 삭제하시겠습니까?";
			break;
		default:
			alert = new Alert(AlertType.WARNING);
			headerText += " 비정상적인 접근입니다.";
			break;
		}
		alert.setTitle("Complete");
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);

		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

		switch (how) {
		case "create":
			if (ButtonType.OK.equals(result)) {
				addPrescription();
			} else if (ButtonType.CANCEL.equals(result)) {
				prescriptionHistory();
			}
			break;
		case "update":
			if (ButtonType.OK.equals(result)) {
			} else if (ButtonType.CANCEL.equals(result)) {
				prescriptionHistory();
			}
			break;
		case "delete":
			if (ButtonType.OK.equals(result)) {
				prescriptionHistory();
			}
			break;
		default:
			prescriptionHistory();
			break;
		}

	}

	// prescription 추가
	private void addPrescription() throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/AddPrescription.fxml"));
		Scene scene = new Scene(root);
		root.translateYProperty().set(scene.getHeight());
		parentContainer.getChildren().add(root);
	}

	// go to prescription history
	private void prescriptionHistory() throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/PrescriptionHistory.fxml"));
		Scene scene = new Scene(root);
		root.translateYProperty().set(scene.getHeight());
		parentContainer.getChildren().add(root);
	}

	// back to previous page
	@FXML
	private void back_prescription_history_form(MouseEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/PrescriptionHistory.fxml"));

		Scene scene = backButton.getScene();

		root.translateYProperty().set(scene.getHeight());

		parentContainer.getChildren().add(root);

		Timeline timeline = new Timeline();
		KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
		KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
		timeline.getKeyFrames().add(kf);
		timeline.setOnFinished(event1 -> {
			parentContainer.getChildren().remove(container);
		});
		timeline.play();
	}

//	medicine이 선택되었는지 확인
	private boolean medIsNull() {
		ArrayList<ComboBox<String>> medList = new ArrayList<ComboBox<String>>();
		ArrayList<Boolean> resultList = new ArrayList<Boolean>();
		medList.add(med1);
		medList.add(med2);
		medList.add(med3);

		boolean result = true;
		for (ComboBox<String> med : medList) {
			if (!med.getValue().equals("Select")) {
				resultList.add(false);
				result = false;
				break;
			} else {
				result = true;
				resultList.add(true);
			}
		}
		return result;
	}

	private boolean isInt(String args) {
		boolean result = false;
		char tmp;

		for (int i = 0; i < args.length(); i++) {
			tmp = args.charAt(i);
			if (!('0' <= tmp && tmp <= '9')) {
				result = true;
				break;
			} else {
				result = false;
			}
		}
		return result;
	}

}

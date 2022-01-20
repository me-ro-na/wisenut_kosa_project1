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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class UpdatePrescriptionController implements Initializable {

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

	@FXML
	private Label title;

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
	private ToggleGroup isTaking;
	@FXML
	private RadioButton radio1;
	@FXML
	private RadioButton radio2;

	@FXML
	private TextArea saTxt;

	@FXML
	private TextField day;
	@FXML
	private TextField time;

	ObservableList<String> list = FXCollections.observableArrayList();
	PrescriptionHistoryDao historyDao = new PrescriptionHistoryDao();
	PrescriptionDao dao = new PrescriptionDao();

	private static int med1Org = 0;
	private static int med2Org = 0;
	private static int med3Org = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		getMedInfo();
		med1.setItems(list);
		med2.setItems(list);
		med3.setItems(list);
		radio1.setSelected(true);
		radio2.setSelected(false);

		setInit();
		setName();

		if (!med1.getValue().equals("Select")) {
			med1Org = Integer.parseInt(med1.getValue().substring(0, 3).trim());
		}
		if (!med2.getValue().equals("Select")) {
			med2Org = Integer.parseInt(med2.getValue().substring(0, 3).trim());
		}
		if (!med3.getValue().equals("Select")) {
			med3Org = Integer.parseInt(med3.getValue().substring(0, 3).trim());
		}
	}

	// 사용자 이름 배치
	private void setName() {
		String userName = null;
		userName = historyDao.getMemName(MainController.memNo);
		title.setText(userName + "님의 처방전");
	}

	private void setInit() {
		Prescription pre = historyDao.getPrescription(PrescriptionHistoryController.rxId);
		saTxt.setText(pre.getSymptoms());
		List<Medicine> medList = dao.rxMed(PrescriptionHistoryController.rxId);
		List<String> selList = new ArrayList<String>();
		for (Medicine med : medList) {
			String selected = med.getMedNo() + "  " + med.getMedName();
			selList.add(selected);
		}

		switch (selList.size()) {
		case 1:
			med1.setValue(selList.get(0));
			break;
		case 2:
			med1.setValue(selList.get(0));
			med2.setValue(selList.get(1));
			break;
		case 3:
			med1.setValue(selList.get(0));
			med2.setValue(selList.get(1));
			med3.setValue(selList.get(2));
			break;
		default:
			break;
		}

		if (pre.getIsTaking().equals("복용중")) {
			radio1.setSelected(true);
		} else {
			radio2.setSelected(true);
		}
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

			int[] medListNew = { 0, 0, 0 };
			int[] medListOrg = { 0, 0, 0 };
			Prescription pre = new Prescription();
			pre.setRxID(PrescriptionHistoryController.rxId);
			pre.setMemNo(MainController.memNo);
			pre.setSymptoms(saTxt.getText());
			pre.setIsTaking(radioBtnChk());

			int medNo = 0;

			List<Integer> haveNewMedList = new ArrayList<Integer>();
			List<Integer> haveDelMedList = new ArrayList<Integer>();
			if (!med1.getValue().equals("Select")) {
				medNo = Integer.parseInt(med1.getValue().substring(0, 3).trim());
				if (medNo != med1Org) {
					if (med1Org == 0) {
						haveNewMedList.add(medNo);
					} else {
						medListNew[0] = medNo;
						medListOrg[0] = med1Org;
					}
				}
			} else {
				if (med1Org > 0) {
					haveDelMedList.add(med1Org);
				}
			}
			if (!med2.getValue().equals("Select")) {
				medNo = Integer.parseInt(med2.getValue().substring(0, 3).trim());
				if (medNo != med2Org) {
					if (med2Org == 0) {
						haveNewMedList.add(medNo);
					} else {
						medListNew[1] = medNo;
						medListOrg[1] = med2Org;
					}
				}
			} else {
				if (med2Org > 0) {
					haveDelMedList.add(med2Org);
				}
			}
			if (!med3.getValue().equals("Select")) {
				medNo = Integer.parseInt(med3.getValue().substring(0, 3).trim());
				if (medNo != med3Org) {
					if (med3Org == 0) {
						haveNewMedList.add(medNo);
					} else {
						medListNew[2] = medNo;
						medListOrg[2] = med3Org;
					}
				}
			} else {
				if (med3Org > 0) {
					haveDelMedList.add(med3Org);
				}
			}

			dao.updatePre(pre, medListNew, medListOrg, haveDelMedList, haveNewMedList);
			alert(getName());
		}
	}

	private String getName() {
		String result = historyDao.getMemName(MainController.memNo);

		return result;
	}

	// alert(고객 이름, 어떤 작업인지, CRUD택1)
	private void alert(String name) throws IOException {
		String headerText = name + "님, 처방전 수정이 완료되었습니다.";
		String contextText = "확인하시겠습니까?";
		Alert alert = null;
		alert = new Alert(AlertType.CONFIRMATION);

		alert.setTitle("Complete");
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);

		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
		viewPrescription();
		if (ButtonType.OK.equals(result)) {
		} else if (ButtonType.CANCEL.equals(result)) {
			prescriptionHistory();
		}
	}

	// prescription 추가
	private void viewPrescription() throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/ViewPrescription.fxml"));
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

	private String radioBtnChk() {
		String result = "T";
		if (radio1.isSelected()) {
			result = "T";
		} else if (radio2.isSelected()) {
			result = "F";
		}
		return result;
	}

}

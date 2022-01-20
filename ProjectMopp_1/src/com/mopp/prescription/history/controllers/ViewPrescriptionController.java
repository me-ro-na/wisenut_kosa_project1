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
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ViewPrescriptionController implements Initializable {

	@FXML
	private StackPane parentContainer;
	@FXML
	private AnchorPane container;
	@FXML
	private HBox deleteButton;
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
	private ToggleGroup isTaking;
	@FXML
	private RadioButton radio1;
	@FXML
	private RadioButton radio2;

	@FXML
	private HBox warnButton;

	@FXML
	private TextArea saTxt;

	@FXML
	private Label mainLabel;

	ObservableList<String> list = FXCollections.observableArrayList();

	PrescriptionHistoryDao historyDao = new PrescriptionHistoryDao();
	PrescriptionDao dao = new PrescriptionDao();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		getMedInfo();
		med1.setItems(list);
		med2.setItems(list);
		med3.setItems(list);
		setInit();
		setName();
	}

	// 사용자 이름 배치
	private void setName() {
		String userName = null;
		userName = historyDao.getMemName(MainController.memNo);
		mainLabel.setText(userName + "님의 처방전");
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
		for (Medicine med : medList) {
			name = med.getMedNo() + "  " + med.getMedName();
			list.add(name);
		}
	}

	// go to 주의사항 확인
	@FXML
	private void open_warn_form(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/com/mopp/prescription/history/views/WarnMedicine.fxml"));

		Scene scene = warnButton.getScene();

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

	// go to delete page
	@FXML
	private void open_delete_prescription_form(MouseEvent event) throws IOException {
		dao.delRX(PrescriptionHistoryController.rxId);

		String headerText = getName() + "님, 처방전이 삭제되었습니다.";
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Complete");
		alert.setHeaderText(headerText);

		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

		if (ButtonType.OK.equals(result)) {
			prescriptionHistory();
		}
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

	private String getName() {
		String result = historyDao.getMemName(MainController.memNo);

		return result;
	}

	// go to prescription history
	private void prescriptionHistory() throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/PrescriptionHistory.fxml"));
		Scene scene = new Scene(root);
		root.translateYProperty().set(scene.getHeight());
		parentContainer.getChildren().add(root);
	}

	// go to update page
	@FXML
	private void open_update_prescription_form(MouseEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/UpdatePrescription.fxml"));

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
}

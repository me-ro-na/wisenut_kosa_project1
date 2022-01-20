package com.mopp.prescription.history.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mopp.login.controllers.MainController;
import com.mopp.prescription.dao.PrescriptionHistoryDao;
import com.mopp.prescription.model.Prescription;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class PrescriptionHistoryController implements Initializable {

	@FXML
	private StackPane parentContainer;
	@FXML
	private AnchorPane container;

//	@FXML
//	private HBox councelingButton;

	@FXML
	private HBox addButton;

	@FXML
	private TableView<Prescription> viewTable;

	@FXML
	private TableColumn<Prescription, String> colNo;
	@FXML
	private TableColumn<Prescription, String> colCrDate;
	@FXML
	private TableColumn<Prescription, String> colMedicine;
	@FXML
	private TableColumn<Prescription, String> colSym;
	@FXML
	private TableColumn<Prescription, String> colIsTaking;

	@FXML
	private Label title;

	private PrescriptionHistoryDao dao = new PrescriptionHistoryDao();

	@FXML
	private Label rxID;
	
	public static String rxId;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		colNo.setCellValueFactory(new PropertyValueFactory<Prescription, String>("no"));
		colCrDate.setCellValueFactory(new PropertyValueFactory<Prescription, String>("createDate"));
		colMedicine.setCellValueFactory(new PropertyValueFactory<Prescription, String>("medicine"));
		colSym.setCellValueFactory(new PropertyValueFactory<Prescription, String>("symptoms"));
		colIsTaking.setCellValueFactory(new PropertyValueFactory<Prescription, String>("isTaking"));

		List<Prescription> preList = getPre(MainController.memNo);
		for (Prescription pre : preList) {
			viewTable.getItems().add(pre);
		}

		viewTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if (!viewTable.getItems().isEmpty() && viewTable.getSelectionModel().getSelectedItem() != null) {
						rxId = viewTable.getSelectionModel().getSelectedItem().getRxID();
						open_prescription_form(event);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		setName();
	}

	// 사용자 이름 배치
	private void setName() {
		String userName = null;
		userName = dao.getMemName(MainController.memNo);
		title.setText(userName + "님의 처방전");
	}

	// go to 처방전 추가하기
	@FXML
	private void open_add_prescription_form(MouseEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/AddPrescription.fxml"));

		Scene scene = addButton.getScene();

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

	// init시 prescription history 출력
	private List<Prescription> getPre(int memNo) {
		List<Prescription> preList = new ArrayList<>();
		preList = dao.getPreMemList(memNo);
		return preList;
	}

	private void open_prescription_form(MouseEvent event) throws IOException {
		Parent root = FXMLLoader
				.load(getClass().getResource("/com/mopp/prescription/history/views/ViewPrescription.fxml"));

		Scene scene = addButton.getScene();

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

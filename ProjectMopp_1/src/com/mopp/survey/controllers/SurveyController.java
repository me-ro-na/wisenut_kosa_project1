package com.mopp.survey.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mopp.survey.model.Survey;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SurveyController implements Initializable {
	@FXML
//	private BorderPane rootPane;
	private StackPane rootPane;
	@FXML
	private TableView<Survey> tableView;
	@FXML
	private Hyperlink btnAdd;
	@FXML
	private Hyperlink btnExit;

	private ObservableList<Survey> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		list = FXCollections.observableArrayList();

		TableColumn tc = tableView.getColumns().get(0);
		tc.setCellValueFactory(new PropertyValueFactory("height"));
		tc.setStyle("-fx-alignment: CENTER;");

		tc = tableView.getColumns().get(1);
		tc.setCellValueFactory(new PropertyValueFactory("weight"));
		tc.setStyle("-fx-alignment: CENTER;");

		tc = tableView.getColumns().get(2);
		tc.setCellValueFactory(new PropertyValueFactory("drink"));
		tc.setStyle("-fx-alignment: CENTER;");

		tc = tableView.getColumns().get(3);
		tc.setCellValueFactory(new PropertyValueFactory("sleep"));
		tc.setStyle("-fx-alignment: CENTER;");

		tc = tableView.getColumns().get(4);
		tc.setCellValueFactory(new PropertyValueFactory("trouble"));
		tc.setStyle("-fx-alignment: CENTER;");

		tableView.setItems(list);

		btnAdd.setOnAction(event->handleBtnAddAction(event));
		btnExit.setOnAction(event->handleBtnHomeAction(event));
		}

	private void handleBtnHomeAction(ActionEvent event) {
		try {
		// TODO Auto-generated method stub
		Stage primaryStage = (Stage) rootPane.getScene().getWindow();
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/verticalmenu/views/main_view.fxml")));
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		}catch (IOException e) {
		}
	
	}

	private void handleBtnAddAction(ActionEvent event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnAdd.getScene().getWindow());
			dialog.setTitle("Survey");

			Parent parent = FXMLLoader.load(getClass().getResource("/com/mopp/survey/views/form.fxml"));

			Button btnFormAdd = (Button) parent.lookup("#btnFormAdd");
			btnFormAdd.setOnAction(e -> {
				TextField txtHeight = (TextField) parent.lookup("#txtHeight");
				TextField txtWeight = (TextField) parent.lookup("#txtWeight");
				TextField txtDrink = (TextField) parent.lookup("#txtDrink");
				TextField txtSleep = (TextField) parent.lookup("#txtSleep");
				TextField txtTrouble = (TextField) parent.lookup("#txtTrouble");

				list.add(new Survey(
						Integer.parseInt(txtHeight.getText()), Integer.parseInt(txtWeight.getText()),
						Integer.parseInt(txtDrink.getText()), Integer.parseInt(txtSleep.getText()),
						Integer.parseInt(txtTrouble.getText())
						));
				dialog.close();
			});
			
			
			Button btnFormCancel = (Button) parent.lookup("#btnFormCancel");
			btnFormCancel.setOnAction(e -> dialog.close());

			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
		} catch (IOException e) {
		}
	}

	
	private String fromSurveyToTrouble(int i) {
		// TODO Auto-generated method stub
		String trb_name = "";
		switch (i) {
		case 1:
			trb_name = "혈관/혈액순환";
			break;
		case 2:
			trb_name = "소화/장";
			break;
		case 3:
			trb_name = "피부";
			break;
		case 4:
			trb_name = "눈";
			break;
		case 5:
			trb_name = "뇌";
			break;
		case 6:
			trb_name = "피로";
			break;
		case 7:
			trb_name = "면역";
			break;
		case 8:
			trb_name = "뼈/관절";
			break;
		case 9:
			trb_name = "모발";
			break;
		}
		return trb_name;
	}

}

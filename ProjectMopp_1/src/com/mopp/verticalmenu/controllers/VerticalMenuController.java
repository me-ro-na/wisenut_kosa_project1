package com.mopp.verticalmenu.controllers;

import com.jfoenix.controls.JFXTabPane;
import com.mopp.login.controllers.MainController;
import com.mopp.prescription.dao.PrescriptionHistoryDao;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;

public class VerticalMenuController {

	@FXML
	private StackPane parentContainer;
	@FXML
	private JFXTabPane tabContainer;

	@FXML
	private Tab homeTab;

	@FXML
	private AnchorPane homeContainer;

	@FXML
	private Tab userProfileTab;

	@FXML
	private AnchorPane userProfileContainer;

	@FXML
	private Tab pHistoryTab;

	@FXML
	private AnchorPane pHistoryContainer;

	@FXML
	private Tab addPrescriptionTab;
	@FXML
	private AnchorPane addPrescriptionContainer;
	@FXML
	private Tab companyTab;

	@FXML
	private AnchorPane companyContainer;

	@FXML
	private Tab logoutTab;

	private double tabWidth = 90.0;
	public static int lastSelectedTabIndex = 0;

	PrescriptionHistoryDao dao = new PrescriptionHistoryDao();

	/// Life cycle

	@FXML
	public void initialize() {
		configureView();
	}

	/// Private

	private void configureView() {
		tabContainer.setTabMinWidth(tabWidth);
		tabContainer.setTabMaxWidth(tabWidth);
		tabContainer.setTabMinHeight(tabWidth);
		tabContainer.setTabMaxHeight(tabWidth);
		tabContainer.setRotateGraphic(true);

		EventHandler<Event> replaceBackgroundColorHandler = event -> {
			lastSelectedTabIndex = tabContainer.getSelectionModel().getSelectedIndex();

			Tab currentTab = (Tab) event.getTarget();
			if (currentTab.isSelected()) {
				currentTab.setStyle("-fx-background-color: -fx-focus-color;");
			} else {
				currentTab.setStyle("-fx-background-color: -fx-accent;");
			}
		};

		EventHandler<Event> logoutHandler = event -> {
			Tab currentTab = (Tab) event.getTarget();
			if (currentTab.isSelected()) {
				logoutAlert();
				tabContainer.getSelectionModel().select(lastSelectedTabIndex);
				MainController.memNo = 0;

				System.out.println("Logging out!");
				try {
					goToLogin();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		configureTab(homeTab, "MOPP", "/com/mopp/verticalmenu/resources/images/moppLogo.png", homeContainer,
				getClass().getResource("/com/mopp/verticalmenu/views/Home.fxml"), replaceBackgroundColorHandler);
		configureTab(userProfileTab, "User\nProfile", "/com/mopp/verticalmenu/resources/images/user-profile.png",
				userProfileContainer, getClass().getResource("/com/mopp/verticalmenu/views/Profile.fxml"),
				replaceBackgroundColorHandler);
		configureTab(pHistoryTab, "Prescription\nHistory", "/com/mopp/verticalmenu/resources/images/prescription.png",
				pHistoryContainer,
				getClass().getResource("/com/mopp/prescription/history/views/PrescriptionHistory.fxml"),
				replaceBackgroundColorHandler);
		configureTab(companyTab, "company", "/com/mopp/verticalmenu/resources/images/company.png", companyContainer,
				getClass().getResource("/com/mopp/verticalmenu/views/Company.fxml"), replaceBackgroundColorHandler);
		configureTab(addPrescriptionTab, "Add\nPrescription", "/com/mopp/verticalmenu/resources/images/add.png",
				addPrescriptionContainer,
				getClass().getResource("/com/mopp/prescription/history/views/AddPrescription.fxml"),
				replaceBackgroundColorHandler);
		configureTab(logoutTab, "Logout", "/com/mopp/verticalmenu/resources/images/logout.png", null, null,
				logoutHandler);

		homeTab.setStyle("-fx-background-color: -fx-focus-color;");
	}

	private void configureTab(Tab tab, String title, String iconPath, AnchorPane containerPane, URL resourceURL,
			EventHandler<Event> onSelectionChangedEvent) {
		double imageWidth = 40.0;

		ImageView imageView = new ImageView(new Image(iconPath));
		imageView.setFitHeight(imageWidth);
		imageView.setFitWidth(imageWidth);

		Label label = new Label(title);
		label.setMaxWidth(tabWidth - 20);
		label.setPadding(new Insets(5, 0, 0, 0));
		label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
		label.setTextAlignment(TextAlignment.CENTER);

		BorderPane tabPane = new BorderPane();
		tabPane.setRotate(90.0);
		tabPane.setMaxWidth(tabWidth);
		tabPane.setCenter(imageView);
		tabPane.setBottom(label);

		tab.setText("");
		tab.setGraphic(tabPane);

		tab.setOnSelectionChanged(onSelectionChangedEvent);

		if (containerPane != null && resourceURL != null) {
			try {
				Parent contentView = FXMLLoader.load(resourceURL);
				containerPane.getChildren().add(contentView);
				AnchorPane.setTopAnchor(contentView, 0.0);
				AnchorPane.setBottomAnchor(contentView, 0.0);
				AnchorPane.setRightAnchor(contentView, 0.0);
				AnchorPane.setLeftAnchor(contentView, 0.0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// alert
	private void logoutAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		String name = dao.getMemName(MainController.memNo);

		alert.setTitle("Logout");
		alert.setContentText(name + "님, 다음에 다시만나요!");

		alert.show();
	}

	// go to prescription history
	private void goToLogin() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Login.fxml"));
		Scene scene = new Scene(root);
		root.translateYProperty().set(scene.getHeight());
		parentContainer.getChildren().add(root);
	}
}

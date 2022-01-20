package com.mopp.prescription.history.controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class WarnController implements Initializable {

	@FXML
	private StackPane parentContainer;
    @FXML
    private AnchorPane container;
    @FXML
    private HBox backButton;
    
    @FXML
    private Label title;
    @FXML
    private Label symptoms;
    @FXML
    private Label dayTime;
    @FXML
    private Label isTaking;
    @FXML
    private Label warn1;
    @FXML
    private Label warn2;
    @FXML
    private Label warn3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	title.setText(getName() + "님 처방전의 주의사항");
    	setInit();
    }
    
    PrescriptionHistoryDao historyDao = new PrescriptionHistoryDao();
    PrescriptionDao dao = new PrescriptionDao();

    @FXML
    private void back_prescription_history_form(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mopp/prescription/history/views/PrescriptionHistory.fxml"));

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
    
    
    private void setInit() {
    	List<Medicine> medList = dao.rxMed(PrescriptionHistoryController.rxId);
		Prescription pre = historyDao.getPrescription(PrescriptionHistoryController.rxId);
		symptoms.setText(symptoms.getText() + "증상 : " + pre.getSymptoms());
		isTaking.setText(isTaking.getText() + "복용 여부: " + pre.getIsTaking());

		switch (medList.size()) {
		case 1:
			warn1.setText(warn1.getText() + "" + medList.get(0).getMedName() + " : " + medList.get(0).getMedWarn());
			break;
		case 2:
			warn1.setText(warn1.getText() + "" + medList.get(0).getMedName() + " : " + medList.get(0).getMedWarn());
			warn2.setText(warn2.getText() + "" + medList.get(1).getMedName() + " : " + medList.get(1).getMedWarn());
			break;
		case 3:
			warn1.setText(warn1.getText() + "" + medList.get(0).getMedName() + " : " + medList.get(0).getMedWarn());
			warn2.setText(warn2.getText() + "" + medList.get(1).getMedName() + " : " + medList.get(1).getMedWarn());
			warn3.setText(warn3.getText() + "" + medList.get(2).getMedName() + " : " + medList.get(2).getMedWarn());
			break;
		default:
			warn1.setText("");
			warn2.setText("");
			warn3.setText("");
			break;
		}
	}
	
    private String getName() {
		String result = historyDao.getMemName(MainController.memNo);
		
		return result;
	}

}

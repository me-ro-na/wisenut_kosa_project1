package com.mopp.main;

import com.mopp.login.controllers.DatabaseJob;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launch extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/mopp/login/views/Start.fxml")));

		stage.setScene(scene);
		stage.setTitle("My Own Personal Pharmacist");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		DatabaseJob.getConnection();
		super.init();
	}

	@Override
	public void stop() throws Exception {

		DatabaseJob.conn.close();
		super.stop();
	}

}

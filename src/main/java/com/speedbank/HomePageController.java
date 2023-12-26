package com.speedbank;



import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomePageController {

	private Scene scene;
	private Stage stage;
	private Parent root;


	// Event Listener on Button.onAction@FXML
	public void toAdminPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AdminLoginPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Event Listener on Button.onAction
	@FXML
	public void toUserPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SignInPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void toAboutPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AboutPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

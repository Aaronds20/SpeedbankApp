package com.speedbank;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotAdminPasswordController {
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField c_password;
	@FXML
	private TextField username;
	@FXML
	private Label invalidate;

	Alert alert = new Alert(AlertType.ERROR);
	Alert alert2 = new Alert(AlertType.CONFIRMATION);

	private Scene scene;
	private Stage stage;
	private Parent root;

	String Username, Password;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	// Event Listener on Button.onAction
	@FXML
	public void UpdateAdminPassword(ActionEvent event) throws IOException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultset1 = null;
		PreparedStatement PsInsert = null;

		if (password.getText().isBlank() || username.getText().isBlank() || c_password.getText().isBlank()) {
			invalidate.setStyle(errorMessage);

			if (password.getText().isBlank() || username.getText().isBlank() || c_password.getText().isBlank()) {
				invalidate.setText("Please enter your details");
				password.setStyle(errorStyle);
				username.setStyle(errorStyle);
				c_password.setStyle(errorStyle);
				new animatefx.animation.Shake(password).play();
				new animatefx.animation.Shake(username).play();
				new animatefx.animation.Shake(c_password).play();
			} else {
				password.setStyle(successStyle);
				username.setStyle(successStyle);
				c_password.setStyle(successStyle);
			}
		} else if (!(password.getText().equals(c_password.getText()))) {
			invalidate.setText("Password Mismatch");
			invalidate.setStyle(errorMessage);
			password.setStyle(errorStyle);
			c_password.setStyle(errorStyle);
			username.setStyle(successStyle);
			new animatefx.animation.Shake(password).play();
			new animatefx.animation.Shake(c_password).play();
			password.clear();
			c_password.clear();
		} else {
			password.setStyle(successStyle);
			username.setStyle(successStyle);
			c_password.setStyle(successStyle);

			invalidate.setText("");

			Username = username.getText();
			Password = password.getText();
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				preparedStatement1 = connection.prepareStatement("SELECT password FROM admindb WHERE username=?");
				preparedStatement1.setString(1, Username);
				resultset1 = preparedStatement1.executeQuery();

				if (resultset1.isBeforeFirst()) {
					PsInsert = connection.prepareStatement("UPDATE admindb SET password=? WHERE username=?");
					PsInsert.setString(1, Password);
					PsInsert.setString(2, Username);
					PsInsert.executeUpdate();

					alert2.setContentText("Password Updated Successfully");
					alert2.show();

					Parent root = FXMLLoader.load(getClass().getResource("AdminLoginPage.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} else {
					invalidate.setText("Admin does not exist");
					username.setStyle(errorStyle);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (resultset1 != null) {
					try {
						resultset1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (preparedStatement1 != null) {
					try {
						preparedStatement1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsInsert != null) {
					try {
						PsInsert.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void GoToAdminLoginPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AdminLoginPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

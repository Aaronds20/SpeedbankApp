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

public class AdminLoginPageController {
	@FXML
	private TextField u_name;
	@FXML
	private PasswordField psswrd;
	@FXML
	private Label invalidate;

	Alert alert = new Alert(AlertType.ERROR);

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
	public void GotoAdminPage(ActionEvent event) throws IOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;

		if (u_name.getText().isBlank() || psswrd.getText().isBlank()) {
			invalidate.setStyle(errorMessage);

			if (u_name.getText().isBlank() && psswrd.getText().isBlank()) {
				invalidate.setText("The Login fields are required!");
				u_name.setStyle(errorStyle);
				psswrd.setStyle(errorStyle);
				new animatefx.animation.Shake(u_name).play();
				new animatefx.animation.Shake(psswrd).play();
			} 
		}

		else {
			invalidate.setText("");
			u_name.setStyle(successStyle);
			psswrd.setStyle(successStyle);

			Username = u_name.getText();
			Password = psswrd.getText();

			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				preparedStatement = connection.prepareStatement("SELECT*FROM admindb WHERE username=? ");
				preparedStatement.setString(1, Username);
				resultset = preparedStatement.executeQuery();
				if (!resultset.isBeforeFirst()) {
					invalidate.setText("Admin not found in database");
					u_name.setStyle(errorStyle);
				} else {
					while (resultset.next()) {
						String retrievedpassword = resultset.getString("password");
						if (retrievedpassword.equals(Password)) {
							Alert alertc = new Alert(AlertType.INFORMATION);
							alertc.setHeaderText("Login Successful!");
							alertc.show();

							Parent root = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
							stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							invalidate.setText("Provided Password is incorrect");
							psswrd.setStyle(errorStyle);
						}
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (resultset != null) {
					try {
						resultset.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (preparedStatement != null) {
						try {
							preparedStatement.close();
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
	}

	// Event Listener on Button.onAction
	@FXML
	public void gotoHomePage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void GotoResetAdminPassword(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ForgotAdminPassword.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	@FXML
    void searchUser(ActionEvent event) {

    }
}

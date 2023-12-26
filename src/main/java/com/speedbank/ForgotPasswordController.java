package com.speedbank;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ForgotPasswordController {
	@FXML
	private TextField custid;
	@FXML
	private PasswordField passwrd;
	@FXML
	private PasswordField cpasswrd;
	@FXML
	private Label invalidateDetails;
	@FXML
	private ToggleGroup accounts;
	@FXML
	private RadioButton current;
	@FXML
	private RadioButton savings;

	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	String Customers_id,Password,Status="1";

	Alert alert2 = new Alert(AlertType.INFORMATION);
	Alert alert = new Alert(AlertType.ERROR);

	// Event Listener on Button.onAction
	@FXML
	public void OnSubmitForm(ActionEvent event) throws IOException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultset1 = null;
		PreparedStatement PsInsert = null;

		if (passwrd.getText().isBlank() || custid.getText().isBlank() || cpasswrd.getText().isBlank()) {
			invalidateDetails.setStyle(errorMessage);

			if (passwrd.getText().isBlank() || custid.getText().isBlank() || cpasswrd.getText().isBlank()) {
				invalidateDetails.setText("Please enter your details");
				passwrd.setStyle(errorStyle);
				custid.setStyle(errorStyle);
				cpasswrd.setStyle(errorStyle);
				new animatefx.animation.Shake(passwrd).play();
				new animatefx.animation.Shake(custid).play();
				new animatefx.animation.Shake(cpasswrd).play();
			} else {
				passwrd.setStyle(successStyle);
				custid.setStyle(successStyle);
				cpasswrd.setStyle(successStyle);
			}
		} else if (!(Pattern.matches("[0-9]{6}", custid.getText()))) {
			invalidateDetails.setText("The Customer ID Should be a 6 Digit Number");
			invalidateDetails.setStyle(errorMessage);
			custid.setStyle(errorStyle);
			new animatefx.animation.Shake(custid).play();
			passwrd.setStyle(successStyle);
			cpasswrd.setStyle(successStyle);
		} else if (!(passwrd.getText().equals(cpasswrd.getText()))) {
			invalidateDetails.setText("Password Mismatch");
			invalidateDetails.setStyle(errorMessage);
			passwrd.setStyle(errorStyle);
			cpasswrd.setStyle(errorStyle);
			custid.setStyle(successStyle);
			new animatefx.animation.Shake(passwrd).play();
			new animatefx.animation.Shake(cpasswrd).play();
			passwrd.clear();
			cpasswrd.clear();
		} else {
			passwrd.setStyle(successStyle);
			custid.setStyle(successStyle);
			cpasswrd.setStyle(successStyle);

			invalidateDetails.setText("");

			Customers_id = custid.getText();
			Password = passwrd.getText();
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				preparedStatement1 = connection.prepareStatement("SELECT Password FROM customerdb WHERE Cust_id=? AND Status=? ");
				preparedStatement1.setString(1, Customers_id);
				preparedStatement1.setString(2, Status);
				resultset1 = preparedStatement1.executeQuery();

				if (resultset1.isBeforeFirst()) {
					while (resultset1.next()) {
						PsInsert = connection.prepareStatement("UPDATE customerdb SET password='" + Password + "' where Cust_id=" + Customers_id);
						PsInsert.executeUpdate();

						alert2.setHeaderText("Password Updated Successfully");
						alert2.show();

						Parent root = FXMLLoader.load(getClass().getResource("SignInPage.fxml"));
						stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
					}
				} else {
					invalidateDetails.setText("User does not exist");
					custid.setStyle(errorStyle);
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

	// Event Listener on Hyperlink.onAction
	@FXML
	public void GotoSignIn(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SignInPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

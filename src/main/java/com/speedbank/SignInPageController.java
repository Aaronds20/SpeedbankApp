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
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SignInPageController {
	@FXML
	private TextField cust_id;
	@FXML
	private PasswordField password;
	@FXML
	private Button sigin;
	@FXML
	private Hyperlink signup;
	@FXML
	private Label invaliddetails;

	String Customer_id, Password,Status="1";

	Alert alert = new Alert(AlertType.ERROR);

	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	// Event Listener on Button[#sigin].onAction
	@FXML
	public void SignIn(ActionEvent event) throws IOException, InterruptedException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement PsGetName = null;
		PreparedStatement PsGetAccount = null;
		ResultSet resultset = null;
		ResultSet rsname = null;
		ResultSet rsaccount = null;

		String name = null;
		String account = null;

		if (cust_id.getText().isBlank() || password.getText().isBlank()) {
			invaliddetails.setStyle(errorMessage);

			if (cust_id.getText().isBlank() && password.getText().isBlank()) {
				invaliddetails.setText("The Login fields are required!");
				cust_id.setStyle(errorStyle);
				password.setStyle(errorStyle);
				new animatefx.animation.Shake(cust_id).play();
				new animatefx.animation.Shake(password).play();
			} 
		} else if (!(Pattern.matches("[0-9]{6}", cust_id.getText()))) {
			invaliddetails.setText("The Customer Id Should be a 6 Digit Number");
			invaliddetails.setStyle(errorMessage);
			cust_id.setStyle(errorStyle);
			password.setStyle(successStyle);
			new animatefx.animation.Shake(cust_id).play();
		}

		else {
			password.setStyle(successStyle);
			cust_id.setStyle(successStyle);
			invaliddetails.setText("");
			Customer_id = cust_id.getText();
			Password = password.getText();
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/speedbank", "root", "1234567890");
				preparedStatement = connection.prepareStatement("SELECT Password FROM customerdb WHERE Cust_id=? AND Status=? ");
				preparedStatement.setString(1, Customer_id);
				preparedStatement.setString(2, Status);
				resultset = preparedStatement.executeQuery();
				if (!resultset.isBeforeFirst()) {
					invaliddetails.setText("User not found in database");
					cust_id.setStyle(errorStyle);
					password.setStyle(errorStyle);
				} else {
					while (resultset.next()) {
						String retrievedpassword = resultset.getString("Password");
						if (retrievedpassword.equals(Password)) {
							Alert alertc = new Alert(AlertType.INFORMATION);
							alertc.setHeaderText("Login Successful!");
							alertc.show();
							PsGetName = connection.prepareStatement("SELECT Name FROM customerdb WHERE Cust_id=? ");
							PsGetName.setString(1, Customer_id);
							rsname = PsGetName.executeQuery();

							while (rsname.next()) {
								name = rsname.getString("Name");
							}

							PsGetAccount = connection
									.prepareStatement("SELECT Acc_no FROM accountsdb WHERE Cust_id=? ");
							PsGetAccount.setString(1, Customer_id);
							rsaccount = PsGetAccount.executeQuery();

							while (rsaccount.next()) {
								account = rsaccount.getString("Acc_no");
							}

							FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
							root = loader.load();

							DashboardController dashboard = loader.getController();
							dashboard.displayDetails(name, account);

							stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							invaliddetails.setText("Provided Password is incorrect");
							password.setStyle(errorStyle);
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
					if (rsaccount != null) {
						try {
							rsaccount.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (PsGetAccount != null) {
						try {
							PsGetAccount.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (preparedStatement != null) {
						try {
							preparedStatement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (PsGetAccount != null) {
						try {
							PsGetAccount.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (PsGetName != null) {
						try {
							PsGetName.close();
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

	// Event Listener on Hyperlink[#signup].onAction
	@FXML
	public void ToForgotPassword(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void GoToHomePage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void CreateNewAccount(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AccountCreation.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

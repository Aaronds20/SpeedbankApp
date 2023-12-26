package com.speedbank;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class AccountCreationController {
	@FXML
	private TextField fullname;
	@FXML
	private RadioButton male;
	@FXML
	private ToggleGroup gender;
	@FXML
	private RadioButton others;
	@FXML
	private TextField phone_no;
	@FXML
	private DatePicker dob;
	@FXML
	private TextArea address;
	@FXML
	private TextField pan_card_no;
	@FXML
	private RadioButton female;
	@FXML
	private RadioButton savings;
	@FXML
	private ToggleGroup accounts;
	@FXML
	private RadioButton current;
	@FXML
	private TextField email;
	@FXML
	private TextField d_amt;
	@FXML
	private Label dis_names;
	@FXML
	private Label dis_accnos;
	@FXML
	private Label invalidatedetails;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField c_password;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 15;");
	String errorStyle1 = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 15;");
	String successStyle1 = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	private Scene scene;
	private Stage stage;
	private Parent root; 

	Alert alert2 = new Alert(AlertType.INFORMATION);
	Alert alert = new Alert(AlertType.ERROR);

	String Name, Email, Address, PanCard, PhoneNo, CustomerId, AccountNumber, DOB, Account_type, Gender, Password,Status="1",Transac_pin;
	int Amount;
	String Account_Status = "Active",IFSCCode="SPBK0240124";

	LocalDate date = LocalDate.now();
	String Adate = date.toString();

	public void initialize() {
		pan_card_no.setOnKeyPressed(Event->{
			if (!(Pattern.matches("[A-Z0-9]{10}", pan_card_no.getText()))) {
				invalidatedetails.setText("Please enter your Pan Card Number correctly");
				pan_card_no.setStyle(errorStyle);
			}
			else {
				pan_card_no.setStyle(successStyle);
				invalidatedetails.setText("");
				PanCard = pan_card_no.getText();
			}
		});
		phone_no.setOnKeyPressed(Event->{
			if (!(Pattern.matches("[0-9]{10}", phone_no.getText()))) {
				invalidatedetails.setText("Please enter your Phone Number correctly");
				phone_no.setStyle(errorStyle);
			}
			else {
				phone_no.setStyle(successStyle);
				invalidatedetails.setText("");
				PhoneNo = phone_no.getText();
			}
		});
		email.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[a-zA-Z0-9_!#$%&*+/=?`{}~^.-]+@gmail.com+$", email.getText()))) {
				invalidatedetails.setText("Please enter your Email Id correctly");
				email.setStyle(errorStyle);
			}
			else {
				email.setStyle(successStyle);
				invalidatedetails.setText("");
				Email = email.getText();
			}
		});
		d_amt.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[1-9][0-9]*$", d_amt.getText()))){
				invalidatedetails.setText("Please enter the amount in numbers only");
				d_amt.setStyle(errorStyle);
			}
			else {
				d_amt.setStyle(successStyle);
				invalidatedetails.setText("");
				Amount = Integer.parseInt(d_amt.getText());
			}
		});
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void CreateAccount(ActionEvent event) throws IOException {

		Connection connection = null;
		PreparedStatement PsInsert1 = null;
		PreparedStatement PsInsert2 = null;
		int rsInsert1;
		int rsInsert2;

		if (fullname.getText().isEmpty() || address.getText().isEmpty() || pan_card_no.getText().isEmpty()
				|| phone_no.getText().isEmpty() || email.getText().isEmpty() || dob.getValue().toString().isEmpty()
				|| d_amt.getText().isEmpty() || password.getText().isEmpty() || c_password.getText().isEmpty()) {
			invalidatedetails.setStyle(errorMessage);

			if (fullname.getText().isEmpty() || address.getText().isEmpty() || pan_card_no.getText().isEmpty()
					|| phone_no.getText().isEmpty() || email.getText().isEmpty() || dob.getValue() == null
					|| d_amt.getText().isEmpty() || password.getText().isEmpty() || c_password.getText().isEmpty()) {
				invalidatedetails.setText("Please enter your details in the required fields");
				fullname.setStyle(errorStyle);
				address.setStyle(errorStyle1);
				pan_card_no.setStyle(errorStyle);
				phone_no.setStyle(errorStyle);
				email.setStyle(errorStyle);
				dob.setStyle(errorStyle1);
				d_amt.setStyle(errorStyle);
				password.setStyle(errorStyle);
				c_password.setStyle(errorStyle);
				new animatefx.animation.Shake(fullname).play();
				new animatefx.animation.Shake(address).play();
				new animatefx.animation.Shake(pan_card_no).play();
				new animatefx.animation.Shake(phone_no).play();
				new animatefx.animation.Shake(email).play();
				new animatefx.animation.Shake(dob).play();
				new animatefx.animation.Shake(d_amt).play();
				new animatefx.animation.Shake(password).play();
				new animatefx.animation.Shake(c_password).play();
				new animatefx.animation.Shake(male).play();
				new animatefx.animation.Shake(female).play();
				new animatefx.animation.Shake(others).play();
				new animatefx.animation.Shake(savings).play();
				new animatefx.animation.Shake(current).play();
			}
		} else if (!(password.getText().equals(c_password.getText()))) {
			invalidatedetails.setText("Password Mismatch");
			fullname.setStyle(successStyle);
			address.setStyle(successStyle1);
			pan_card_no.setStyle(successStyle);
			phone_no.setStyle(successStyle);
			email.setStyle(successStyle);
			dob.setStyle(successStyle1);
			d_amt.setStyle(successStyle);
			password.setStyle(errorStyle);
			c_password.setStyle(errorStyle);
			new animatefx.animation.Shake(password).play();
			new animatefx.animation.Shake(c_password).play();
			password.clear();
			c_password.clear();
		} else if (!(male.isSelected() | female.isSelected() | others.isSelected())) {
			invalidatedetails.setText("Please Select your Gender");
			fullname.setStyle(successStyle);
			address.setStyle(successStyle1);
			pan_card_no.setStyle(successStyle);
			phone_no.setStyle(successStyle);
			email.setStyle(successStyle);
			dob.setStyle(successStyle1);
			d_amt.setStyle(successStyle);
			password.setStyle(successStyle);
			c_password.setStyle(successStyle);
			new animatefx.animation.Shake(male).play();
			new animatefx.animation.Shake(female).play();
			new animatefx.animation.Shake(others).play();
		} else if (!(savings.isSelected() | current.isSelected())) {
			invalidatedetails.setText("Please Select the Type of Bank Account you want");
			fullname.setStyle(successStyle);
			address.setStyle(successStyle1);
			pan_card_no.setStyle(successStyle);
			phone_no.setStyle(successStyle);
			email.setStyle(successStyle);
			dob.setStyle(successStyle1);
			d_amt.setStyle(successStyle);
			password.setStyle(successStyle);
			c_password.setStyle(successStyle);
			new animatefx.animation.Shake(savings).play();
			new animatefx.animation.Shake(current).play();
		} else {
			RadioButton selectedRadioButton1 = (RadioButton) accounts.getSelectedToggle();
			Account_type = selectedRadioButton1.getText();

			RadioButton selectedRadioButton2 = (RadioButton) gender.getSelectedToggle();
			Gender = selectedRadioButton2.getText();

			fullname.setStyle(successStyle);
			address.setStyle(successStyle1);
			pan_card_no.setStyle(successStyle);
			phone_no.setStyle(successStyle);
			email.setStyle(successStyle);
			dob.setStyle(successStyle1);
			d_amt.setStyle(successStyle);
			password.setStyle(successStyle);
			c_password.setStyle(successStyle);

			invalidatedetails.setText("");

			Name = fullname.getText().strip();
			Address = address.getText();
			DOB = dob.getValue().toString();
			Password = password.getText();
			Amount = Integer.parseInt(d_amt.getText());

			try {

				long num = (long) (Math.random() * 1000000000L);
				long number1 = 100000000000L + num;
				CustomerId = Long.toString(number1);
				CustomerId = CustomerId.substring(0, Math.min(CustomerId.length(), 6));

				long first14 = (long) (Math.random() * 1000000000000000L);
				long number2 = 21000000000000000L + first14;
				AccountNumber = Long.toString(number2);
				AccountNumber = AccountNumber.substring(0, Math.min(AccountNumber.length(), 16));
				
				long number = (long) (Math.random()*90000000);
				Transac_pin=Long.toString(number);
				Transac_pin = Transac_pin.substring(0, Math.min(Transac_pin.length(), 4));

				if (Amount < 1000) {
					invalidatedetails.setText("Please enter an Amount of above Rs.1000");
					d_amt.setStyle(errorStyle);
				} else {

					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root",
							"1234567890");
					PsInsert1 = connection.prepareStatement(
							"INSERT INTO customerdb(Cust_id,Name,password,Email_id,Phone_no,Address,Pan_card_no,Dob,Gender,Status)"
									+ "VALUES(?,?,?,?,?,?,?,?,?,?)");
					PsInsert1.setString(1, CustomerId);
					PsInsert1.setString(2, Name);
					PsInsert1.setString(3, Password);
					PsInsert1.setString(4, Email);
					PsInsert1.setString(5, PhoneNo);
					PsInsert1.setString(6, Address);
					PsInsert1.setString(7, PanCard);
					PsInsert1.setString(8, DOB);
					PsInsert1.setString(9, Gender);
					PsInsert1.setString(10, Status);
					rsInsert1 = PsInsert1.executeUpdate();

					PsInsert2 = connection.prepareStatement(
							"INSERT INTO accountsdb(Cust_id,Acc_type,Acc_no,Balance,Acc_Status,Acc_Opening_Date,IFSC_Code,Transaction_pin)"
									+ "VALUES(?,?,?,?,?,?,?,?)");
					PsInsert2.setString(1, CustomerId);
					PsInsert2.setString(2, Account_type);
					PsInsert2.setString(3, AccountNumber);
					PsInsert2.setLong(4, Amount);
					PsInsert2.setString(5, Account_Status);
					PsInsert2.setString(6, Adate);
					PsInsert2.setString(7, IFSCCode);
					PsInsert2.setString(8, Transac_pin);
					rsInsert2 = PsInsert2.executeUpdate();
					alert2.setHeaderText("Account Created Successfully\n" + "Customert Id:" + CustomerId
							+ "\nAccount Number:" + AccountNumber
							+ "\nTransction Pin:" + Transac_pin);
					alert2.show();

					Parent root = FXMLLoader.load(getClass().getResource("SignInPage.fxml"));
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (PsInsert2 != null) {
					try {
						PsInsert2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsInsert1 != null) {
					try {
						PsInsert1.close();
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
	public void goToSignInPage(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SignInPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

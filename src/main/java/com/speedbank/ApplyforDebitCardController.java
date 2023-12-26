package com.speedbank;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

public class ApplyforDebitCardController {
	@FXML
	private Label invalidate;
	@FXML
	private TextField Cust_id;
	@FXML
	private TextField fullname;
	@FXML
	private TextField gender;
	@FXML
	private TextField phone_no;
	@FXML
	private TextField dob;
	@FXML
	private TextArea address;
	@FXML
	private TextField pancardno;
	@FXML
	private TextField accounts;
	@FXML
	private TextField accountno;
	@FXML
	private TextField balance;
	@FXML
	private TitledPane pane2;
	@FXML
	private CheckBox c_box;
	
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	Alert alert2 = new Alert(AlertType.CONFIRMATION);
	
	protected String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	
	String SFullName, SAccount_Number, SCust_id,Card_Number,CVV,Status="1";
	String Num1,Num2,Num3,Num4;
	String check="5";
	
	LocalDate date = LocalDate.now();
	String Sdate = date.toString();
	LocalDate Date = date.plusYears(3); 
	String Edate= Date.toString();
	
	
	// Event Listener on Button.onAction
	@FXML
	public void SubmitCardData(ActionEvent event) throws IOException {
		Connection con = null;
		PreparedStatement PsInsert1 = null;
		
		if(!(c_box.isSelected())){
			invalidate.setText("Please select the checkbox to accept the declaration");
			pane2.setStyle(errorStyle);
		}
		else {
			long number1 = (long) (Math.random()*90000000);
			Num1=Long.toString(number1);
			Num1 = Num1.substring(0, Math.min(Num1.length(), 3));
			
			long number2 = (long) (Math.random()*90000000);
			Num2=Long.toString(number2);
			Num2 = Num2.substring(0, Math.min(Num2.length(), 4));
			
			long number3 = (long) (Math.random()*90000000);
			Num3=Long.toString(number3);
			Num3 = Num3.substring(0, Math.min(Num3.length(), 4));
			
			long number4 = (long) (Math.random()*90000000);
			Num4=Long.toString(number4);
			Num4 = Num4.substring(0, Math.min(Num4.length(), 4));
			
			Card_Number=check+Num1+"-"+Num2+"-"+Num3+"-"+Num4;
			
			long num = (long) (Math.random()*90000000);
			CVV=Long.toString(num);
			CVV = CVV.substring(0, Math.min(CVV.length(), 3));
			
			try {
				 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				PsInsert1 = con.prepareStatement(
			"INSERT INTO debitcarddb(Cust_id,Card_Number,Card_Creation_Date,Card_Expiry_Date,Status,CVV)"
								+ "VALUES(?,?,?,?,?,?)");
				PsInsert1.setString(1, SCust_id);
				PsInsert1.setString(2, Card_Number);
				PsInsert1.setString(3, Sdate);
				PsInsert1.setString(4, Edate);
				PsInsert1.setString(5, Status);
				PsInsert1.setString(6, CVV);
				PsInsert1.executeUpdate();
				
				alert2.setHeaderText("Debit Card Applied Successfully");
				alert2.show();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("DebitCardMainPage.fxml"));
				root = loader.load();
				DebitCardMainPageController details = loader.getController();
				details.displayAccountDetails(SFullName, SAccount_Number);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (PsInsert1 != null) {
				try {
					PsInsert1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		}
	}
	// Event Listener on Button.onAction
	@FXML
	public void GoToDebitCardFrontPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DebitCardMainPage.fxml"));
		root = loader.load();
		DebitCardMainPageController details = loader.getController();
		details.displayAccountDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void displayAccountDetail(String sFullName2, String sAccount_Number2, String pan_card, String phone,
			String address2, String dOB2, String cust_id2, String gender2, String acc_type, String balance2) {
		fullname.setText(sFullName2);
		accountno.setText(sAccount_Number2);
		pancardno.setText(pan_card);
		phone_no.setText(phone);
		address.setText(address2);
		dob.setText(dOB2);
		Cust_id.setText(cust_id2);
		accounts.setText(acc_type);
		balance.setText(balance2);
		gender.setText(gender2);

		SFullName = fullname.getText();
		SAccount_Number = accountno.getText();
		SCust_id = Cust_id.getText();
		
	}
}

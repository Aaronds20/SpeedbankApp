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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AccountdetailsController {
	@FXML
	private TextField dis_c_id;
	@FXML
	private TextField dis_name;
	@FXML
	private TextField dis_email;
	@FXML
	private TextArea dis_add;
	@FXML
	private TextField dis_dob;
	@FXML
	private TextField dis_mobile_no;
	@FXML
	private TextField dis_acctype;
	@FXML
	private TextField dis_accno;
	@FXML
	private TextField dis_acc_date;
	@FXML
	private TextField dis_balance;
	@FXML
	private TextField dis_acstatus;
	@FXML
	private TextField dis_ifsccode;
	@FXML
	private TextField dis_pin;

	private Scene scene;
	private Stage stage;
	private Parent root;

	String FullName, Account_Number, Cust_id, Email_id, Address, Acc_status, Acc_Type, DOB, Mobile_no, Balance,Acc_Opening_Date,
	IFSC_Code,T_pin;

	public void displayAccountDetails(String name, String Account) {
		dis_name.setText(name);
		dis_accno.setText(Account);

		FullName = dis_name.getText();
		Account_Number = dis_accno.getText();

		Connection con = null;
		PreparedStatement PsAccDetails1 = null;
		ResultSet RsAccDetails1 = null;
		PreparedStatement PsAccDetails2 = null;
		ResultSet RsAccDetails2 = null;

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PsAccDetails1 = con
					.prepareStatement("SELECT Cust_id,Email_id,Address,Dob,Phone_no FROM customerdb WHERE Name=? ");
			PsAccDetails1.setString(1, FullName);
			RsAccDetails1 = PsAccDetails1.executeQuery();
			while (RsAccDetails1.next()) {
				Cust_id = RsAccDetails1.getString("Cust_id");
				Email_id = RsAccDetails1.getString("Email_id");
				Address = RsAccDetails1.getString("Address");
				DOB = RsAccDetails1.getString("Dob");
				Mobile_no = RsAccDetails1.getString("Phone_no");
			}

			PsAccDetails2 = con.prepareStatement(
					"SELECT Acc_type,Acc_Status,Balance,Acc_Opening_Date,IFSC_Code,Transaction_pin FROM accountsdb WHERE Acc_no=? ");
			PsAccDetails2.setString(1, Account_Number);
			RsAccDetails2 = PsAccDetails2.executeQuery();
			while (RsAccDetails2.next()) {
				Acc_Type = RsAccDetails2.getString("Acc_type");
				Acc_status = RsAccDetails2.getString("Acc_Status");
				Balance = RsAccDetails2.getString("Balance");
				Acc_Opening_Date = RsAccDetails2.getString("Acc_Opening_Date");
				IFSC_Code= RsAccDetails2.getString("IFSC_Code");
				T_pin= RsAccDetails2.getString("Transaction_pin");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (RsAccDetails1 != null) {
				try {
					RsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (RsAccDetails2 != null) {
					try {
						RsAccDetails2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsAccDetails1 != null) {
					try {
						PsAccDetails1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
						if (PsAccDetails2 != null) {
							try {
								PsAccDetails2.close();
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

		dis_c_id.setText(Cust_id);
		dis_email.setText(Email_id);
		dis_add.setText(Address);
		dis_dob.setText(DOB);
		dis_mobile_no.setText(Mobile_no);
		
		dis_ifsccode.setText(IFSC_Code);
		dis_acctype.setText(Acc_Type);
		dis_acc_date.setText(Acc_Opening_Date);
		dis_balance.setText("Rs."+Balance);
		dis_acstatus.setText(Acc_status);
		dis_pin.setText(T_pin);
	}
	// Event Listener on Button.onAction

	@FXML
	public void GotoDashboard(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		root = loader.load();
		DashboardController details = loader.getController();
		details.displayDetails(FullName, Account_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

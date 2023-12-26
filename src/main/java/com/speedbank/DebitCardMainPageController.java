package com.speedbank;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class DebitCardMainPageController {
	@FXML
	private Label Name;
	@FXML
	private TextField account;
	@FXML
	private Label amt;
	@FXML
	private Label card_number;
	@FXML
	private Label name;
	@FXML
	private Label exp_date;
	@FXML
	private Label cvv;
	@FXML
    private ImageView img;
	
	
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	Alert alert=new Alert(AlertType.ERROR);
	Alert alert2=new Alert(AlertType.CONFIRMATION);
	
	String SFullName, SAccount_Number, Pan_card, Phone, Address, DOB, gender, acc_type,Balance,Cust_id,Status="1",CardNumber,ExpiryDate,Cap_Name,CVV;
	
	public void displayAccountDetails(String name2, String accountNumber) {
		Name.setText(name2);
		account.setText(accountNumber);
		SFullName = Name.getText();
		SAccount_Number = account.getText();
	}

	// Event Listener on Button.onAction
	@FXML
	public void ApplyForDebitCard(ActionEvent event) throws IOException {
int row_count=0;
		
		Connection connection = null;
		PreparedStatement PsAccDetails1 = null;
		ResultSet RsAccDetails1 = null;
		PreparedStatement PsAccDetails2 = null;
		ResultSet RsAccDetails2 = null;
		PreparedStatement PsCard=null;
		ResultSet rsCard=null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PreparedStatement PsAccDetails = connection.prepareStatement("SELECT Cust_id FROM customerdb WHERE Name=? ");
			PsAccDetails.setString(1, SFullName);
			ResultSet RsAccDetails=PsAccDetails.executeQuery();
			while(RsAccDetails.next()) {
				Cust_id = RsAccDetails.getString("Cust_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PsCard = connection.prepareStatement("SELECT * FROM debitcarddb WHERE Cust_id= ? AND Status=?");
			PsCard.setString(1, Cust_id);
			PsCard.setString(2, Status);
			rsCard = PsCard.executeQuery();
			while(rsCard.next()) {
				row_count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(row_count>0) {
			alert.setHeaderText("Cannot apply for more than 1 debit card");
			alert.show();
		}
		else {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PsAccDetails1 = connection.prepareStatement("SELECT Pan_card_no,Phone_no,Address,Dob,Gender FROM customerdb WHERE Name=? ");
			PsAccDetails1.setString(1, SFullName);
			RsAccDetails1 = PsAccDetails1.executeQuery();
			while (RsAccDetails1.next()) {
				Pan_card = RsAccDetails1.getString("Pan_card_no");
				Phone = RsAccDetails1.getString("Phone_no");
				Address = RsAccDetails1.getString("Address");
				DOB = RsAccDetails1.getString("Dob");
				gender = RsAccDetails1.getString("Gender");
			}
			
			PsAccDetails2 = connection.prepareStatement("SELECT Acc_type,Balance FROM accountsdb WHERE Acc_no=? ");
			PsAccDetails2.setString(1, SAccount_Number);
			RsAccDetails2 = PsAccDetails2.executeQuery();
			while (RsAccDetails2.next()) {
				acc_type = RsAccDetails2.getString("Acc_type");
				Balance= RsAccDetails2.getString("Balance");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplyforDebitCard.fxml"));
			root = loader.load();

			ApplyforDebitCardController debitcard_c = loader.getController();
			debitcard_c.displayAccountDetail(SFullName, SAccount_Number, Pan_card, Phone, Address, DOB, Cust_id, gender,acc_type,Balance);

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			finally {
				if (rsCard != null) {
					try {
						rsCard.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (RsAccDetails1 != null) {
					try {
						RsAccDetails1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (RsAccDetails2 != null) {
					try {
						RsAccDetails1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsCard != null) {
					try {
						PsCard.close();
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
				}
				if (PsAccDetails2 != null) {
					try {
						PsAccDetails2.close();
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
	public void GoBackToDashBoard(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		root = loader.load();
		DashboardController details = loader.getController();
		details.displayDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void RevealCardDetails(ActionEvent event) {
		Connection con=null;
		PreparedStatement PsAccDetails1=null;
		ResultSet RsAccDetails1=null;
		PreparedStatement Statement1=null;
		ResultSet resultSet=null;
      Cap_Name=SFullName.toUpperCase();
		try {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
		PsAccDetails1 = con.prepareStatement("SELECT Cust_id FROM customerdb WHERE Name=? ");
		PsAccDetails1.setString(1, SFullName);
		RsAccDetails1 = PsAccDetails1.executeQuery();
		while (RsAccDetails1.next()) {
			Cust_id = RsAccDetails1.getString("Cust_id");
		}
 Statement1=con.prepareStatement("SELECT Card_Number,Card_Expiry_Date,CVV FROM debitcarddb WHERE Cust_id=? AND Status=?");
		Statement1.setString(1, Cust_id);
		Statement1.setString(2, Status);
		resultSet=Statement1.executeQuery();
		if(!resultSet.isBeforeFirst()) {
			alert.setHeaderText("No Debit Card has been applied yet");
			alert.show();
		}else {
		while(resultSet.next()) {
			CardNumber=resultSet.getString("Card_Number");
			ExpiryDate=resultSet.getString("Card_Expiry_Date");
			CVV=resultSet.getString("CVV");
		}
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails1 != null) {
				try {
					RsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (Statement1 != null) {
				try {
					Statement1.close();
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
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	}
		}
		LocalDate dates = LocalDate.parse(ExpiryDate);
		int month= dates.getMonthValue();
		String monthString=String.valueOf(month);
		
		int year=dates.getYear();
		String yearString=String.valueOf(year);
		
		String Date=monthString+"/"+yearString;
		
		card_number.setText(CardNumber);
		name.setText(Cap_Name);
		exp_date.setText(Date);
		
		img.setOnMouseEntered(Event->{ 
			cvv.setText("CVV: "+CVV);
			});
		img.setOnMouseExited(Event->{ 
			cvv.setText("");
			});
		
	}
	
	
}

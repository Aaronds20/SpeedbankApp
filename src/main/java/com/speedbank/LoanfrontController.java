package com.speedbank;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import com.speedbank.Model.LoanDetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class LoanfrontController {
	@FXML
	private Label amt;
	@FXML
	private Label name;
	@FXML
	private TextField account;
	@FXML
	private TableView<LoanDetails> loantable;
	@FXML
	private TableColumn<LoanDetails, Integer> loanid;
	@FXML
	private TableColumn<LoanDetails, String> loantype;
	@FXML
	private TableColumn<LoanDetails, Long> loanamount;
	@FXML
	private TableColumn<LoanDetails, String> Ls_date;
	@FXML
	private TableColumn<LoanDetails, String> Le_date;

	private ObservableList<LoanDetails> data;

	private Scene scene;
	private Stage stage;
	private Parent root;
	
	Alert alert=new Alert(AlertType.ERROR);
	Alert alert2=new Alert(AlertType.CONFIRMATION);
	
	LocalDateTime date = LocalDateTime.now();
	String Ldate = date.toString();
	
	String blankString = "-";

	String SFullName, SAccount_Number, Pan_card, Phone, Address, DOB, gender, acc_type, Cust_id,Status="1";
	String amount = null;
	
	public void initialize() {
		loantable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
	}
	
	public void displayAccountDetails(String Name, String Account) {
		name.setText(Name);
		account.setText(Account);
		SFullName = name.getText();
		SAccount_Number = account.getText();
	}

	// Event Listener on Button.onAction
	@FXML
	public void ApplyForLoan(ActionEvent event) throws IOException {

		int row_count=0;
		
		Connection connection = null;
		PreparedStatement PsAccDetails1 = null;
		ResultSet RsAccDetails1 = null;
		PreparedStatement PsAccDetails2 = null;
		ResultSet RsAccDetails2 = null;
		PreparedStatement PsAccDetails=null;
		ResultSet RsAccDetails=null;
		PreparedStatement PsLoan=null;
		ResultSet rsLoan=null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PsAccDetails = connection.prepareStatement("SELECT Cust_id FROM customerdb WHERE Name=? ");
			PsAccDetails.setString(1, SFullName);
			RsAccDetails=PsAccDetails.executeQuery();
			while(RsAccDetails.next()) {
				Cust_id = RsAccDetails.getString("Cust_id");
			}
			PsLoan = connection.prepareStatement("SELECT * FROM loan_db WHERE Cust_id= ? AND Status=?");
			PsLoan.setString(1, Cust_id);
			PsLoan.setString(2, Status);
			rsLoan = PsLoan.executeQuery();
			while(rsLoan.next()) {
				row_count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (rsLoan!= null) {
				try {
					rsLoan.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails!= null) {
				try {
					RsAccDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsAccDetails!= null) {
				try {
					PsAccDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsLoan!= null) {
				try {
					PsLoan.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection!= null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(row_count>0) {
			alert.setHeaderText("Cannot apply for more than 1 loan");
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

			PsAccDetails2 = connection.prepareStatement("SELECT Acc_type FROM accountsdb WHERE Acc_no=? ");
			PsAccDetails2.setString(1, SAccount_Number);
			RsAccDetails2 = PsAccDetails2.executeQuery();
			while (RsAccDetails2.next()) {
				acc_type = RsAccDetails2.getString("Acc_type");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Loan.fxml"));
			root = loader.load();

			LoanController loancreate = loader.getController();
			loancreate.displayAccountDetails(SFullName, SAccount_Number, Pan_card, Phone, Address, DOB, Cust_id, gender,acc_type);

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (RsAccDetails1!= null) {
				try {
					RsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails2!= null) {
				try {
					RsAccDetails2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsAccDetails1!= null) {
				try {
					PsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsAccDetails2!= null) {
				try {
					PsAccDetails2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection!= null) {
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
	public void RevealLoanDetails(ActionEvent event) {
		
		Connection con=null;
		PreparedStatement PsAccDetails1=null;
		PreparedStatement PsLoanDetails=null;
		ResultSet RsAccDetails1=null;
		ResultSet rSet=null;
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
		    PsAccDetails1 = con.prepareStatement("SELECT Cust_id FROM customerdb WHERE Name=? ");
			PsAccDetails1.setString(1, SFullName);
			RsAccDetails1 = PsAccDetails1.executeQuery();
			while (RsAccDetails1.next()) {
				Cust_id = RsAccDetails1.getString("Cust_id");
			}
			data = FXCollections.observableArrayList();
			PsLoanDetails = con.prepareStatement("SELECT * FROM loan_db WHERE Cust_id= ? AND Status=?");
			PsLoanDetails.setString(1, Cust_id);
			PsLoanDetails.setString(2, Status);
			rSet = PsLoanDetails.executeQuery();
			if(!rSet.isBeforeFirst()) {
				alert.setHeaderText("No Loan has been applied yet");
				alert.show();
			}else {
			while (rSet.next()) {
		data.add(new LoanDetails(rSet.getInt(1), rSet.getString(2), rSet.getLong(3), rSet.getString(4),rSet.getString(5)));
			}
			}
		}
		catch (SQLException e) {

			e.printStackTrace();
		}
		finally {
			if (rSet != null) {
				try {
					rSet.close();
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
			if (PsAccDetails1 != null) {
				try {
					PsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsLoanDetails != null) {
				try {
					PsLoanDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con!= null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
}
			}
		}
		loanid.setCellValueFactory(new PropertyValueFactory<>("loanid"));
		loantype.setCellValueFactory(new PropertyValueFactory<>("loantype"));
		loanamount.setCellValueFactory(new PropertyValueFactory<>("loanamount"));
		Ls_date.setCellValueFactory(new PropertyValueFactory<>("Ls_date"));
		Le_date.setCellValueFactory(new PropertyValueFactory<>("Le_date"));

		loantable.setItems(null);
		loantable.setItems(data);

	}
	
	@FXML
	public void PayLoan(ActionEvent event) {
		
		Connection con = null;
		PreparedStatement PsAccDetails1 = null;
		ResultSet RsAccDetails1 = null;
		PreparedStatement PsAccDetails2 = null;
		ResultSet RsAccDetails2 = null;
		PreparedStatement PsLoanDetails1=null;
		ResultSet rSet1=null;
		PreparedStatement PsLoanDetails=null;
		ResultSet rSet=null;
		
		Long TAmount,CBalance = null,LAmount= null;
		String CAcc_no = null ;
		int id = 0;
		try {
			 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			 PsAccDetails1 = con.prepareStatement("SELECT Cust_id FROM customerdb WHERE Name=? ");
			PsAccDetails1.setString(1, SFullName);
			RsAccDetails1 = PsAccDetails1.executeQuery();
			while (RsAccDetails1.next()) {
				Cust_id = RsAccDetails1.getString("Cust_id");
			}
			PsLoanDetails = con.prepareStatement("SELECT * FROM loan_db WHERE Cust_id= ? AND Status=?");
			PsLoanDetails.setString(1, Cust_id);
			PsLoanDetails.setString(2, Status);
			rSet = PsLoanDetails.executeQuery();
			if(!rSet.isBeforeFirst()) {
				alert.setHeaderText("No Loan has been applied yet");
				alert.show();
			}
			else {
				TextInputDialog td = new TextInputDialog();
				td.setTitle("Pay Loan");
				td.setHeaderText("Enter the Amount:");
				td.setContentText("Amount:");
				 Optional<String> result = td.showAndWait();

			        result.ifPresent(Amount -> {
			            this.amt.setText(Amount);
			            amount=amt.getText();
			        });
			        if (amount.isBlank()) {
			        	alert.setHeaderText("Please Enter an amount");
						alert.show();
					}
			        else if (!(Pattern.matches("^[0-9]*$", amount))) {
			        	alert.setHeaderText("Please enter numbers only");
						alert.show();
					}
			        else {
			         TAmount=Long.parseLong(amount);
			  PsAccDetails2 = con.prepareStatement("SELECT Balance,Acc_no FROM accountsdb WHERE Cust_id= ?");
				PsAccDetails2.setString(1, Cust_id);
				RsAccDetails2 = PsAccDetails2.executeQuery();
				while (RsAccDetails2.next()) {
					CBalance = RsAccDetails2.getLong("Balance");
					CAcc_no=RsAccDetails2.getString("Acc_no");
				}
				
				PsLoanDetails1 = con.prepareStatement("SELECT Loan_amount,Loan_id FROM loan_db WHERE Cust_id= ?");
				PsLoanDetails1.setString(1, Cust_id);
				rSet1 = PsLoanDetails1.executeQuery();
				while (rSet1.next()) {
					LAmount = rSet1.getLong("Loan_amount");
					id=rSet1.getInt("Loan_id");
				}
				
				if(TAmount>CBalance) {
					alert.setHeaderText("Insufficent Balance in your account");
					alert.show();
				}
				else {
					LAmount=LAmount-TAmount;
					CBalance=CBalance-TAmount;
	PreparedStatement PsUpdate1  = con.prepareStatement("UPDATE accountsdb SET Balance='" + CBalance + "' where Cust_id=" + Cust_id);
					PsUpdate1.executeUpdate();
					
					 String Description="Loan Amount from Loan Id "+id+" debited at "+Ldate;
					
	String psmt1 = "INSERT INTO transactionsdb(Acc_no,Description,Debit,Credit,Transc_date,prev_balance,Status,Customer_ID) VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement PsInsertTransaction1 = con.prepareStatement(psmt1);
					PsInsertTransaction1.setString(1, CAcc_no);
					PsInsertTransaction1.setString(2, Description);
					PsInsertTransaction1.setString(3, amt.getText());
					PsInsertTransaction1.setString(4, blankString);
					PsInsertTransaction1.setString(5, Ldate);
					PsInsertTransaction1.setLong(6, CBalance);
					PsInsertTransaction1.setString(7, Status);
					PsInsertTransaction1.setString(8, Cust_id);
					PsInsertTransaction1.executeUpdate();
					
					if(LAmount==0) {
						String Istatus="0";
						PreparedStatement PsDelete4 = con.prepareStatement("UPDATE loan_db SET Status='" + Istatus + "' where Cust_id=" + Cust_id);
						PsDelete4.executeUpdate();
						Alert alert2=new Alert(AlertType.CONFIRMATION);
						alert2.setHeaderText("Loan paid successfully");
						alert2.show();
					}
					else {
		PreparedStatement PsUpdate2  = con.prepareStatement("UPDATE loan_db SET Loan_amount='" + LAmount + "' where Cust_id=" + Cust_id);
				PsUpdate2.executeUpdate();		
				alert2.setHeaderText("Loan amount debited successfully");
				alert2.show();
					}

				}
			}
			}

	    }catch (SQLException e) {

		}
		finally {
			if (RsAccDetails1!= null) {
				try {
					RsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails2!= null) {
				try {
					RsAccDetails2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rSet!= null) {
				try {
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rSet1!= null) {
				try {
					rSet1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsAccDetails1!= null) {
				try {
					PsAccDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsAccDetails2!= null) {
				try {
					PsAccDetails2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsLoanDetails!= null) {
				try {
					PsLoanDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsLoanDetails1!= null) {
				try {
					PsLoanDetails1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con!= null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package com.speedbank;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.control.RadioButton;

public class BanktoBankMoneyTransferController {
	@FXML
	private Label s_balance;
	@FXML
	private Label pin;
	@FXML
	private TextField from_accountno;
	@FXML
	private Label name;
	@FXML
	private Label invalidatedetails;
	@FXML
	private RadioButton savings;
	@FXML
	private ToggleGroup accounts;
	@FXML
	private RadioButton current;
	@FXML
	private TextField to_accountno;
	@FXML
	private TextField T_amount;
	@FXML
	private TextField ifsccode;
	
	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	Connection con = null;
	Connection con2 = null;
	PreparedStatement FromAccDetails1 = null;
	ResultSet RsFromAccDetails1 = null;
	PreparedStatement ToAccDetails2 = null;
	ResultSet RsToAccDetails2 = null;
	PreparedStatement PsUpdate1 = null;
	PreparedStatement PsUpdate2 = null;
	PreparedStatement PsInsertTransaction1 = null;
	PreparedStatement PsInsertTransaction2 = null;

	Alert alert2 = new Alert(AlertType.INFORMATION);
	Alert alert = new Alert(AlertType.ERROR);

	String SenderAccount, ReceiverAccount, Account_Type,IFSCCode,Status="1",CustomerID,Pin,T_pin;
	int SBalance, RBalance, Amount;

	Date date = new Date( );
	SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd 'at' hh:mm:ss a");
	String Tdate = ft.format(date).toString();

	LocalDate date2 = LocalDate.now();
	String Tdate2 = date2.toString();

	String blankString = "-";
	
	public void initialize() {
		to_accountno.setOnKeyPressed(Event->{
			if (!(Pattern.matches("[0-9]{16}", to_accountno.getText()))) {
				invalidatedetails.setText("The Account Number Should be a 16 Digit Number");
				to_accountno.setStyle(errorStyle);
			}
			else {
				to_accountno.setStyle(successStyle);
				invalidatedetails.setText("");
				ReceiverAccount = to_accountno.getText();
			}
		});
		T_amount.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[1-9][0-9]*$", T_amount.getText()))) {
				invalidatedetails.setText("Enter the transfer amount in numbers only");
				T_amount.setStyle(errorStyle);
			}
			else {
				T_amount.setStyle(successStyle);
				invalidatedetails.setText("");
				Amount = Integer.parseInt(T_amount.getText());
			}
		});
		ifsccode.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[A-Z]{4}0[A-Z0-9]{6}$", ifsccode.getText()))) {
				invalidatedetails.setText("Enter the correct IFSC Code of the Bank");
				ifsccode.setStyle(errorStyle);
			}
			else {
				ifsccode.setStyle(successStyle);
				invalidatedetails.setText("");
				IFSCCode = ifsccode.getText();
			}
		});
	}

	public void displayAccountDetails(String names, String Account) {
		name.setText(names);
		from_accountno.setText(Account);
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			FromAccDetails1 = con.prepareStatement("SELECT Balance,Cust_id,Transaction_pin FROM accountsdb WHERE Acc_no=? ");
			FromAccDetails1.setString(1, from_accountno.getText());
			RsFromAccDetails1 = FromAccDetails1.executeQuery();
			while (RsFromAccDetails1.next()) {
				SBalance = Integer.parseInt(RsFromAccDetails1.getString("Balance"));
				CustomerID=RsFromAccDetails1.getString("Cust_id");
				T_pin=RsFromAccDetails1.getString("Transaction_pin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		s_balance.setText("Rs." + SBalance);
	}

	// Event Listener on Button.onAction
	@FXML
	public void OnMoneyTransfers(ActionEvent event) throws IOException {
		SenderAccount = from_accountno.getText();

		if (to_accountno.getText().isBlank() || T_amount.getText().isBlank()|| ifsccode.getText().isBlank()) {
			invalidatedetails.setStyle(errorMessage);
			if (to_accountno.getText().isBlank() || T_amount.getText().isBlank() || ifsccode.getText().isBlank()) {
				invalidatedetails.setText("Please Fill the required fields!");
				ifsccode.setStyle(errorStyle);
				to_accountno.setStyle(errorStyle);
				T_amount.setStyle(errorStyle);
				new animatefx.animation.Shake(to_accountno).play();
				new animatefx.animation.Shake(T_amount).play();
				new animatefx.animation.Shake(ifsccode).play();
				new animatefx.animation.Shake(savings).play();
				new animatefx.animation.Shake(current).play();
			} else {
				to_accountno.setStyle(successStyle);
				T_amount.setStyle(successStyle);
				ifsccode.setStyle(successStyle);
			}
		}  else if (from_accountno.getText().equals(to_accountno.getText())) {
			invalidatedetails.setText("Sender and Receiver's Account Number cannot be the same");
			to_accountno.setStyle(errorStyle);
			T_amount.setStyle(successStyle);
			ifsccode.setStyle(successStyle);
			new animatefx.animation.Shake(to_accountno).play();
		}
		else if(ifsccode.getText().equals("SPBK0240124")) {
			invalidatedetails.setText("Please use the Local Transfer Page to perform this transaction");
			to_accountno.setStyle(successStyle);
			T_amount.setStyle(successStyle);
			ifsccode.setStyle(errorStyle);
			new animatefx.animation.Shake(ifsccode).play();
		}
		else if (!(savings.isSelected() | current.isSelected())) {
			invalidatedetails.setText("Please Select the Account Type");
			to_accountno.setStyle(successStyle);
			T_amount.setStyle(successStyle);
			ifsccode.setStyle(successStyle);
			new animatefx.animation.Shake(savings).play();
			new animatefx.animation.Shake(current).play();
		} else {
			RadioButton selectedRadioButton1 = (RadioButton) accounts.getSelectedToggle();
			Account_Type = selectedRadioButton1.getText();

			to_accountno.setStyle(successStyle);
			T_amount.setStyle(successStyle);
			ifsccode.setStyle(successStyle);

			invalidatedetails.setText("");

			String Description = "Money Transferred to " + ReceiverAccount +" with Bank Code "+ IFSCCode +" at " + Tdate;
			
			try {
				//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/genericbank", "root", "1234567890");
				ToAccDetails2 = con2.prepareStatement("SELECT Balance FROM accounts WHERE Acc_no=? AND Acc_type=? AND IFSC_Code=?");
				ToAccDetails2.setString(1, ReceiverAccount);
				ToAccDetails2.setString(2, Account_Type);
				ToAccDetails2.setString(3, IFSCCode);
				RsToAccDetails2 = ToAccDetails2.executeQuery();
				if (!RsToAccDetails2.next()) {
					invalidatedetails.setText("Account does not exist Please enter the details again");
					ifsccode.setStyle(errorStyle);
					to_accountno.setStyle(errorStyle);
					new animatefx.animation.Shake(savings).play();
					new animatefx.animation.Shake(current).play();
				} else {
					RBalance = Integer.parseInt(RsToAccDetails2.getString("Balance"));

					if (Amount > SBalance) {
						invalidatedetails.setText("Insufficient Balance in your Account");
						T_amount.setStyle(errorStyle);
					} else {
						TextInputDialog td = new TextInputDialog();
						td.setTitle("Transfer Money");
						td.setHeaderText("Enter Transaction Pin:");
						td.setContentText("Transaction Pin:");
						 Optional<String> result = td.showAndWait();

					        result.ifPresent(PIN -> {
					            this.pin.setText(PIN);
					             Pin = pin.getText();
					        });
					        if (Pin.isBlank()) {
					        	alert.setHeaderText("Please Enter the Transaction Pin");
								alert.show();
							}
					        else if (!(Pattern.matches("[0-9]{4}", Pin))) {
					        	alert.setHeaderText("Please enter numbers only");
								alert.show();
							}
					        else if(T_pin.equals(Pin)){
						SBalance = SBalance - Amount;
						RBalance = RBalance + Amount;

						PsUpdate1 = con.prepareStatement(
								"UPDATE accountsdb SET Balance='" + SBalance + "' where Acc_no=" + SenderAccount);
						PsUpdate1.executeUpdate();

						PsUpdate2 = con2.prepareStatement(
								"UPDATE accounts SET Balance='" + RBalance + "' where Acc_no=" + ReceiverAccount);
						PsUpdate2.executeUpdate();

	String psmt1 = "INSERT INTO transactionsdb(Acc_no,Description,Debit,Credit,Transc_date,prev_balance,Status,Customer_ID) VALUES(?,?,?,?,?,?,?,?)";

						PsInsertTransaction1 = con.prepareStatement(psmt1);

						PsInsertTransaction1.setString(1, SenderAccount);
						PsInsertTransaction1.setString(2, Description);
						PsInsertTransaction1.setString(3, T_amount.getText());
						PsInsertTransaction1.setString(4, blankString);
						PsInsertTransaction1.setString(5, Tdate2);
						PsInsertTransaction1.setLong(6, SBalance);
						PsInsertTransaction1.setString(7, Status);
						PsInsertTransaction1.setString(8, CustomerID);
						PsInsertTransaction1.executeUpdate();


						alert2.setHeaderText("Transaction Successful");
						alert2.show();

						String Name = name.getText();
						String Account_Number = from_accountno.getText();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
						root = loader.load();
						DashboardController details = loader.getController();
						details.displayDetails(Name, Account_Number);
						stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						scene = new Scene(root);
						stage.setScene(scene);
						stage.show();

					}
					        else {
					        	alert.setHeaderText("Invalid Transaction Pin");
								alert.show();
					        }
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (RsFromAccDetails1 != null) {
					try {
						RsFromAccDetails1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (RsToAccDetails2 != null) {
					try {
						RsToAccDetails2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (FromAccDetails1 != null) {
					try {
						FromAccDetails1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (ToAccDetails2 != null) {
					try {
						ToAccDetails2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsUpdate1 != null) {
					try {
						PsUpdate1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsUpdate2 != null) {
					try {
						PsUpdate2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsInsertTransaction1 != null) {
					try {
						PsInsertTransaction1.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (PsInsertTransaction2 != null) {
					try {
						PsInsertTransaction2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (con2 != null) {
					try {
						con2.close();
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
	public void GoToMainTransferPage(ActionEvent event) throws IOException {
		String Name = name.getText();
		String Account_Number = from_accountno.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTransferMoney.fxml"));
		root = loader.load();
		MainTransferMoneyController details = loader.getController();
		details.displayAccountDetails(Name, Account_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

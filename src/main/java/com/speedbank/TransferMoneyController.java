package com.speedbank;



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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class TransferMoneyController {
	@FXML
	private Label balance;
	@FXML
	private Label pin;
	@FXML
	private RadioButton savings;
	@FXML
	private RadioButton current;
	@FXML
	private TextField amount;
	@FXML
	private TextField from_account;
	@FXML
	private TextField to_account;
	@FXML
	private Label Name;
	@FXML
	private ToggleGroup accounts;
	@FXML
	private Label invalidatedetails;

	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	Connection con = null;
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

	String SenderAccount, ReceiverAccount, Account_Type,Status="1",acc_Status="Active",SCustomerId,RCustomerId,Pin,T_pin;
	int SBalance, RBalance, Amount;

	Date date = new Date( );
	SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd 'at' hh:mm:ss a");
	String Tdate = ft.format(date).toString();

	LocalDate date2 = LocalDate.now();
	String Tdate2 = date2.toString();

	String blankString = "-";
	
	public void initialize() {
		to_account.setOnKeyPressed(Event->{
			if (!(Pattern.matches("[0-9]{16}", to_account.getText()))) {
				invalidatedetails.setText("The Account Number Should be a 16 Digit Number");
				to_account.setStyle(errorStyle);
			}
			else {
				to_account.setStyle(successStyle);
				invalidatedetails.setText("");
				ReceiverAccount = to_account.getText();
			}
		});
		amount.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[1-9][0-9]*$", amount.getText()))) {
				invalidatedetails.setText("Enter the transfer amount in numbers only");
				amount.setStyle(errorStyle);
			}
			else {
				amount.setStyle(successStyle);
				invalidatedetails.setText("");
				Amount = Integer.parseInt(amount.getText());
			}
		});
	}

	public void displayAccountDetails(String name, String Account) {
		Name.setText(name);
		from_account.setText(Account);
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			FromAccDetails1 = con.prepareStatement("SELECT Balance,Cust_id,Transaction_pin FROM accountsdb WHERE Acc_no=? ");
			FromAccDetails1.setString(1, from_account.getText());
			RsFromAccDetails1 = FromAccDetails1.executeQuery();
			while (RsFromAccDetails1.next()) {
				SBalance = Integer.parseInt(RsFromAccDetails1.getString("Balance"));
				SCustomerId=RsFromAccDetails1.getString("Cust_id");
				T_pin=RsFromAccDetails1.getString("Transaction_pin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		balance.setText("Rs." + SBalance);
	}

	@FXML
	public void OnMoneyTransfer(ActionEvent event) throws IOException {
		SenderAccount = from_account.getText();

		if (to_account.getText().isBlank() || amount.getText().isBlank()) {
			invalidatedetails.setStyle(errorMessage);
			if (to_account.getText().isBlank() || amount.getText().isBlank()) {
				invalidatedetails.setText("Please Fill the required fields!");
				to_account.setStyle(errorStyle);
				amount.setStyle(errorStyle);
				new animatefx.animation.Shake(to_account).play();
				new animatefx.animation.Shake(amount).play();
				new animatefx.animation.Shake(savings).play();
				new animatefx.animation.Shake(current).play();
			} else {
				to_account.setStyle(successStyle);
				amount.setStyle(successStyle);
			}
		} else if (from_account.getText().equals(to_account.getText())) {
			invalidatedetails.setText("Sender and Receiver's Account Number cannot be the same");
			to_account.setStyle(errorStyle);
			amount.setStyle(successStyle);
			new animatefx.animation.Shake(to_account).play();
		}
		else if (!(savings.isSelected() | current.isSelected())) {
			invalidatedetails.setText("Please Select the Account Type");
			to_account.setStyle(successStyle);
			amount.setStyle(successStyle);
			new animatefx.animation.Shake(savings).play();
			new animatefx.animation.Shake(current).play();
		} else {
			RadioButton selectedRadioButton1 = (RadioButton) accounts.getSelectedToggle();
			Account_Type = selectedRadioButton1.getText();

			to_account.setStyle(successStyle);
			amount.setStyle(successStyle);

			invalidatedetails.setText("");

			String Description1 = "Money Transferred to " + ReceiverAccount + " on " + Tdate;
			String Description2 = "Money Received from " + SenderAccount + " on " + Tdate;

			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				ToAccDetails2 = con.prepareStatement("SELECT Balance,Cust_id FROM accountsdb WHERE Acc_no=? AND Acc_type=? AND Acc_Status=?");
				ToAccDetails2.setString(1, ReceiverAccount);
				ToAccDetails2.setString(2, Account_Type);
				ToAccDetails2.setString(3, acc_Status);
				RsToAccDetails2 = ToAccDetails2.executeQuery();
				if (!RsToAccDetails2.next()) {
					invalidatedetails.setText("Account does not exist Please enter the details again");
					to_account.setStyle(errorStyle);
					new animatefx.animation.Shake(savings).play();
					new animatefx.animation.Shake(current).play();
				} else {
					RBalance = Integer.parseInt(RsToAccDetails2.getString("Balance"));
					RCustomerId=RsToAccDetails2.getString("Cust_id");

					if (Amount > SBalance) {
						invalidatedetails.setText("Insufficient Balance in your Account");
						amount.setStyle(errorStyle);
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

						PsUpdate2 = con.prepareStatement(
								"UPDATE accountsdb SET Balance='" + RBalance + "' where Acc_no=" + ReceiverAccount);
						PsUpdate2.executeUpdate();

	String psmt1 = "INSERT INTO transactionsdb(Acc_no,Description,Debit,Credit,Transc_date,prev_balance,Status,Customer_ID) VALUES(?,?,?,?,?,?,?,?)";

						PsInsertTransaction1 = con.prepareStatement(psmt1);

						PsInsertTransaction1.setString(1, SenderAccount);
						PsInsertTransaction1.setString(2, Description1);
						PsInsertTransaction1.setString(3, amount.getText());
						PsInsertTransaction1.setString(4, blankString);
						PsInsertTransaction1.setString(5, Tdate2);
						PsInsertTransaction1.setLong(6, SBalance);
						PsInsertTransaction1.setString(7, Status);
						PsInsertTransaction1.setString(8, SCustomerId);
						PsInsertTransaction1.executeUpdate();

	String psmt2 = "INSERT INTO transactionsdb(Acc_no,Description,Debit,Credit,Transc_date,prev_balance,Status,Customer_ID) VALUES(?,?,?,?,?,?,?,?)";

						PsInsertTransaction2 = con.prepareStatement(psmt2);

						PsInsertTransaction2.setString(1, ReceiverAccount);
						PsInsertTransaction2.setString(2, Description2);
						PsInsertTransaction2.setString(3, blankString);
						PsInsertTransaction2.setString(4, amount.getText());
						PsInsertTransaction2.setString(5, Tdate2);
						PsInsertTransaction2.setLong(6, RBalance);
						PsInsertTransaction2.setString(7, Status);
						PsInsertTransaction2.setString(8, RCustomerId);
						PsInsertTransaction2.executeUpdate();

						alert2.setHeaderText("Transaction Successfull");
						alert2.show();

						String name = Name.getText();
						String Account_Number = from_account.getText();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
						root = loader.load();
						DashboardController details = loader.getController();
						details.displayDetails(name, Account_Number);
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
		String name = Name.getText();
		String Account_Number = from_account.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTransferMoney.fxml"));
		root = loader.load();
		MainTransferMoneyController details = loader.getController();
		details.displayAccountDetails(name, Account_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

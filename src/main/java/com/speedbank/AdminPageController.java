package com.speedbank;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.speedbank.Model.Accountdetails;
import com.speedbank.Model.CardDetails;
import com.speedbank.Model.Loan;
import com.speedbank.Model.Transactions;
import com.speedbank.Model.UserDetails;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class AdminPageController {
	@FXML
	private TextField filtertext;
	@FXML
	private Button deleteuser;
	@FXML
	private Button updateuser;
	@FXML
	private Button addUser;
	@FXML
	private TextArea resultArea;
	@FXML
	private TextField newEmailText;
	@FXML
	private TextField newphoneno;
	
	@FXML
	private TableView<UserDetails> UserTable;
	@FXML
	private TableColumn<UserDetails, String> usercustid;
	@FXML
	private TableColumn<UserDetails, String> fullname;
	@FXML
	private TableColumn<UserDetails, String> Pancard;
	@FXML
	private TableColumn<UserDetails, String> Email;
	@FXML
	private TableColumn<UserDetails, String> Phoneno;
	@FXML
	private TableColumn<UserDetails, String> address;
	@FXML
    private TableColumn<UserDetails, String> dob;
	@FXML
    private TableColumn<UserDetails, String> gender;
	@FXML
    private TableColumn<UserDetails, String> password;
	
	
	@FXML
	private TableView<Accountdetails> Accountstable;
	@FXML
	private TableColumn<Accountdetails, String> Acc_no;
	@FXML
	private TableColumn<Accountdetails, String> acc_type;
	@FXML
    private TableColumn<Accountdetails, String> acc_status;
	@FXML
    private TableColumn<Accountdetails, String> acc_date;
	@FXML
	private TableColumn<Accountdetails, Integer> balance;
	@FXML
    private TableColumn<Accountdetails, String> ifsccode;
	@FXML
    private TableColumn<Accountdetails, String> t_pin;
	@FXML
    private TableColumn<Accountdetails, String> cids;
	
	@FXML
    private TableView<Transactions> Transactiontable;
	@FXML
    private TableColumn<Transactions, Integer> transactionid;
	@FXML
    private TableColumn<Transactions, String> description;
	@FXML
    private TableColumn<Transactions, String> debit;
	@FXML
    private TableColumn<Transactions, String> credit;
	@FXML
    private TableColumn<Transactions, Integer> pbalance;
	@FXML
    private TableColumn<Transactions, String> t_date;
	@FXML
	private TableColumn<Transactions, String> acc;
	@FXML
	private TableColumn<Transactions, String> cid;
    
	@FXML
    private TableView<Loan> Loantable;
	@FXML
    private TableColumn<Loan, Integer> loanid;
	@FXML
    private TableColumn<Loan, String> loantype;
	@FXML
    private TableColumn<Loan, Long> loanamount;
	@FXML
    private TableColumn<Loan, String> LsDate;
	@FXML
    private TableColumn<Loan, String> LeDate;
	@FXML
    private TableColumn<Loan, String> c_id;
	
	 @FXML
	 private TableView<CardDetails> DebitCardTable;
	 @FXML
	 private TableColumn<CardDetails, String> card_number;
	 @FXML
	 private TableColumn<CardDetails, String> c_date;
	 @FXML
	 private TableColumn<CardDetails, String> e_date;
	 @FXML
	 private TableColumn<CardDetails, String> cvv;
	 @FXML
	 private TableColumn<CardDetails, String> c_ids;


	private ObservableList<UserDetails> data=FXCollections.observableArrayList();
	private ObservableList<Accountdetails> data1=FXCollections.observableArrayList();
	private ObservableList<Loan> data2=FXCollections.observableArrayList();
	private ObservableList<Transactions> data3=FXCollections.observableArrayList();
	private ObservableList<CardDetails> data4=FXCollections.observableArrayList();

	private Scene scene;
	private Stage stage;
	private Parent root;

	Alert alert = new Alert(AlertType.ERROR);

	String acc_No,Status="1",acc_Status="Active",Customer_id;
	
	public void initialize() {
		UserTable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
		Accountstable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
		Loantable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
		Transactiontable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
		DebitCardTable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
		
		Connection con=null;
		PreparedStatement PsAccDetails1=null;
		ResultSet RsAccDetails1=null;
		PreparedStatement PsAccDetails=null;
		ResultSet RsAccDetails=null;
		PreparedStatement PsLoanDetails=null;
		ResultSet rSet=null;
		PreparedStatement PsBankDetails=null; 
		ResultSet RsBankDetails=null;
		PreparedStatement PsCardDetails=null;
		ResultSet RsCardDetails=null;
		//Creation of Customer Table
		try {
		    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
			PsAccDetails1 = con.prepareStatement("SELECT * FROM customerdb WHERE Status=?");
			PsAccDetails1.setString(1, Status);
			RsAccDetails1 = PsAccDetails1.executeQuery();
			
			while (RsAccDetails1.next()) {
         data.add(new UserDetails(RsAccDetails1.getString(1), RsAccDetails1.getString(2),
		RsAccDetails1.getString(7), RsAccDetails1.getString(4), RsAccDetails1.getString(5),RsAccDetails1.getString(6),
		RsAccDetails1.getString(8),RsAccDetails1.getString(9),RsAccDetails1.getString(3)));
			} 
			usercustid.setCellValueFactory(new PropertyValueFactory<>("customerid"));
			fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
			Pancard.setCellValueFactory(new PropertyValueFactory<>("pancard"));
			Email.setCellValueFactory(new PropertyValueFactory<>("email"));
			Phoneno.setCellValueFactory(new PropertyValueFactory<>("phoneno"));
			address.setCellValueFactory(new PropertyValueFactory<>("address"));
			dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
			gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
			password.setCellValueFactory(new PropertyValueFactory<>("password"));
			UserTable.setItems(data);
			
			FilteredList<UserDetails> filtereddata=new FilteredList<>(data, b->true);
			filtertext.textProperty().addListener((observable,oldValue,newValue)->{
				filtereddata.setPredicate(UserDetails->{
					if(newValue==null||newValue.isEmpty()) {
						return true;
					}
					String lowcasefilter=newValue.toLowerCase();
					if(UserDetails.getCustomerid().indexOf(lowcasefilter)>-1) {
						return true;
					}
					else {
						return false;
					}
				});
			});
			
			SortedList<UserDetails> sortedData=new SortedList<>(filtereddata);
			sortedData.comparatorProperty().bind(UserTable.comparatorProperty());
			UserTable.setItems(sortedData);
			
		//	Creation of Accounts Table
			PsAccDetails = con.prepareStatement("SELECT * FROM accountsdb WHERE Acc_Status=?");
			PsAccDetails.setString(1, acc_Status);
			RsAccDetails = PsAccDetails.executeQuery();
			while (RsAccDetails.next()) {
		data1.add(new Accountdetails(RsAccDetails.getString(3), RsAccDetails.getString(2),
	RsAccDetails.getString(5), RsAccDetails.getString(6), RsAccDetails.getInt(4),RsAccDetails.getString(7),RsAccDetails.getString(8),RsAccDetails.getString(1)));
							} 
							Acc_no.setCellValueFactory(new PropertyValueFactory<>("acc_no"));
							acc_type.setCellValueFactory(new PropertyValueFactory<>("acc_type"));
							acc_status.setCellValueFactory(new PropertyValueFactory<>("acc_status"));
							acc_date.setCellValueFactory(new PropertyValueFactory<>("acc_opening_date"));
							balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
							ifsccode.setCellValueFactory(new PropertyValueFactory<>("ifsccode"));
							t_pin.setCellValueFactory(new PropertyValueFactory<>("t_pin"));
						
							Accountstable.setItems(data1);
							
							FilteredList<Accountdetails> filtereddata1=new FilteredList<>(data1, b->true);
							filtertext.textProperty().addListener((observable,oldValue,newValue)->{
								filtereddata1.setPredicate(Accountdetails->{
									if(newValue==null||newValue.isEmpty()) {
										return true;
									}
									String lowcasefilter=newValue.toLowerCase();
									if(Accountdetails.getCid().indexOf(lowcasefilter)>-1) {
										return true;
									}
									else {
										return false;
									}
								});
							});
							
							SortedList<Accountdetails> sortedData1=new SortedList<>(filtereddata1);
							sortedData1.comparatorProperty().bind(Accountstable.comparatorProperty());
							Accountstable.setItems(sortedData1);
							
				// Creation of Loan Table
							PsLoanDetails = con.prepareStatement("SELECT * FROM loan_db WHERE Status=?");
							PsLoanDetails.setString(1, Status);
							rSet = PsLoanDetails.executeQuery();
							while (rSet.next()) {
				data2.add(new Loan(rSet.getInt(1), rSet.getString(2), rSet.getLong(3), rSet.getString(4),rSet.getString(5),rSet.getString(6)));
							}
							
							loanid.setCellValueFactory(new PropertyValueFactory<>("loanid"));
							loantype.setCellValueFactory(new PropertyValueFactory<>("loantype"));
							loanamount.setCellValueFactory(new PropertyValueFactory<>("loanamount"));
							LsDate.setCellValueFactory(new PropertyValueFactory<>("Ls_date"));	
							LeDate.setCellValueFactory(new PropertyValueFactory<>("Le_date"));	
							Loantable.setItems(data2);
							
							FilteredList<Loan> filtereddata2=new FilteredList<>(data2, b->true);
							filtertext.textProperty().addListener((observable,oldValue,newValue)->{
								filtereddata2.setPredicate(Loan->{
									if(newValue==null||newValue.isEmpty()) {
										return true;
									}
									String lowcasefilter=newValue.toLowerCase();
									if(Loan.getC_id().indexOf(lowcasefilter)>-1) {
										return true;
									}
									else {
										return false;
									}
								});
							});
							
							SortedList<Loan> sortedData2=new SortedList<>(filtereddata2);
							sortedData2.comparatorProperty().bind(Loantable.comparatorProperty());
							Loantable.setItems(sortedData2);
						
			// Creation of transaction table
					PsBankDetails = con.prepareStatement("SELECT * FROM transactionsdb WHERE Status=?");
					PsBankDetails.setString(1, Status);
				 RsBankDetails = PsBankDetails.executeQuery();
				while (RsBankDetails.next()) {
data3.add(new Transactions(RsBankDetails.getInt(1),RsBankDetails.getString(3), RsBankDetails.getString(4),
		RsBankDetails.getString(5), RsBankDetails.getInt(6),RsBankDetails.getString(7),RsBankDetails.getString(2),RsBankDetails.getString(9)));
				}		
				transactionid.setCellValueFactory(new PropertyValueFactory<>("t_id"));
				description.setCellValueFactory(new PropertyValueFactory<>("description"));
				debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
				credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
				pbalance.setCellValueFactory(new PropertyValueFactory<>("prevbalance"));
				t_date.setCellValueFactory(new PropertyValueFactory<>("t_date"));
				acc.setCellValueFactory(new PropertyValueFactory<>("accnos"));
				
				Transactiontable.setItems(data3);
				
				FilteredList<Transactions> filtereddata3=new FilteredList<>(data3, b->true);
				filtertext.textProperty().addListener((observable,oldValue,newValue)->{
					filtereddata3.setPredicate(Transactions->{
						if(newValue==null||newValue.isEmpty()) {
							return true;
						}
						String lowcasefilter=newValue.toLowerCase();
						if(Transactions.getC_ids().indexOf(lowcasefilter)>-1) {
							return true;
						}
						else {
							return false;
						}
					});
				});
				
				SortedList<Transactions> sortedData3=new SortedList<>(filtereddata3);
				sortedData3.comparatorProperty().bind(Transactiontable.comparatorProperty());
				Transactiontable.setItems(sortedData3);
				
		// Creation of Debit Card table
		 PsCardDetails = con.prepareStatement("SELECT * FROM debitcarddb WHERE Status=?");
		PsCardDetails.setString(1, Status);
		RsCardDetails = PsCardDetails.executeQuery();
		while (RsCardDetails.next()) {
	data4.add(new CardDetails(RsCardDetails.getString(1), RsCardDetails.getString(2),
RsCardDetails.getString(3), RsCardDetails.getString(4),RsCardDetails.getString(5)));
					}		
					card_number.setCellValueFactory(new PropertyValueFactory<>("cardnumber"));
					c_date.setCellValueFactory(new PropertyValueFactory<>("c_date"));
					e_date.setCellValueFactory(new PropertyValueFactory<>("e_date"));
					cvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
					
					DebitCardTable.setItems(data4);
					
					FilteredList<CardDetails> filtereddata4=new FilteredList<>(data4, b->true);
					filtertext.textProperty().addListener((observable,oldValue,newValue)->{
						filtereddata4.setPredicate(CardDetails->{
							if(newValue==null||newValue.isEmpty()) {
								return true;
							}
							String lowcasefilter=newValue.toLowerCase();
							if(CardDetails.getC_ids().indexOf(lowcasefilter)>-1) {
								return true;
							}
							else {
								return false;
							}
						});
					});
					
					SortedList<CardDetails> sortedData4=new SortedList<>(filtereddata4);
					sortedData4.comparatorProperty().bind(DebitCardTable.comparatorProperty());
					DebitCardTable.setItems(sortedData4);
				
		} catch (SQLException e) {

			e.printStackTrace();
		}
		finally {
			if (RsCardDetails != null) {
				try {
					RsCardDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsBankDetails != null) {
				try {
					RsBankDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rSet!= null) {
				try {
					rSet .close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails!= null) {
				try {
					RsAccDetails .close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (RsAccDetails1!= null) {
				try {
					RsAccDetails1 .close();
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
			if (PsBankDetails!= null) {
				try {
					PsBankDetails.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (PsCardDetails != null) {
				try {
					PsCardDetails.close();
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
			if (PsAccDetails!= null) {
				try {
					PsAccDetails.close();
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

	// Event Listener on Button[#deleteuser].onAction
	@FXML
	public void DeleteCustomer(ActionEvent event) {
		String Istatus="0",Iatatus="Inactive";
		if (filtertext.getText().isBlank()) {
			if (filtertext.getText().isBlank()) {
				alert.setHeaderText("Enter the User Customer Id");
				alert.show();
				filtertext.requestFocus();
			}
		} else if (!(Pattern.matches("[0-9]{6}", filtertext.getText()))) {
			alert.setHeaderText("The Customer Id Should be 6 Digit Number");
			alert.show();
			filtertext.requestFocus();
		} else {
			Customer_id = filtertext.getText();

			Connection con=null;
			PreparedStatement PsAccDetails1=null;
			PreparedStatement PsAccDetails2=null;
			ResultSet RsAccDetails1=null;
			ResultSet RsAccDetails2=null;
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				PsAccDetails1 = con.prepareStatement("SELECT * FROM customerdb WHERE Cust_id=? AND Status=?");
				PsAccDetails1.setString(1, Customer_id);
				PsAccDetails1.setString(2, Status);
				PsAccDetails2 = con.prepareStatement("SELECT * FROM accountsdb WHERE Cust_id=? AND Acc_Status=?");
				PsAccDetails2.setString(1, Customer_id);
				PsAccDetails2.setString(2, acc_Status);
				RsAccDetails1 = PsAccDetails1.executeQuery();
				RsAccDetails2 = PsAccDetails2.executeQuery();
				if (!RsAccDetails1.isBeforeFirst() && !RsAccDetails2.isBeforeFirst()) {
					alert.setHeaderText("User not found in database");
					alert.show();
					filtertext.requestFocus();
				} else {
					PreparedStatement PsAccDetails3 = con
							.prepareStatement("SELECT Acc_no FROM accountsdb WHERE Cust_id=?");
					PsAccDetails3.setString(1, Customer_id);
					ResultSet RsAccDetails3 = PsAccDetails3.executeQuery();
					while (RsAccDetails3.next()) {
						acc_No = RsAccDetails3.getString("Acc_no");
					}
					PreparedStatement PsDelete1 = con.prepareStatement("UPDATE customerdb SET Status='" + Istatus + "' where Cust_id=" + Customer_id);
					PreparedStatement PsDelete2 = con.prepareStatement("UPDATE accountsdb SET Acc_Status='" + Iatatus + "' where Cust_id=" + Customer_id);
					PreparedStatement PsDelete3 = con.prepareStatement("UPDATE transactionsdb SET Status='" + Istatus + "' where Acc_no=" + acc_No);
					PreparedStatement PsDelete4 = con.prepareStatement("UPDATE loan_db SET Status='" + Istatus + "' where Cust_id=" + Customer_id);
					PreparedStatement PsDelete5 = con.prepareStatement("UPDATE debitcarddb SET Status='" + Istatus + "' where Cust_id=" + Customer_id);
					PsDelete1.executeUpdate();
					PsDelete2.executeUpdate();
					PsDelete3.executeUpdate();
					PsDelete4.executeUpdate();
					PsDelete5.executeUpdate();
					resultArea.setText("Records deleted from database for CustomerId " + Customer_id);
					filtertext.clear();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
				finally {
					if (RsAccDetails2 != null) {
						try {
							RsAccDetails2.close();
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
					if (PsAccDetails2 != null) {
						try {
							PsAccDetails2.close();
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

	// Event Listener on Button[#updateuser].onAction
	@FXML
	public void updateUserdetails(ActionEvent event) {
		String  Email, PhoneNo;
		if (filtertext.getText().isBlank()) {
			if (filtertext.getText().isBlank()) {
				alert.setHeaderText("Enter the User Customer Id");
				alert.show();
				filtertext.requestFocus();
			}
		} else if (!(Pattern.matches("[0-9]{6}", filtertext.getText()))) {
			alert.setHeaderText("The Customer Id Should be 6 Digit Number");
			alert.show();
			filtertext.requestFocus();
		} else {
			Customer_id = filtertext.getText();
			
			Connection con=null;
			PreparedStatement PsAccDetails1=null;
			ResultSet RsAccDetails1=null;

			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				PsAccDetails1 = con.prepareStatement("SELECT * FROM customerdb WHERE Cust_id=? AND Status=?");
				PsAccDetails1.setString(1, Customer_id);
				PsAccDetails1.setString(2, Status);
				RsAccDetails1 = PsAccDetails1.executeQuery();
				if (!RsAccDetails1.isBeforeFirst()) {
					alert.setHeaderText("User not found in database");
					alert.show();
					filtertext.requestFocus();
				} else {
					if(!(newphoneno.getText().isBlank() || newEmailText.getText().isBlank())){
						alert.setHeaderText("Only one field can be updated at a time");
						alert.show();
					}else {
						if (!(newEmailText.getText().isBlank())) {
						if(Pattern.matches("^[a-zA-Z0-9_!#$%&*+/=?`{}~^.-]+@gmail.com+$", newEmailText.getText())){
							Email = newEmailText.getText();
							PreparedStatement PsInsert1 = con.prepareStatement(
									"UPDATE customerdb SET Email_id='" + Email + "' where Cust_id=" + Customer_id);
							PsInsert1.executeUpdate();
							resultArea.setText("Email updated for CustomerId " + Customer_id);
							filtertext.clear();
							newEmailText.clear();
						}
							else{
								alert.setHeaderText("Enter the correct Email ID");
								alert.show();
							}
						}
						else if (!(newphoneno.getText().isBlank())) {
							if(Pattern.matches("[0-9]{10}", newphoneno.getText())) {
							PhoneNo = newphoneno.getText();
							PreparedStatement PsInsert2 = con.prepareStatement(
									"UPDATE customerdb SET Phone_no='" + PhoneNo + "' where Cust_id=" + Customer_id);
							PsInsert2.executeUpdate();
							resultArea.setText("Phone Number updated for CustomerId " + Customer_id);
							filtertext.clear();
							newphoneno.clear();
							}
							else {
								alert.setHeaderText("Enter the correct Phone Number");
								alert.show();
							}
						}
						else{
							alert.setHeaderText("Enter a value in either fields to be updated");
							alert.show();
						} 
						}
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
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


	@FXML
	public void gotoAdminPage(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Do you want to logout from the com.speedbank!");
		if (alert.showAndWait().get() == ButtonType.OK) {
			Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	@FXML
    void RefreshData(ActionEvent event) {
		Customer_id = filtertext.getText();
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank","root","1234567890");
			PreparedStatement PsAccDetails1 = con.prepareStatement("SELECT * FROM customerdb WHERE Cust_id=? AND Status=?");
			PsAccDetails1.setString(1, Customer_id);
			PsAccDetails1.setString(2, Status);
			ResultSet RsAccDetails1 = PsAccDetails1.executeQuery();
			data=FXCollections.observableArrayList();
			if(!RsAccDetails1.isBeforeFirst()) {
				initialize();
				filtertext.clear();
			}else {
				initialize();
				filtertext.clear();
			}
	}catch (SQLException e) {
		e.printStackTrace();
	}	
    }
	
@FXML
void toSummary(ActionEvent event) throws IOException {
		  Parent root = FXMLLoader.load(getClass().getResource("SummaryReport.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
	    }
}

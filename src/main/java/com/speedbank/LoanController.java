package com.speedbank;



import java.io.IOException;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

public class LoanController {
	@FXML
    private TitledPane pane1;
    @FXML
    private TitledPane pane2;
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
	private TextField job_title;
	@FXML
	private TextArea work_address;
	@FXML
	private TextField salary;
	@FXML
	private TextField accountno;
	@FXML
	private RadioButton vehicle;
	@FXML
	private ToggleGroup loantype;
	@FXML
	private RadioButton travel;
	@FXML
	private TextField loanamount;
	@FXML
	private TextField terms;
	@FXML
	private TextField accounts;
	@FXML
	private RadioButton house;
	@FXML
	private RadioButton education;
	@FXML
	private TextField Cust_id;
	@FXML
	private Label invalidate;
	@FXML
	private Label loanvalid;

	Alert alert2 = new Alert(AlertType.INFORMATION);
	Alert alert = new Alert(AlertType.ERROR);

	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 15;");
	String errorStyle1 = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 15;");
	String successStyle1 = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");
	String LoanMessage = String.format("-fx-text-fill: GREEN;");
	
	String SFullName, SAccount_Number, SCust_id, Job_Title, Work_Address, Account_type, Gender, Loan_Type,Status="1";
	int LoanAmount,E_Range,Salary,Terms;
	
	LocalDate date = LocalDate.now();
	String Ldate = date.toString();
	
	public void initialize() {
		loanamount.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^[1-9][0-9]*$", loanamount.getText()))) {
				invalidate.setText("Please enter the Loan Amount in numbers only");
				loanamount.setStyle(errorStyle);
			}
			else {
				loanamount.setStyle(successStyle);
				invalidate.setText("");
				LoanAmount = Integer.parseInt(loanamount.getText());
			}
		});
		terms.setOnKeyPressed(Event->{
			if (!(Pattern.matches("^([1-9]|[12][0-9]|3[0-6])$", terms.getText()))) {
				invalidate.setText("Please enter the months needed to payback the loan between 1 to 36");
				terms.setStyle(errorStyle);
			}
			else {
				terms.setStyle(successStyle);
				invalidate.setText("");
				Terms = Integer.parseInt(terms.getText());
			}
		});
		salary.setOnKeyPressed(Event->{
			if(!(Pattern.matches("^[1-9][0-9]*$", salary.getText()))) {
				invalidate.setText("Please Enter Salary in numbers only");
				salary.setStyle(errorStyle);
			}
			else {
				salary.setStyle(successStyle);
				invalidate.setText("");
			Salary=Integer.parseInt(salary.getText());
			if(Salary>10000 && Salary<=100000) {
					loanvalid.setText("You can apply loan upto Rs 50000");
					loanvalid.setStyle("-fx-text-fill: GREEN");
					E_Range=50000;
				}
				else if (Salary>100000 && Salary<=200000) {
					loanvalid.setText("You can apply loan upto Rs 150000");
					loanvalid.setStyle("-fx-text-fill: GREEN");
					E_Range=150000;
					
				}
				else if(Salary>200000) {
					loanvalid.setText("You can apply loan upto Rs 500000");
					loanvalid.setStyle("-fx-text-fill: GREEN");
					E_Range=500000;
				}	
				else {
					loanvalid.setText("Loan can only be applied for salary starting from above Rs.10000");
					loanvalid.setStyle("-fx-text-fill: RED");
					salary.setStyle(errorStyle);
				}
			}
		});
	}
	
	public void displayAccountDetails(String sFullName, String sAccount_Number, String pan_card, String phone,
			String address2, String dOB2, String custid, String gender2, String acc_type) {
		fullname.setText(sFullName);
		accountno.setText(sAccount_Number);
		pancardno.setText(pan_card);
		phone_no.setText(phone);
		address.setText(address2);
		dob.setText(dOB2);
		Cust_id.setText(custid);
		accounts.setText(acc_type);
		gender.setText(gender2);

		SFullName = fullname.getText();
		SAccount_Number = accountno.getText();
		SCust_id = Cust_id.getText();
	}

	// Event Listener on Button.onAction
	@FXML
	public void SubmitLoanData(ActionEvent event) throws IOException {

		Connection con = null;
		PreparedStatement PsInsert1 = null;

		if (job_title.getText().isBlank() || work_address.getText().isBlank() || loanamount.getText().isBlank()
				|| terms.getText().isBlank()||salary.getText().isBlank()) {
			invalidate.setStyle(errorMessage);
			if (job_title.getText().isBlank() || work_address.getText().isBlank() || loanamount.getText().isBlank()
					|| terms.getText().isBlank()||salary.getText().isBlank()) {
				invalidate.setText("Please Fill the required fields!");
				pane1.setStyle(errorStyle1);
				pane2.setStyle(errorStyle1);
				job_title.setStyle(errorStyle);
				work_address.setStyle(errorStyle1);
				loanamount.setStyle(errorStyle);
				terms.setStyle(errorStyle);
				salary.setStyle(errorStyle);
				new animatefx.animation.Shake(job_title).play();
				new animatefx.animation.Shake(work_address).play();
				new animatefx.animation.Shake(loanamount).play();
				new animatefx.animation.Shake(terms).play();
				new animatefx.animation.Shake(salary).play();
				new animatefx.animation.Shake(travel).play();
				new animatefx.animation.Shake(house).play();
				new animatefx.animation.Shake(education).play();
				new animatefx.animation.Shake(vehicle).play();
			} else {
				pane1.setStyle(successStyle1);
				pane2.setStyle(successStyle1);
				job_title.setStyle(successStyle);
				work_address.setStyle(successStyle1);
				loanamount.setStyle(successStyle);
				terms.setStyle(successStyle);
				salary.setStyle(successStyle);
			}
		}
	 else if (!(travel.isSelected() | house.isSelected() | education.isSelected() | vehicle.isSelected())) {
			invalidate.setText("Please select the Type of Loan you want");
			pane1.setStyle(successStyle1);
			pane2.setStyle(errorStyle1);
			job_title.setStyle(successStyle);
			work_address.setStyle(successStyle1);
			loanamount.setStyle(successStyle);
			terms.setStyle(successStyle);
			salary.setStyle(successStyle);
			new animatefx.animation.Shake(travel).play();
			new animatefx.animation.Shake(house).play();
			new animatefx.animation.Shake(education).play();
			new animatefx.animation.Shake(vehicle).play();
		} else {
			RadioButton selectedRadioButton = (RadioButton) loantype.getSelectedToggle();
			Loan_Type = selectedRadioButton.getText();

			pane1.setStyle(successStyle1);
			pane2.setStyle(successStyle1);
			job_title.setStyle(successStyle);
			work_address.setStyle(successStyle1);
			loanamount.setStyle(successStyle);
			terms.setStyle(successStyle);
			salary.setStyle(successStyle);
			invalidate.setText("");

			
			if(LoanAmount<0 || LoanAmount>E_Range) {
				invalidate.setText("Please enter the loan amount in the specified range");
				loanamount.setStyle(errorStyle);
			}
			else {
				LocalDate LEDate = date.plusMonths(Terms); 
				String LEdate= LEDate.toString();
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				PsInsert1 = con.prepareStatement(
				"INSERT INTO loan_db(Cust_id,Loan_End_Date,Loan_amount,Loan_purpose,Status,Loan_Creation_Date)"
								+ "VALUES(?,?,?,?,?,?)");
				PsInsert1.setString(1, SCust_id);
				PsInsert1.setString(2, LEdate);
				PsInsert1.setLong(3, LoanAmount);
				PsInsert1.setString(4, Loan_Type);
				PsInsert1.setString(5, Status);
				PsInsert1.setString(6, Ldate);

				PsInsert1.executeUpdate();

				alert2.setHeaderText("Loan Applied Successfully");
				alert2.show();

				FXMLLoader loader = new FXMLLoader(getClass().getResource("Loanfront.fxml"));
				root = loader.load();
				LoanfrontController details = loader.getController();
				details.displayAccountDetails(SFullName, SAccount_Number);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();

			} catch (SQLException e) {
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
	}

	// Event Listener on Button.onAction
	@FXML
	public void GoToLoanFrontPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Loanfront.fxml"));
		root = loader.load();
		LoanfrontController details = loader.getController();
		details.displayAccountDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}

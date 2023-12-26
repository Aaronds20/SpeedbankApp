package com.speedbank;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController {

	@FXML
	private Label accountget;
	@FXML
	private Label nameget;
	@FXML
	private AnchorPane a_scene;
	@FXML
	private ImageView img;
	@FXML
	private Button acc_details;
	@FXML
	private Button loan_apply;
	@FXML
	private Button t_money;
	@FXML
	private Button v_balance;
	@FXML
	private Button debit_card;

	private Scene scene;
	private Stage stage;
	private Parent root;

	Alert alert=new Alert(AlertType.ERROR);
	Alert alert2=new Alert(AlertType.CONFIRMATION);
	
	LocalDateTime date = LocalDateTime.now();
	String Ldate = date.toString();
	
	String Name, AccountNumber,Cust_id,LEdate,Status="1";
	


	public void initialize() throws FileNotFoundException {
		acc_details.setOnMouseMoved(event -> {
			Image image = new Image("/com/speedbank/assets/images/Account Details.png");
			img.setImage(image);
			new animatefx.animation.JackInTheBox(img).play();
		}
		);
		loan_apply.setOnMouseMoved(event -> {
			Image image = new Image("/com/speedbank/assets/images/Loan.png");
			img.setImage(image);
			new animatefx.animation.JackInTheBox(img).play();
		});
		t_money.setOnMouseMoved(event -> {
			Image image = new Image("/com/speedbank/assets/images/Money Transfer.png");
			img.setImage(image);
			new animatefx.animation.JackInTheBox(img).play();
		});
		v_balance.setOnMouseMoved(event -> {
			Image image = new Image("/com/speedbank/assets/images/Passbook.png");
			img.setImage(image);
			new animatefx.animation.JackInTheBox(img).play();
		}
		);
		debit_card.setOnMouseMoved(event -> {
			Image image = new Image("/com/speedbank/assets/images/Debit Card.png");
			img.setImage(image);
			new animatefx.animation.JackInTheBox(img).play();
		}
		);
		
	}

	public void displayDetails(String name, String Account) {
		nameget.setText(name);
		accountget.setText(Account);
	}

	@FXML
	public void ToViewBalanceStatement(ActionEvent event) throws IOException {
		Name = nameget.getText();
		AccountNumber = accountget.getText();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBankStatement.fxml"));
		root = loader.load();

		ViewBankStatementController v_bank = loader.getController();
		v_bank.displayAccountDetails(Name, AccountNumber);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Event Listener on Button.onAction
	@FXML
	public void ToTransferMoney(ActionEvent event) throws IOException {
		Name = nameget.getText();
		AccountNumber = accountget.getText();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainTransferMoney.fxml"));
		root = loader.load();

		MainTransferMoneyController account = loader.getController();
		account.displayAccountDetails(Name, AccountNumber);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Event Listener on Button.onAction
	@FXML
	public void ToApplyLoan(ActionEvent event) throws IOException {
		Name = nameget.getText();
		AccountNumber = accountget.getText();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Loanfront.fxml"));
		root = loader.load();

		LoanfrontController loandetails = loader.getController();
		loandetails.displayAccountDetails(Name, AccountNumber);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Event Listener on Button.onAction
	@FXML
	public void LogOut(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Do you want to logout from the application!");
		if (alert.showAndWait().get() == ButtonType.OK) {
			Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void ToAccountDetails(ActionEvent event) throws IOException {

		Name = nameget.getText();
		AccountNumber = accountget.getText();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Accountdetails.fxml"));
		root = loader.load();

		AccountdetailsController accdetails = loader.getController();
		accdetails.displayAccountDetails(Name, AccountNumber);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	   @FXML
	   public void ToDebitCard(ActionEvent event) throws IOException {
		   Name = nameget.getText();
		AccountNumber = accountget.getText();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("DebitCardMainPage.fxml"));
			root = loader.load();

			DebitCardMainPageController debitcard = loader.getController();
			debitcard.displayAccountDetails(Name, AccountNumber);

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
	    }
}

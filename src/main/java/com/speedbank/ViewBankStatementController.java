package com.speedbank;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.speedbank.Model.BankDetails;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewBankStatementController {
	@FXML
	private DatePicker s_date;
	@FXML
	private DatePicker e_date;
	@FXML
	private TableView<BankDetails> balancetable;
	@FXML
	private TableColumn<BankDetails, String> description;
	@FXML
	private TableColumn<BankDetails, String> debit;
	@FXML
	private TableColumn<BankDetails, String> credit;
	@FXML
	private TableColumn<BankDetails, Integer> balance;
	@FXML
	private Label name;
	@FXML
	private Label acc_no;
	@FXML
	private Label invalidate;

	private ObservableList<BankDetails> data;

	private Scene scene;
	private Stage stage;
	private Parent root;

	protected 
	String errorMessage = String.format("-fx-text-fill: RED;");
	String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
	String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

	Alert alert2 = new Alert(AlertType.INFORMATION);
	Alert alert = new Alert(AlertType.ERROR);

	String SFullName, SAccount_Number, S_Date, E_Date, Desc;
	
	public void initialize() {
		balancetable.getColumns().forEach(e -> {
			e.setResizable(false);
			e.setReorderable(false);
		});
	}

	// Event Listener on Button.onAction
	@FXML
	public void tobalancedetails(ActionEvent event) {

		Connection con = null;
		PreparedStatement PsBankDetails = null;
		ResultSet RsBankDetails = null;

		if (s_date.getValue() == null || e_date.getValue() == null) {
			invalidate.setStyle(errorMessage);
			if (s_date.getValue() == null || e_date.getValue() == null) {
				invalidate.setText("Please Select the transaction dates");
				s_date.setStyle(errorStyle);
				e_date.setStyle(errorStyle);
				new animatefx.animation.Shake(s_date).play();
				new animatefx.animation.Shake(e_date).play();
			} else {
				s_date.setStyle(successStyle);
				e_date.setStyle(successStyle);
			}
		} else if (s_date.getValue().compareTo(e_date.getValue())>0) {
			invalidate.setText("The Start Date cannot be greater than End Date");
			s_date.setStyle(errorStyle);
			e_date.setStyle(errorStyle);
			new animatefx.animation.Shake(s_date).play();
			new animatefx.animation.Shake(e_date).play();
		}
		else {
			s_date.setStyle(successStyle);
			e_date.setStyle(successStyle);
			invalidate.setText("");
			S_Date = s_date.getValue().toString();
			E_Date = e_date.getValue().toString();
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				data = FXCollections.observableArrayList();
				PsBankDetails = con.prepareStatement(
						"SELECT * FROM transactionsdb WHERE Transc_date BETWEEN ? AND ? AND Acc_no=?");
				PsBankDetails.setString(1, S_Date);
				PsBankDetails.setString(2, E_Date);
				PsBankDetails.setString(3, SAccount_Number);
				RsBankDetails = PsBankDetails.executeQuery();
				if(!RsBankDetails.isBeforeFirst()) {
					invalidate.setText("No Transactions have been performed between the given dates");
					s_date.setStyle(errorStyle);
					e_date.setStyle(errorStyle);
				}else {
					while (RsBankDetails.next()) {
						data.add(new BankDetails(RsBankDetails.getString(3), RsBankDetails.getString(4),
								RsBankDetails.getString(5), RsBankDetails.getInt(6)));
					}
				}
			}catch (SQLException e) {

					e.printStackTrace();
				}
			finally {
				if (RsBankDetails!= null) {
					try {
						RsBankDetails.close();
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
				if (con!= null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
				}
			description.setCellValueFactory(new PropertyValueFactory<>("description"));
			debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
			credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
			balance.setCellValueFactory(new PropertyValueFactory<>("prevbalance"));
			balancetable.setItems(null);
			balancetable.setItems(data);
	}

	// Event Listener on Button.onAction
	@FXML
	public void GoToDashboard(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		root = loader.load();
		DashboardController details = loader.getController();
		details.displayDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void displayAccountDetails(String name2, String accountNumber) {
		name.setText(name2);
		acc_no.setText(accountNumber);
		SFullName = name.getText();
		SAccount_Number = acc_no.getText();

	}
}

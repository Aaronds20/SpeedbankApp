package com.speedbank;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainTransferMoneyController {
	@FXML
	private Label name;
	@FXML
	private Label acc_no;
	@FXML
	private Button local;
	@FXML
	private Button bank;
	
	String SFullName,SAccount_Number;
	
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	public void initialize() {
		local.setOnMouseMoved(event -> {
			new animatefx.animation.GlowBackground(local, Color.BLUEVIOLET,Color.BLUEVIOLET , 1).play();
		});
		bank.setOnMouseMoved(event -> {
			new animatefx.animation.GlowBackground(bank, Color.BLUEVIOLET,Color.BLUEVIOLET , 1).play();
		});
		local.setOnMouseExited(event -> {
			new animatefx.animation.GlowBackground(local, Color.WHITE,Color.WHITE , 1).play();
		});
		bank.setOnMouseExited(event -> {
			new animatefx.animation.GlowBackground(bank, Color.WHITE,Color.WHITE , 1).play();
		});
	}
	
	public void displayAccountDetails(String name2, String accountNumber) {
		name.setText(name2);
		acc_no.setText(accountNumber);
		SFullName = name.getText();
		SAccount_Number = acc_no.getText();
	}

	// Event Listener on Button.onAction
	@FXML
	public void toDashBoard(ActionEvent event) throws IOException {
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
	public void toLocalMoneyTransferPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferMoney.fxml"));
		root = loader.load();
		TransferMoneyController details = loader.getController();
		details.displayAccountDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	// Event Listener on Button.onAction
	@FXML
	public void toBanktoBankMoneyTransferPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BanktoBankMoneyTransfer.fxml"));
		root = loader.load();
		BanktoBankMoneyTransferController details = loader.getController();
		details.displayAccountDetails(SFullName, SAccount_Number);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}

package com.speedbank;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.stage.Stage;



public class SummaryReportController {
	 @FXML
	 private PieChart piechart1;
	  @FXML
	  private PieChart piechart2;
	  
	  private Scene scene;
		private Stage stage;
		private Parent root;
	  
	private ObservableList<Data> data=FXCollections.observableArrayList();
	private ObservableList<Data> data1=FXCollections.observableArrayList();
	
	 public void buildData(){
		 
		Connection con = null;
		PreparedStatement PsAccDetails1 = null;
		ResultSet RsAccDetails1 = null;
		
		 try {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
		PsAccDetails1 = con.prepareStatement("SELECT COUNT(Loan_id),Loan_purpose FROM loan_db GROUP BY Loan_purpose");
		RsAccDetails1 = PsAccDetails1.executeQuery();
				while (RsAccDetails1.next()) {
					data.add(new PieChart.Data(RsAccDetails1.getString(2),RsAccDetails1.getInt(1)));
				}
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
				if (PsAccDetails1!= null) {
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
	 
	 public void Build(){
		 
		 Connection con = null;
		PreparedStatement PsAccDetails2 = null;
		ResultSet RsAccDetails2 = null;
			
		 try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SpeedBank", "root", "1234567890");
				 PsAccDetails2 = con.prepareStatement("SELECT COUNT(Balance),Acc_type FROM accountsdb GROUP BY Acc_type");
					RsAccDetails2 = PsAccDetails2.executeQuery();
					while (RsAccDetails2.next()) {
						data1.add(new PieChart.Data(RsAccDetails2.getString(2),RsAccDetails2.getInt(1)));
					}		
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 finally {
				if (RsAccDetails2!= null) {
					try {
						RsAccDetails2.close();
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
				if (con!= null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		 }
}
	 public void initialize() {
     buildData();
     piechart1.getData().addAll(data);
     Build();
     piechart2.getData().addAll(data1);
}
	 
	 @FXML
	 void gotoAdminPage(ActionEvent event) throws IOException {
		 Parent root = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
	    }
}

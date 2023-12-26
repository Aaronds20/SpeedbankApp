package com.speedbank.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;


public class Accountdetails {
	private final StringProperty acc_no;
	private final StringProperty acc_type;
	private final StringProperty acc_status;
	private final StringProperty acc_opening_date;
	private final IntegerProperty balance;
	private final StringProperty ifsccode;
	private final StringProperty t_pin;
	private final StringProperty cid;
	
	public Accountdetails(String acc_no, String acc_type, String acc_status, String acc_opening_date, int balance, String ifsccode,String t_pin,String cid) {
		this.acc_no = new SimpleStringProperty(acc_no);
		this.acc_type = new SimpleStringProperty(acc_type);
		this.acc_status = new SimpleStringProperty(acc_status);
		this.acc_opening_date = new SimpleStringProperty(acc_opening_date);
		this.balance = new SimpleIntegerProperty(balance);
		this.ifsccode = new SimpleStringProperty(ifsccode);
		this.t_pin = new SimpleStringProperty(t_pin);
		this.cid = new SimpleStringProperty(cid);
	}

	public StringProperty acc_noProperty() {
		return this.acc_no;
	}
	

	public String getAcc_no() {
		return this.acc_noProperty().get();
	}
	

	public void setAcc_no(final String acc_no) {
		this.acc_noProperty().set(acc_no);
	}
	

	public StringProperty acc_typeProperty() {
		return this.acc_type;
	}
	

	public String getAcc_type() {
		return this.acc_typeProperty().get();
	}
	

	public void setAcc_type(final String acc_type) {
		this.acc_typeProperty().set(acc_type);
	}
	

	public StringProperty acc_statusProperty() {
		return this.acc_status;
	}
	

	public String getAcc_status() {
		return this.acc_statusProperty().get();
	}
	

	public void setAcc_status(final String acc_status) {
		this.acc_statusProperty().set(acc_status);
	}
	

	public StringProperty acc_opening_dateProperty() {
		return this.acc_opening_date;
	}
	

	public String getAcc_opening_date() {
		return this.acc_opening_dateProperty().get();
	}
	

	public void setAcc_opening_date(final String acc_opening_date) {
		this.acc_opening_dateProperty().set(acc_opening_date);
	}
	

	public StringProperty ifsccodeProperty() {
		return this.ifsccode;
	}
	

	public String getIfsccode() {
		return this.ifsccodeProperty().get();
	}
	

	public void setIfsccode(final String ifsccode) {
		this.ifsccodeProperty().set(ifsccode);
	}
	

	public StringProperty cidProperty() {
		return this.cid;
	}
	

	public String getCid() {
		return this.cidProperty().get();
	}
	

	public void setCid(final String cid) {
		this.cidProperty().set(cid);
	}

	public IntegerProperty balanceProperty() {
		return this.balance;
	}
	

	public int getBalance() {
		return this.balanceProperty().get();
	}
	

	public void setBalance(final int balance) {
		this.balanceProperty().set(balance);
	}

	public StringProperty t_pinProperty() {
		return this.t_pin;
	}
	

	public String getT_pin() {
		return this.t_pinProperty().get();
	}
	

	public void setT_pin(final String t_pin) {
		this.t_pinProperty().set(t_pin);
	}
	
	
	
	
}

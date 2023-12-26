package com.speedbank.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transactions {
	private final IntegerProperty t_id;
	private final StringProperty description;
	private final StringProperty debit;
	private final StringProperty credit;
	private final IntegerProperty prevbalance;
	private final StringProperty t_date;
	private final StringProperty accnos;
	private final StringProperty c_ids;

	public Transactions(int t_id,String description, String debit, String credit, int prevbalance,String t_date,String accnos,String c_ids) {
		this.t_id= new SimpleIntegerProperty(t_id);
		this.description = new SimpleStringProperty(description);
		this.debit = new SimpleStringProperty(debit);
		this.credit = new SimpleStringProperty(credit);
		this.prevbalance = new SimpleIntegerProperty(prevbalance);
		this.t_date=new SimpleStringProperty(t_date);
		this.accnos = new SimpleStringProperty(accnos);
		this.c_ids = new SimpleStringProperty(c_ids);
	}

	public String getDescription() {
		return description.get();
	}

	public String getDebit() {
		return debit.get();
	}

	public String getCredit() {
		return credit.get();
	}

	public int getPrev_balance() {
		return prevbalance.get();
	}

	public void setDescription(String value) {
		description.set(value);
	}

	public void setDebit(String value) {
		debit.set(value);
	}

	public void setCredit(String value) {
		credit.set(value);
	}

	public void setPrev_balance(int value) {
		prevbalance.set(value);
	}

	public StringProperty descriptionProperty() {
		return description;
	}

	public StringProperty debitProperty() {
		return debit;
	}

	public StringProperty creditProperty() {
		return credit;
	}

	public IntegerProperty prevbalanceProperty() {
		return prevbalance;
	}

	public StringProperty accnosProperty() {
		return this.accnos;
	}
	

	public String getAccnos() {
		return this.accnosProperty().get();
	}
	

	public void setAccnos(final String accnos) {
		this.accnosProperty().set(accnos);
	}
	

	public StringProperty c_idsProperty() {
		return this.c_ids;
	}
	

	public String getC_ids() {
		return this.c_idsProperty().get();
	}
	

	public void setC_ids(final String c_ids) {
		this.c_idsProperty().set(c_ids);
	}

	public IntegerProperty t_idProperty() {
		return this.t_id;
	}
	

	public int getT_id() {
		return this.t_idProperty().get();
	}
	

	public void setT_id(final int t_id) {
		this.t_idProperty().set(t_id);
	}

	public StringProperty t_dateProperty() {
		return this.t_date;
	}
	

	public String getT_date() {
		return this.t_dateProperty().get();
	}
	

	public void setT_date(final String t_date) {
		this.t_dateProperty().set(t_date);
	}
	
	
	

}

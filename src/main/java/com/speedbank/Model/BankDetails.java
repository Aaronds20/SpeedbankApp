package com.speedbank.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BankDetails {
	private final StringProperty description;
	private final StringProperty debit;
	private final StringProperty credit;
	private final IntegerProperty prevbalance;

	public BankDetails(String description, String debit, String credit, int prevbalance) {
		this.description = new SimpleStringProperty(description);
		this.debit = new SimpleStringProperty(debit);
		this.credit = new SimpleStringProperty(credit);
		this.prevbalance = new SimpleIntegerProperty(prevbalance);
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
}

package com.speedbank.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoanDetails {
	private final IntegerProperty loanid;
	private final StringProperty loantype;
	private final LongProperty loanamount;
	private final StringProperty Ls_date;
	private final StringProperty Le_date;

	public LoanDetails(int loanid, String loantype, long loanamount,String Ls_date,String Le_date) {
		this.loanid = new SimpleIntegerProperty(loanid);
		this.loantype = new SimpleStringProperty(loantype);
		this.loanamount = new SimpleLongProperty(loanamount);
		this.Ls_date = new SimpleStringProperty(Ls_date);
		this.Le_date = new SimpleStringProperty(Le_date);
	}

	public int getLoanid() {
		return loanid.get();
	}

	public String getLoantype() {
		return loantype.get();
	}

	public long getLoanamount() {
		return loanamount.get();
	}
	
	public String getLs_date() {
		return Ls_date.get();
	}

	public String getLe_date() {
		return Le_date.get();
	}

	public void setLoanid(int value) {
		loanid.set(value);
	}

	public void setLoantype(String value) {
		loantype.set(value);
	}

	public void setLoanamount(long value) {
		loanamount.set(value);
	}
	
	public void setLs_date(String value) {
		Ls_date.set(value);
	}

	public void setLe_date(String value) {
		Le_date.set(value);
	}

	public IntegerProperty loanidProperty() {
		return loanid;
	}

	public StringProperty loantypeProperty() {
		return loantype;
	}

	public LongProperty loanamountProperty() {
		return loanamount;
	}
	
	public StringProperty ls_dateProperty() {
		return Ls_date;
	}

	public StringProperty le_dateProperty() {
		return Le_date;
	}
}

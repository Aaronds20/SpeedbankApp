package com.speedbank.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Loan {
	private final IntegerProperty loanid;
	private final StringProperty loantype;
	private final LongProperty loanamount;
	private final StringProperty Ls_date;
	private final StringProperty Le_date;
	private final StringProperty c_id;

	public Loan(int loanid, String loantype, long loanamount, String Ls_date,String Le_date,String c_id) {
		this.loanid = new SimpleIntegerProperty(loanid);
		this.loantype = new SimpleStringProperty(loantype);
		this.loanamount = new SimpleLongProperty(loanamount);
		this.Ls_date = new SimpleStringProperty(Ls_date);
		this.Le_date = new SimpleStringProperty(Le_date);
		this.c_id=new SimpleStringProperty(c_id);
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

	public void setLoanid(int value) {
		loanid.set(value);
	}

	public void setLoantype(String value) {
		loantype.set(value);
	}

	public void setLoanamount(long value) {
		loanamount.set(value);
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

	public StringProperty c_idProperty() {
		return this.c_id;
	}
	

	public String getC_id() {
		return this.c_idProperty().get();
	}
	

	public void setC_id(final String c_id) {
		this.c_idProperty().set(c_id);
	}

	public StringProperty Ls_dateProperty() {
		return this.Ls_date;
	}
	

	public String getLs_date() {
		return this.Ls_dateProperty().get();
	}
	

	public void setLs_date(final String Ls_date) {
		this.Ls_dateProperty().set(Ls_date);
	}
	

	public StringProperty Le_dateProperty() {
		return this.Le_date;
	}
	

	public String getLe_date() {
		return this.Le_dateProperty().get();
	}
	

	public void setLe_date(final String Le_date) {
		this.Le_dateProperty().set(Le_date);
	}
	
	
}

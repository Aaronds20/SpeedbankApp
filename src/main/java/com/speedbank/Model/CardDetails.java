package com.speedbank.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CardDetails {
	private final StringProperty cardnumber;
	private final StringProperty c_date;
	private final StringProperty e_date;
	private final StringProperty cvv;
	private final StringProperty c_ids;
	
	public CardDetails(String cardnumber, String c_date, String e_date, String cvv,String c_ids) {
		this.cardnumber = new SimpleStringProperty(cardnumber);
		this.c_date = new SimpleStringProperty(c_date);
		this.e_date = new SimpleStringProperty(e_date);
		this.cvv = new SimpleStringProperty(cvv);
		this.c_ids = new SimpleStringProperty(c_ids);
	}

	public StringProperty cardnumberProperty() {
		return this.cardnumber;
	}
	

	public String getCardnumber() {
		return this.cardnumberProperty().get();
	}
	

	public void setCardnumber(final String cardnumber) {
		this.cardnumberProperty().set(cardnumber);
	}
	

	public StringProperty c_dateProperty() {
		return this.c_date;
	}
	

	public String getC_date() {
		return this.c_dateProperty().get();
	}
	

	public void setC_date(final String c_date) {
		this.c_dateProperty().set(c_date);
	}
	

	public StringProperty e_dateProperty() {
		return this.e_date;
	}
	

	public String getE_date() {
		return this.e_dateProperty().get();
	}
	

	public void setE_date(final String e_date) {
		this.e_dateProperty().set(e_date);
	}
	

	public StringProperty cvvProperty() {
		return this.cvv;
	}
	

	public String getCvv() {
		return this.cvvProperty().get();
	}
	

	public void setCvv(final String cvv) {
		this.cvvProperty().set(cvv);
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
	
}

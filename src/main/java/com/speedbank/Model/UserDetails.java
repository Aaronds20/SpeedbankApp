package com.speedbank.Model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserDetails {
	private final StringProperty customerid;
	private final StringProperty fullname;
	private final StringProperty pancard;
	private final StringProperty email;
	private final StringProperty phoneno;
	private final StringProperty address;
	private final StringProperty dob;
	private final StringProperty gender;
	private final StringProperty password;


	public UserDetails(String customerid, String fullname, String pancard, String email, String phoneno,String address, String dob,
			String gender, String password) {
		this.customerid = new SimpleStringProperty(customerid);
		this.fullname = new SimpleStringProperty(fullname);
		this.pancard = new SimpleStringProperty(pancard);
		this.email = new SimpleStringProperty(email);
		this.phoneno = new SimpleStringProperty(phoneno);
		this.address = new SimpleStringProperty(address);
		this.dob = new SimpleStringProperty(dob);
		this.gender = new SimpleStringProperty(gender);
		this.password = new SimpleStringProperty(password);
	}

	public String getCustomerid() {
		return customerid.get();
	}

	public String getFullname() {
		return fullname.get();
	}

	public String getPancard() {
		return pancard.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getPhoneno() {
		return phoneno.get();
	}
	public String getAddress() {
		return address.get();
	}

	public String getDob() {
		return dob.get();
	}

	public String getGender() {
		return gender.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setCustomerid(String value) {
		customerid.set(value);
	}

	public void setFullname(String value) {
		fullname.set(value);
	}

	public void setPancard(String value) {
		pancard.set(value);
	}

	public void setEmail(String value) {
		email.set(value);
	}

	public void setPhoneno(String value) {
		phoneno.set(value);
	}
	public void setAddress(String value) {
		address.set(value);
	}
	public void setDob(String value) {
		dob.set(value);
	}

	public void setGender(String value) {
		gender.set(value);
	}

	public void setPassword(String value) {
		password.set(value);
	}

	public StringProperty customeridProperty() {
		return customerid;
	}

	public StringProperty fullnameProperty() {
		return fullname;
	}

	public StringProperty pancardProperty() {
		return pancard;
	}

	public StringProperty emailProperty() {
		return email;
	}

	public StringProperty phonenoProperty() {
		return phoneno;
	}
	public StringProperty addressProperty() {
		return address;
	}

	public StringProperty dobProperty() {
		return dob;
	}

	public StringProperty genderProperty() {
		return gender;
	}

	public StringProperty passwordProperty() {
		return password;
	}
}

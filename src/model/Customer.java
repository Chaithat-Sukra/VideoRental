package model;

public class Customer {
	private int customerID;
	private String name;
	private String address;
	private String email;
	private String custTel;
	private String password;
	private boolean rating = false;
	
	public void init(int aCustID, String aName, String aAddress, String aEmail, String aTel, String aPremium, String aPassword) {
		this.customerID = aCustID;
		this.name = aName;
		this.address = aAddress;
		this.email = aEmail;
		this.custTel = aTel;
		this.rating = (aPremium.equals("S"))? false : true;
		this.password = aPassword;
	}
	
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isRating() {
		return rating;
	}

	public void setRating(boolean rating) {
		this.rating = rating;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getCustTel() {
		return custTel;
	}

	public void setCustTel(String custTel) {
		this.custTel = custTel;
	}
	
	public String toString() {
		return "*********************\nName: " + name + "\n" + "Premium: " + (rating? "Yes" : "No") + "\n" + "Address: " + address + "\n" + "Email: " + email + "\n" + "Telephone: " + this.custTel + "\n";	
	}
}
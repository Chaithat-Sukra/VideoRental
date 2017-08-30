package model;

public class Clerk {
	private int AdminID;
	private boolean isAdmin = false;
	private String name;
	private String address;
	private String email;
	private String adminTel;
	private String password;
	
	public void init(int aAdminID, boolean aAdmin, String aName, String aAddress, String aEmail, String aTel, String aPassword) {
		this.AdminID = aAdminID;
		this.isAdmin = aAdmin;
		this.name = aName;
		this.address = aAddress;
		this.email = aEmail;
		this.adminTel = aTel;
		this.password = aPassword;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public int getAdminID() {
		return AdminID;
	}

	public void setAdminID(int adminID) {
		AdminID = adminID;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return "************\nName: " + name + "\n" + "Admin: " + (this.isAdmin? "Yes" : "No") + "\n" + "Address: " + address + "\n" + "Email: " + email + "\n" + "Telephone: " + this.adminTel + "\n";
	}

	public String getAdminTel() {
		return adminTel;
	}

	public void setAdminTel(String adminTel) {
		this.adminTel = adminTel;
	}
}
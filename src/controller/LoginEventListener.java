package controller;

import model.Customer;
import model.Clerk;
import model.VideoCopy;
import utilities.SQLAdapter;
import view.VideoSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginEventListener implements ActionListener {
	VideoSystem _videoSystem;
	private Map<String, Customer> _mCustomer = new HashMap<String, Customer>();
	private Map<String, Clerk> _mClerk = new HashMap<String, Clerk>();
	
	private Customer _currentCustomer;
	private Clerk _currentClerk;
	
	public enum TypeUser {
		TypeUserCustomer, TypeUserClerk, TypeUserAdmin, TypeUserNone, TypeUserIncorrectPassoword;
	}
	
	public TypeUser typeUser;
	
	public LoginEventListener(VideoSystem aSystem) {
		super();
		this._videoSystem = aSystem;
		
		SQLAdapter sqlAdapter = SQLAdapter.getInstance();
		this.loadClerk(sqlAdapter);
		this.loadCustomer(sqlAdapter);
	}
	
	public TypeUser verifyLogin(String aUsername, String aPassword) {
		if (this._mCustomer.containsKey(aUsername)) {
			this._currentCustomer = this._mCustomer.get(aUsername);
			if (aPassword.equals(this._currentCustomer.getPassword())) {
				return TypeUser.TypeUserCustomer;
			}
			this._currentCustomer = null;
			return TypeUser.TypeUserIncorrectPassoword;
		}
		if (this._mClerk.containsKey(aUsername)) {
			this._currentClerk = this._mClerk.get(aUsername);
			if (aPassword.equals(this._currentClerk.getPassword())) {				
				return this._currentClerk.isAdmin()? TypeUser.TypeUserAdmin : TypeUser.TypeUserClerk; 
			}
			this._currentClerk = null;
			return TypeUser.TypeUserIncorrectPassoword;
		}
		return TypeUser.TypeUserNone;
	}

	public boolean addCustomer(String aName, String aAddress, String aEmail, String aTel, String aRating, String aPassword) {
		SQLAdapter sqlAdapter = SQLAdapter.getInstance();
		int custID = this._mCustomer.size() + 1;
		String rating = aRating;
		rating = rating.replace("1", "S");
		rating = rating.replace("2", "P");

		List<Object> objects = new ArrayList<Object>();
		objects.add(custID);
		objects.add(aName);
		objects.add(aAddress);
		objects.add(aEmail);
		objects.add(aTel);
		objects.add(rating);
		objects.add(aPassword);
		
		if (sqlAdapter.insertIntoTable("INSERT INTO CUSTOMER VALUES (?, ?, ?, ?, ?, ?, ?)", objects)) {
			this.loadCustomer(sqlAdapter);
			return true;
		}
		return false;
	}
	
	public Clerk getCurrentClerk() {
		return this._currentClerk;
	}
	
	public Customer getCurrentCustomer() {
		return this._currentCustomer;
	}
	
	public Customer getCustomerWithID(int aID) {
		for (Map.Entry<String, Customer> entry : this._mCustomer.entrySet()) {
			Customer cust = entry.getValue();
			if (cust.getCustomerID() == aID)
				return cust;
		}
		return null;
	}
	
	public Customer getCustomerWithName(String aName) {
		return this._mCustomer.get(aName);
	}

	private void loadClerk(SQLAdapter aSQLAdapter) {
		this._mClerk.clear();
		String[] mappingClerk = {"AdminID", "name", "address", "email", "admintel", "isAdmin", "password"};
		
		ArrayList<Map<String, String>> dataList = aSQLAdapter.getData(mappingClerk, "select * from ADMIN");
		for (Map<String, String> mapClerk : dataList) {
			Clerk clerk = new Clerk();
			clerk.init(Integer.parseInt(mapClerk.get("AdminID")), 
					mapClerk.get("isAdmin").equals("0")? false : true, 
					mapClerk.get("name"), 
					mapClerk.get("address"),  
					mapClerk.get("email"),  
					mapClerk.get("admintel"),  
					mapClerk.get("password"));
			if (clerk != null)
				this._mClerk.put(clerk.getName(), clerk);
		}
//		System.out.println(this._mClerk);
	}
	
	private void loadCustomer(SQLAdapter aSQLAdapter) {
		this._mCustomer.clear();
		String[] mappingCustomer = {"CustID", "name", "address", "email", "Custel", "rating", "password"};
		ArrayList<Map<String, String>> dataList = aSQLAdapter.getData(mappingCustomer, "select * from CUSTOMER");
		for (Map<String, String> mapCustomer : dataList) {
			Customer customer = new Customer(); 
			customer.init(Integer.parseInt(mapCustomer.get("CustID")), 
					mapCustomer.get("name"), 
					mapCustomer.get("address"),  
					mapCustomer.get("email"),  
					mapCustomer.get("Custel"), 
					mapCustomer.get("rating"),
					mapCustomer.get("password"));
			if (customer != null)
				_mCustomer.put(customer.getName(), customer);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
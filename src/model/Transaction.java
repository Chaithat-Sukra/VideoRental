package model;

public class Transaction {
	private int transacID;
	private String videoCopyIDs;
	private String r_videoCopyIDs;
	
	protected String rentaldate;
	protected int custID;
	private boolean reviewed;
	
	public void init(int aTransacID, String aRentaldate, int aCustID, String aReviewed, String aCopyIDs, String aRCopyIDs) {
		this.transacID = aTransacID;
		this.rentaldate = aRentaldate;
		this.custID = aCustID;
		this.reviewed = aReviewed.equals("0")? false : true;
		this.videoCopyIDs = aCopyIDs;
		this.r_videoCopyIDs = aRCopyIDs;
	}
	
	public int getTransacID() {
		return transacID;
	}
	public void setTransacID(int transacID) {
		this.transacID = transacID;
	}
	public boolean isReviewed() {
		return reviewed;
	}
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	public String getRentaldate() {
		return this.rentaldate;
	}
	public void setRentaldate(String aRentaldate) {
		this.rentaldate = aRentaldate;
	}
	public int getCustID() {
		return this.custID;
	}
	public void setCustID(int aCustID) {
		this.custID = aCustID;
	}

	public String getVideoCopyIDs() {
		return videoCopyIDs;
	}

	public void setVideoCopyIDs(String videoCopyIDs) {
		this.videoCopyIDs = videoCopyIDs;
	}

	public String getR_videoCopyIDs() {
		return r_videoCopyIDs;
	}

	public void setR_videoCopyIDs(String r_videoCopyIDs) {
		this.r_videoCopyIDs = r_videoCopyIDs;
	}
}
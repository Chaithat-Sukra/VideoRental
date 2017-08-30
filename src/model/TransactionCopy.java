package model;

public class TransactionCopy {
	private int copyID; 
	private String dateDue;
	private String dateReturn;
	private float rentalCharge;
	private boolean status;
	private int transacID;
	private int videoCopyID;
	
	public void init(int aCopyID, String aDateDue, String aDateReturn, String aRentalCharge, String aStatus, int aTransacID, int aVideoCopyID) {
		this.copyID = aCopyID;
		this.dateDue = aDateDue;
		if (aDateReturn != null)
			this.dateReturn = aDateReturn;
		this.rentalCharge = Float.parseFloat(aRentalCharge);
		this.status = aStatus.equals("0")? false : true;
		this.transacID  = aTransacID;
		this.videoCopyID = aVideoCopyID;
	}
	
	public String getDateDue() {
		return dateDue;
	}
	public void setDateDue(String dateDue) {
		this.dateDue = dateDue;
	}
	public String getDateReturn() {
		return dateReturn;
	}
	public void setDateReturn(String dateReturn) {
		this.dateReturn = dateReturn;
	}
	public float getRentalCharge() {
		return rentalCharge;
	}
	public void setRentalCharge(float rentalCharge) {
		this.rentalCharge = rentalCharge;
	}
	public int getCopyID() {
		return copyID;
	}
	public void setCopyID(int copyID) {
		this.copyID = copyID;
	}
	public boolean isStatusActive() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getTransacID() {
		return transacID;
	}
	public void setTransacID(int transacID) {
		this.transacID = transacID;
	}
	public int getVideoCopyID() {
		return videoCopyID;
	}
	public void setVideoCopyID(int videoCopyID) {
		this.videoCopyID = videoCopyID;
	}
}
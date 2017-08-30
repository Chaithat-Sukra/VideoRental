package model;

public class VideoCopy {
	private int copyID;
	private String datePurchase;
	private boolean damage;
	private boolean availale;
	private int noOfRent;
	private int videoID;
	
	public void init(int aCopyID, String aDatePurchase, boolean aDamage, boolean aAvailale, int aNoOfRent, int aVideoID) {
		this.copyID = aCopyID;
		this.datePurchase = aDatePurchase;
		this.damage = aDamage;
		this.availale = aAvailale;
		this.noOfRent = aNoOfRent;
		this.videoID = aVideoID;
	}

	public int getCopyID() {
		return copyID;
	}

	public void setCopyID(int copyID) {
		this.copyID = copyID;
	}

	public String getDatePurchase() {
		return datePurchase;
	}

	public void setDatePurchase(String datePurchase) {
		this.datePurchase = datePurchase;
	}

	public boolean isDamage() {
		return damage;
	}

	public void setDamage(boolean damage) {
		this.damage = damage;
	}

	public boolean isAvailale() {
		return availale;
	}

	public void setAvailale(boolean availale) {
		this.availale = availale;
	}

	public int getNoOfRent() {
		return noOfRent;
	}

	public void setNoOfRent(int noOfRent) {
		this.noOfRent = noOfRent;
	}

	public int getVideoID() {
		return videoID;
	}

	public void setVideoID(int videoID) {
		this.videoID = videoID;
	}
}
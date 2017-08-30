package model;
import model.VideoCopy;

public class Video {
	public enum Categories {
		Comedy, Action, Family, Drama
	}
	private VideoCopy videoCopy;
	private int videoID;
	private String title;
	private float rentalCharge;
	private Categories categories;
	private int rentPeriod;
	private String yearRelease;
	private float overdueCharge;
	
	public void init(int aVideoID, String aTitle, float aRentalCharge, Categories aCategories, int aRentPeriod, String aYearRelease, float aOverdueCharge, VideoCopy aVideoCopy) {
		this.videoID = aVideoID;
		this.title = aTitle;
		this.rentalCharge = aRentalCharge;
		this.categories = aCategories;
		this.rentPeriod = aRentPeriod;
		this.yearRelease = aYearRelease;
		this.overdueCharge = aOverdueCharge;
		this.videoCopy = aVideoCopy;
	}
	
	public int getVideoID() {
		return videoID;
	}
	public void setVideoID(int videoID) {
		this.videoID = videoID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getRentalCharge() {
		return rentalCharge;
	}
	public void setRentalCharge(float rentalCharge) {
		this.rentalCharge = rentalCharge;
	}
	public int getRentPeriod() {
		return rentPeriod;
	}
	public void setRentPeriod(int rentPeriod) {
		this.rentPeriod = rentPeriod;
	}
	public String getYearRelease() {
		return yearRelease;
	}
	public void setYearRelease(String yearRelease) {
		this.yearRelease = yearRelease;
	}
	public float getOverdueCharge() {
		return overdueCharge;
	}
	public void setOverdueCharge(float overdueCharge) {
		this.overdueCharge = overdueCharge;
	}
	public Categories getCategories() {
		return categories;
	}
	public void setCategories(Categories categories) {
		this.categories = categories;
	}
	public VideoCopy getVideoCopy() {
		return videoCopy;
	}
	public void setVideoCopy(VideoCopy videoCopy) {
		this.videoCopy = videoCopy;
	}
	
	public String toString() {
		return "Video ID: " + Integer.toString(videoID) + "\n" + "Title: " + title + "\n" + "Category: " + categories + "\n" + "Year of Release: " + yearRelease + "\n" + "Rental Period: " + Float.toString(rentPeriod) + " days" + "\n" + "Rental Charge: $" + Float.toString(rentalCharge) + "\n" + "Overdue Charge: $" + Float.toString(overdueCharge) + " per day";
	}

}
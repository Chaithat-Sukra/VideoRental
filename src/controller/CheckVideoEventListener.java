package controller;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.AddressException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import utilities.SQLAdapter;
import model.ObjectEvent;
import model.Transaction;
import model.TransactionCopy;
import model.Video;
import model.Customer;
import model.VideoCopy;

public class CheckVideoEventListener implements ActionListener {
	private Map<String, Transaction> _mTransaction = new HashMap<String, Transaction>();
	private Map<String, TransactionCopy> _mTransactionCopy = new HashMap<String, TransactionCopy>();
	public CheckVideoEventListener() {
		super();
		this.loadTransactions(SQLAdapter.getInstance());
		this.loadTransactionCopys(SQLAdapter.getInstance());
	}
	
	public Map<String, Transaction> getTransactions() {
		return this._mTransaction;
	}
	
	public Map<String, TransactionCopy> getTransactionCopys() {
		return this._mTransactionCopy;
	}
	
	public Transaction getTransaction(String aTransactionID) {
		if (this._mTransaction.containsKey(aTransactionID))
			return this._mTransaction.get(aTransactionID);
		return null;
	}
	
	public TransactionCopy getTransactionCopy(String aCopyID) {
		if (this._mTransactionCopy.containsKey(aCopyID))
			return this._mTransactionCopy.get(aCopyID);
		return null;
	}
	
	public ObjectEvent emailCustomer(Customer aCustomer, Transaction aTransaction, Map<String, VideoCopy> aVideoCopys, Map<String, Video> aVideos) throws UnsupportedEncodingException {
		ObjectEvent event = new ObjectEvent();
		event.isSuccessful = false;
		event.resultMessage = "No emails have been sent";
		
		String email = aCustomer.getEmail();
		String name = aCustomer.getName();
		
		String[] listOfReveveCopyID = aTransaction.getR_videoCopyIDs().split(",");
		String msgAlert = "Mail has been sent to " + email + "\nMessage: "; 
				
		for (String copyID : listOfReveveCopyID) {
			if (copyID.equals(""))
				continue;
			
			VideoCopy copy = aVideoCopys.get(copyID);
			Video video = aVideos.get(String.valueOf(copy.getVideoID()));
			msgAlert += video.getTitle() + " is now avaialble for rent\n";
		}
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		final String emailFrom = "chaithat.sukra@gmail.com";
		final String password = "Ch@mp2727";
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailFrom,
								password);
					}
				  });
		try {
			event.isSuccessful = true;

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailFrom));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Video/s that you have reserved are now available");
			message.setText(msgAlert);

			Transport.send(message);

			event.resultMessage = "An email has been sent to " + email + " sucessfully";

        } catch (AddressException e) {
        	event.resultMessage = "address error";
			System.out.println(e);
			
        } catch (MessagingException e) {
        	event.resultMessage = "message error";
        	System.out.println(e);
        }
		return event;
	}
	
	public ObjectEvent checkoutVideo(Transaction aTransaction, Map<String, VideoCopy> aVideoCopys, Map<String, Video> aVideos) {
		ObjectEvent event = new ObjectEvent();
		event.isSuccessful = false;
		event.resultMessage = "";
		
		SQLAdapter sqlAdapter = SQLAdapter.getInstance();
		int transacID = aTransaction.getTransacID();
		List<Object> objForTrans = new ArrayList<Object>();
		objForTrans.add(true);
		objForTrans.add(transacID);
		if (sqlAdapter.updateTable("UPDATE TRANSACTION SET reviewed = ? WHERE transacID = ?", objForTrans)) {				
			event.isSuccessful = true;

			//for rental videos
			String[] listOfCopyID = aTransaction.getVideoCopyIDs().split(",");
			String coMessage = "";
			
			for (String copyID : listOfCopyID) {
				if (copyID.equals(""))
					continue;
				List<Object> objForTransCopy = new ArrayList<Object>();
				objForTransCopy.add(this._mTransactionCopy.size() + 1);
				
				VideoCopy copy = aVideoCopys.get(copyID);
				Video video = aVideos.get(String.valueOf(copy.getVideoID()));
				
				String sRentalDate = aTransaction.getRentaldate();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date dateDue = null;
				
				int returnDate = video.getRentPeriod();
				try {
					Date rentalDate = format.parse(sRentalDate);
					dateDue = new Date(rentalDate.getTime() + (1000 * 60 * 60 * 24 * returnDate));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				objForTransCopy.add(format.format(dateDue)); 
				objForTransCopy.add(true);
				objForTransCopy.add(format.format(dateDue));				
				objForTransCopy.add(0);
				objForTransCopy.add(aTransaction.getTransacID());
				objForTransCopy.add(copyID);
				
				if (sqlAdapter.insertIntoTable("INSERT INTO TRANSACTION_COPY VALUES (?, ?, ?, ?, ?, ?, ?)", objForTransCopy)) {
					coMessage = video.getTitle() + " has been checked out successfully\n";
					this.loadTransactionCopys(sqlAdapter);
				} else {
					coMessage = video.getTitle() + " has been checked out unsuccessfully\n";
				}
				event.resultMessage = event.resultMessage + coMessage;
			}
			
			//for rental videos
			String[] listOfReveveCopyID = aTransaction.getR_videoCopyIDs().split(",");
			for (String copyID : listOfReveveCopyID) {
				if (copyID.equals(""))
					continue;
				List<Object> objForTransCopy = new ArrayList<Object>();
				objForTransCopy.add(this._mTransactionCopy.size() + 1);
				
				VideoCopy copy = aVideoCopys.get(copyID);
				Video video = aVideos.get(String.valueOf(copy.getVideoID()));
				coMessage = video.getTitle() + " has been succesfully reserved\n";
				event.resultMessage = event.resultMessage + coMessage;
			}
			return event;
		} else {
			event.resultMessage = "Transaction is updated unsuccessfully.";
		}
		return event;
	}
			
	public boolean createTransaction(Customer aCustomer, String aListOfVideoIDs, String aListOfVideoCopyIDs) {
		SQLAdapter sqlAdapter = SQLAdapter.getInstance();

		List<Object> objects = new ArrayList<Object>();
		objects.add(this._mTransaction.size() + 1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		objects.add(dateFormat.format(date));
		objects.add(aCustomer.getCustomerID());		
		objects.add(false);
		objects.add(aListOfVideoIDs);
		objects.add(aListOfVideoCopyIDs);
		if (sqlAdapter.insertIntoTable("INSERT INTO TRANSACTION VALUES (?, ?, ?, ?, ?, ?)", objects)) {
			this.loadTransactions(sqlAdapter);
			return true;		
		}
		return false;
	}
	
	public ObjectEvent returnVideo(Customer aCustomer, Transaction aTrans, TransactionCopy aTransacCopy, Video aVideo, VideoCopy aVideoCopy, String aNoOfReturn, String aDamaged) {
		float charge = aVideo.getRentalCharge();
		//check if rental overdue
		int allowDay = aVideo.getRentPeriod();
		int totalRent = Integer.parseInt(aNoOfReturn);
		int dayOver = totalRent - allowDay;
		if (dayOver > 0) {
			charge += dayOver * aVideo.getOverdueCharge();
		}

		//discount for premium user
		if (aCustomer.isRating()) {
			charge = (float) (charge * 0.90); 
		}
				
		ObjectEvent event = new ObjectEvent(); 
		event.isSuccessful = false;
		event.resultMessage = "";
		
		SQLAdapter adapter = SQLAdapter.getInstance();
                
		//check if damage
		if (aDamaged.equals("2")) {
			//update SQL 
			List<Object> objUpdate = new ArrayList<Object>();
			objUpdate.add(aVideoCopy.getCopyID());
			if (adapter.updateTable("UPDATE VIDEO_COPY SET damage = 1 WHERE copyID = ?", objUpdate))
				event.resultMessage = "Item is updated as damaged\n";
		}
		
		//update sql
		List<Object> objUpdate = new ArrayList<Object>();
		objUpdate.add(true);
		objUpdate.add(aVideoCopy.getNoOfRent() + 1);
		objUpdate.add(aVideoCopy.getCopyID());
		
		String videoName = aVideo.getTitle();
		if (adapter.updateTable("UPDATE VIDEO_COPY SET available = ?, noOfRent = ? WHERE copyID = ?", objUpdate)) {
			
			List<Object> objTransCopyUpdate = new ArrayList<Object>();
			String sRentalDate = aTrans.getRentaldate();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date dateReturn = null;
			
			int returnDate = totalRent;
			try {
				Date rentalDate = format.parse(sRentalDate);
				dateReturn = new Date(rentalDate.getTime() + (1000 * 60 * 60 * 24 * returnDate));
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			objTransCopyUpdate.add(false);
			objTransCopyUpdate.add(dateReturn);
			objTransCopyUpdate.add(charge);
			objTransCopyUpdate.add(aTransacCopy.getCopyID());
			if (adapter.updateTable("UPDATE TRANSACTION_COPY SET status = ?, dateReturn = ?, rentalCharged = ? WHERE copyID = ?", objTransCopyUpdate)) {
				event.isSuccessful = true;
				event.resultMessage += "\n*********************\n" + videoName + " has been returned\nRental change is $" + charge + "\n";
			}
		}
		return event;
	}
	
	public List<Transaction> getWaitingReviewTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (Map.Entry<String, Transaction> entry : this._mTransaction.entrySet()) {
			Transaction transaction = entry.getValue();
			if (!transaction.isReviewed()) 
				transactions.add(transaction);
		}
		return transactions;
	}
	
	private void loadTransactions(SQLAdapter aSQLAdapter) {
		this._mTransaction.clear();

		String[] mappingTransaction = {"transacID", "rentaldate", "custID", "reviewed", "videoCopyIDs", "r_videoCopyIDs"};
		
		ArrayList<Map<String, String>> dataList = aSQLAdapter.getData(mappingTransaction, "select * from TRANSACTION");
		for (Map<String, String> mapTransaction : dataList) {
			Transaction transaction = new Transaction();
			transaction.init(
					Integer.parseInt(mapTransaction.get("transacID")), 
					mapTransaction.get("rentaldate"), 
					Integer.parseInt(mapTransaction.get("custID")), 
					mapTransaction.get("reviewed"), 
					mapTransaction.get("videoCopyIDs"), 
					mapTransaction.get("r_videoCopyIDs"));
			if (transaction != null)
				this._mTransaction.put(String.valueOf(transaction.getTransacID()), transaction);
		}
	}
	
	private void loadTransactionCopys(SQLAdapter aSQLAdapter) {
		this._mTransactionCopy.clear();

		String[] mappingTransaction = {"copyID", "dateDue", "status", "dateReturn", "rentalCharged", "transacID", "videoCopyID"};
		
		ArrayList<Map<String, String>> dataList = aSQLAdapter.getData(mappingTransaction, "select * from TRANSACTION_COPY");
		for (Map<String, String> mapTransactionCopy : dataList) {
			TransactionCopy transCopy = new TransactionCopy();
			transCopy.init(
					Integer.parseInt(mapTransactionCopy.get("copyID")), 
					mapTransactionCopy.get("dateDue"), 
					mapTransactionCopy.get("dateReturn"),  
					mapTransactionCopy.get("rentalCharged"), 
					mapTransactionCopy.get("status"), 
					Integer.parseInt(mapTransactionCopy.get("transacID")), 
					Integer.parseInt(mapTransactionCopy.get("videoCopyID")));
			if (transCopy != null)
				this._mTransactionCopy.put(String.valueOf(transCopy.getCopyID()), transCopy);
		}
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
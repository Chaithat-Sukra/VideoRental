package view;

import controller.LoginEventListener;
import controller.LoginEventListener.TypeUser;
import controller.LoadVideoEventListener;
import controller.CheckVideoEventListener;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import org.junit.Test;

import model.Customer;
import model.ObjectEvent;
import model.Transaction;
import model.TransactionCopy;
import model.Video;
import model.Video.Categories;
import model.VideoCopy;
import utilities.SQLAdapter;

public class VideoSystem extends JFrame {
	private static final long serialVersionUID = 1L;
	private Scanner scannerInput;
	
	private LoginEventListener _loginBL = null;
	private LoadVideoEventListener _loadVideoBL = null; 
	private CheckVideoEventListener _checkVideo = null;
	
	TypeUser _typeUser;
	
	@Test
	public void testLogin() {
		System.out.println("testLogin");

//		String testUsername = "champ_customer123";
//		String testPassword = "123";
//		
//		assertEquals("invalid user!!!", 
//				this._loginBL.verifyLogin(testUsername, testPassword), 
//				this._loginBL.verifyLogin("champ_customer", "123"));
	}
	
//	@Test 
//	public void testShowInformationOfAdmin() {
//		System.out.println("testShowInformationOfAdmin");
//		
//		String testInput = "1";
//		String inputOption = this.getUserInputByShowingMessage("*****Option*****\n1 = information, 2 = function : ");
//		assertEquals("wrong input!!!", 
//				testInput, 
//				inputOption);		
//	}
//	
//	@Test
//	public void testSearchVideo() {
//		System.out.println("testSearchVideo");
//		
//		int numVideo = 1;
//		String inputKeyword = this.getUserInputByShowingMessage("Keyword : ");
//		
//		assertEquals("wrong number of video", 
//				numVideo, 
//				this._loadVideoBL.getSearchByKeywordListOfVideos(inputKeyword).size());
//	}
//	
//	@Test
//	public void testSearchCatogoryVideo() {
//		System.out.println("testSearchCatogoryVideo");
//		
//		int numVideo = 1;
//		String inputCategory = this.getUserInputByShowingMessage("*****Categories*****\n1 = Comedy\n2 = Action\n3 = Family\n4 = Drama\n");
//		assertEquals("wrong number of video", 
//				numVideo, 
//				this._loadVideoBL.getSearchByCategoryListOfVideos(Categories.values()[Integer.valueOf(inputCategory) - 1]).size());
//	}
	
	public VideoSystem() {
		SQLAdapter sqlAdapter = SQLAdapter.getInstance();
		try {
			if (sqlAdapter.sqlConnect()) {
				_loginBL = new LoginEventListener(this);
				_loadVideoBL = new LoadVideoEventListener(this);
				_checkVideo = new CheckVideoEventListener();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		
		}

//		this.testLogin();
//		this.testShowInformationOfAdmin();
//		this.testSearchVideo();
//		this.testSearchCatogoryVideo();
		validateUser();
		presentOption();
	}
		
        //log in function
	private void validateUser() {
		String username = this.getUserInputByShowingMessage("\nLogin Page\n*********************\nEnter your Username: ");
		String password = this.getUserInputByShowingMessage("Enter your Password: ");
		
		//validating username && password		
		_typeUser = this._loginBL.verifyLogin(username, password);
		if (_typeUser == TypeUser.TypeUserAdmin) { 
			System.out.println("\nAdmin page");
			
		} else if (_typeUser == TypeUser.TypeUserClerk) {
			System.out.println("\nClerk page");
		
		} else if (_typeUser == TypeUser.TypeUserCustomer) {
			System.out.println("\nCustomer page");
			
		} else if (_typeUser == TypeUser.TypeUserIncorrectPassoword) {
			System.out.println("\nIncorrect password, please try again");
			validateUser();
		
		} else {
			System.out.println("\nInvalid username, please try again");
			validateUser();
		}
	}
	
	void presentOption() {
		String inputOption = this.getUserInputByShowingMessage("*********************\nPlease select an option\n1 = Information\n2 = Functions\nSelect: ");

		//Admin || Clerk
		//1 : show detail, 2 : show function 
		if (_typeUser == TypeUser.TypeUserAdmin || _typeUser == TypeUser.TypeUserClerk) {
			if (inputOption.equals("1")) {
                                System.out.println("\nYour information");
				System.out.println(this._loginBL.getCurrentClerk().toString());
			        this.presentOption();
			} else if (inputOption.equals("2")) {
				String promptFunction = "\nFunctions Page\n*********************\nPlease select an option\n1 = Add customer\n2 = Add video\n3 = Checkout video\n4 = Return video\n5 = Show all videos\n6 = Exit System\n";
				if (_typeUser == TypeUser.TypeUserAdmin)
					promptFunction += "7 = Communicate with customer\n";
				
				String inputFunction = this.getUserInputByShowingMessage(promptFunction);
				
				//add customer
				if (inputFunction.equals("1")) {
					String inputName = this.getUserInputByShowingMessage("\nAdd Customer - Enter the customers details below\n*********************\nName: ");
					String inputAddress = this.getUserInputByShowingMessage("Home Address: ");
					String inputEmail = this.getUserInputByShowingMessage("Email Address: ");
					String inputTel = this.getUserInputByShowingMessage("Telephone Number: ");
					String inputRating = this.getUserInputByShowingMessage("Rating: 1 = Standard, 2 = Premium: ");
					String inputPassword = this.getUserInputByShowingMessage("Password: ");
					String inputConfirmPassword = this.getUserInputByShowingMessage("Re-Password: ");
					if (inputPassword.equals(inputConfirmPassword)) {
						if (this._loginBL.addCustomer(inputName, inputAddress, inputEmail, inputTel, inputRating, inputPassword)) {							
							System.out.println("\n*********************\n\nCustomer has been added successfully ");
							if (inputRating.equals("1"))
								System.out.println("Customer is a standard member\n ");
							else
								System.out.println("Customer is a premium member, please pay the $50 joining fee\n ");
						} else {
							System.out.println("\n*********************\n\nError: Customer has not been added\n ");
						}
					}
					this.presentOption();
					
				//add video
				} else if (inputFunction.equals("2")) {					
					String inputTitle = this.getUserInputByShowingMessage("\nAdd Video - Enter video information below\n*********************\nTitle: ");
					String inputRentalCharge = this.getUserInputByShowingMessage("Rental Charge: $");
					String inputCat = this.getUserInputByShowingMessage("Category: Comedy = 1, Action = 2, Family = 3, Drama = 4: ");
					Categories category;
					//Comedy, Action, Family, Drama
					if (inputCat.equals("1"))
						category = Categories.Comedy;
					else if (inputCat.equals("2"))
						category = Categories.Action;
					else if (inputCat.equals("3"))
						category = Categories.Family;
					else
						category = Categories.Drama;
					
					String inputPeriod = this.getUserInputByShowingMessage("Rental Period (In days) : ");
					String inputYear = this.getUserInputByShowingMessage("Year of Release: ");
					String inputOverdueCharge = this.getUserInputByShowingMessage("Overdue Charge (per day): $");
					String inputNoOfCopy = this.getUserInputByShowingMessage("Number of copies you want to add: ");
					
					ObjectEvent event = this._loadVideoBL.addVideo(inputTitle, 
							Float.parseFloat(inputRentalCharge),
							category,  
							Integer.parseInt(inputPeriod),  
							inputYear, 
							Float.parseFloat(inputOverdueCharge), 
							inputNoOfCopy);
					System.out.println(event.resultMessage);
					this.presentOption();
				
				//checkout video
				} else if (inputFunction.equals("3")) {
					List<Transaction> unrevieweds = this._checkVideo.getWaitingReviewTransactions();
					for (Transaction transaction : unrevieweds) {
						System.out.println(transaction);
						if (transaction.getVideoCopyIDs() != null) {
							ObjectEvent event = this._checkVideo.checkoutVideo(transaction, 
									this._loadVideoBL.getListOfVideoCopys(), 
									this._loadVideoBL.getListOfVideos());
							System.out.println(event.resultMessage);
						}
					}
					this.presentOption();
					
				//check return video
				} else if (inputFunction.equals("4")) {
					String custName = this.getUserInputByShowingMessage("\nReturn Video\n*********************\nEnter the Customers Name: ");
					String transcopyID = this.getUserInputByShowingMessage("Enter the Movie Copy ID: ");
					
					TransactionCopy transCopy = this._checkVideo.getTransactionCopy(transcopyID);
					Transaction trans = this._checkVideo.getTransaction(String.valueOf(transCopy.getTransacID()));
					VideoCopy videoCopy = this._loadVideoBL.getVideoCopy(String.valueOf(transCopy.getVideoCopyID()));
					Video video = this._loadVideoBL.getVideo(String.valueOf(videoCopy.getVideoID()));
					
					String dateReturn = this.getUserInputByShowingMessage("No of days it was rented for: ");
					String damaged = this.getUserInputByShowingMessage("Is the copy damaged? No = 1, Yes = 2: ");
					
					ObjectEvent objEvent = this._checkVideo.returnVideo(this._loginBL.getCustomerWithName(custName), 
							trans, 
							transCopy, 
							video, 
							videoCopy, 
							dateReturn, 
							damaged);
					System.out.println(objEvent.resultMessage);
					this.presentOption();
						
				//show all videos
				} else if (inputFunction.equals("5")) {
					this.showListOfVideos(this._loadVideoBL.getListOfVideos());
					this.presentOption();
                                        
                                        
                                  //exit system
                                } else if (inputFunction.equals("6")) {
			          System.out.println("\nThanks for using our system, logging out now...");
					
				//communicate with users
				} else if (inputFunction.equals("7")) {
					String inputTrans = this.getUserInputByShowingMessage("Email to transactionID : ");
					Transaction transac = this._checkVideo.getTransaction(inputTrans);
					if (transac != null) {
						Customer cust = this._loginBL.getCustomerWithID(transac.getCustID());
						
						try {
							ObjectEvent objEvent = this._checkVideo.emailCustomer(cust, 
									transac, 
									this._loadVideoBL.getListOfVideoCopys(), 
									this._loadVideoBL.getListOfVideos());
							if (objEvent.isSuccessful)
								System.out.println(objEvent.resultMessage);
						
						} catch (UnsupportedEncodingException e) {
							System.out.println(e);
							e.printStackTrace();
						}
					}
					this.presentOption();
				        }
		                
                                    //if incorrect option is chosen
                                     else{
                                     System.out.println("\nPlease select a valid option");
                                     this.presentOption();
                                }
		}
                }
		//Customer
		//1 : show detail, 2 : show function 
		else if (_typeUser == TypeUser.TypeUserCustomer) {
			if (inputOption.equals("1")) {
                            System.out.println("\nYour information");
				System.out.println(this._loginBL.getCurrentCustomer().toString());
			 this.presentOption();
			} else if (inputOption.equals("2")) {
				String promptFunction = "\nFunctions Page\n*********************\nPlease select an option\n1 = Show videos\n2 = Search Videos by Title\n3 = Search Videos by Category\n4 = Rent Video\n5 = Exit System\nSelect: ";
				String inputFunction = this.getUserInputByShowingMessage(promptFunction);
				
				//Show all videos
				if (inputFunction.equals("1")) {
					this.showListOfVideos(this._loadVideoBL.getListOfVideos());
					this.presentOption();
					
				//Search by title
				} else if (inputFunction.equals("2")) {
					String inputKeyword = this.getUserInputByShowingMessage("\nSearch movies by keyword\n*********************\nSearch: ");
					if (inputKeyword.length() > 0) {
						this.showListOfVideos(this._loadVideoBL.getSearchByKeywordListOfVideos(inputKeyword));
					}
					this.presentOption();
				
				//Search by category
				} else if (inputFunction.equals("3")) {
					String inputCategory = this.getUserInputByShowingMessage("\nSearch movies by Category\n*********************\n1 = Comedy\n2 = Action\n3 = Family\n4 = Drama\n");
					this.showListOfVideos(this._loadVideoBL.getSearchByCategoryListOfVideos(Categories.values()[Integer.valueOf(inputCategory) - 1]));
					this.presentOption();
				
				//Rent video
				} else if (inputFunction.equals("4")) {
					String inputNoOfVideoRent = this.getUserInputByShowingMessage("\nRenting Page\n*********************\nNumber of videos you want to rent: ");
					if (Integer.parseInt(inputNoOfVideoRent) > 5) {
						System.out.println("Unable to rent/reserve - You have reached the max limit of 5 active reservations");
						this.presentOption();
						return;
					}
					String listOfVideo = "";
					String listOfVideoReserve = "";
					for (int i = 0; i < Integer.parseInt(inputNoOfVideoRent); i++) {
						String inputVideoID = this.getUserInputByShowingMessage("Enter the Video Copy ID: ");
						ObjectEvent objEvent =  this._loadVideoBL.getVideoCopyFromVideoID(inputVideoID);
						Video video = (Video)objEvent.objResult;
						if (objEvent.resultMessage.equals(" unavailable")) {
							String inputReserve = this.getUserInputByShowingMessage("This movie is currently unavailable. Would you like to make a reservation? No = 1, Yes = 2: ");
							if (inputReserve.equals("2"))
								listOfVideoReserve += String.valueOf(video.getVideoCopy().getCopyID()) + ",";								
						
						} else if (objEvent.isSuccessful) {
							boolean isRent = this._loadVideoBL.rentVideo(video);
							if (isRent) {
								System.out.println(video.getTitle() + " has been added to the transaction");
								listOfVideo += String.valueOf(video.getVideoCopy().getCopyID()) + ",";
							}
						
						} else {
							i--;
							System.out.println(video.getTitle() + objEvent.resultMessage);
						}
					}
					if (listOfVideo != null && listOfVideo.length() > 1)
						listOfVideo = listOfVideo.substring(0, listOfVideo.length() - 1);
						
					if (listOfVideoReserve != null && listOfVideoReserve.length() > 1) 
						listOfVideoReserve = listOfVideoReserve.substring(0, listOfVideoReserve.length() - 1);
					
					if (this._checkVideo.createTransaction(this._loginBL.getCurrentCustomer(), listOfVideo, listOfVideoReserve))
						System.out.println("\n*********************\n\nTransaction succesful\n");

					this.presentOption();
				}
                                //exit system
                                  else if (inputFunction.equals("5")) {
			          System.out.println("\nThanks for using our system, logging out now...");
                              }
                                
                                //if incorrect option is chosen sends user back to menu
                                else{
                                     System.out.println("\nPlease select a valid option");
                                    this.presentOption();
                                }
			}
		}
        }
	       //returns a string of all the videos in the database
	       private void showListOfVideos(Map<String, Video> aVideos) {
		for (Map.Entry<String, Video> entry : aVideos.entrySet()) {
		    Video value = entry.getValue();
		    
		    System.out.println(value.toString() + "\n");
		}
	}
               
	//initialize scanner class for entire process in order to make sure that the performance will not be used uncessary   
	private String getUserInputByShowingMessage(String aMessage) {
		String input;
		try {
			if (scannerInput == null)
				scannerInput = new Scanner(System.in);
				
			System.out.print(aMessage);
			input = scannerInput.nextLine();
			return input;
		}
		catch (NumberFormatException aEx) {
			throw (aEx);
		}
	}
}
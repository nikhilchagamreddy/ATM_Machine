package co.codegnan.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.codegnan.cards.AxisDebitCards;
import com.codegnan.cards.HDFCDebitCards;
import com.codegnan.cards.OperatorCard;
import com.codegnan.cards.SBIDebitCards;
import com.codegnan.interfaces.IATMServices;
import com.codegnanexceptions.IncorrectPintLimitReachedException;
import com.codegnanexceptions.InsufficientBalanceException;
import com.codegnanexceptions.InsufficientMachineBalanceException;
import com.codegnanexceptions.InvalidAmountException;
import com.codegnanexceptions.InvalidCardException;
import com.codegnanexceptions.InvalidPinException;
import com.codegnanexceptions.NotOperatorException;


public class ATMOperations {
	public static double ATM_MACHINE_BALANCE=100000.00;
	
	public static ArrayList<String>ACTIVITY=new ArrayList<>();
//	database to map card numbers to card object
	public static HashMap<Long,IATMServices> dataBase=new HashMap<>();
//	flag to indicate if the ATM machine is on are of
	public static boolean MACHINE_ON = true;
//	refrence to the current card in use
	public static IATMServices card;
	
//	validate the inserted card by checking aganist the database.
	public static IATMServices validateCard(long cardNumber)throws InvalidCardException{
		if(dataBase.containsKey(cardNumber)) {
			return dataBase.get(cardNumber);
		}else {
			ACTIVITY.add("Accessed By " +cardNumber + "is not compatiable ");
			throw new InvalidCardException("This is not a Valid Card");
		}
	}
//	to check ATM Machine Activities
	public static void checkATMMachineActivities() {
		System.out.println("==================================Activities Performed=====================================");
		for(String activity:ACTIVITY) {
			System.out.println("=======================================================================================");
			System.out.println(activity);
			System.out.println("=======================================================================================");
		}
	}
	
//	Reset the Number of pin attempts for a user.
	public static void resetUserAttempts(IATMServices operatorCard) {
		IATMServices card = null;
		long number;
		Scanner scanner= new Scanner(System.in);
		System.out.println("Enter your Card Number : ");
		number = scanner.nextLong();
		try {
			card = validateCard(number);
			card.resetPinChances();
			ACTIVITY.add("Accessed By " + operatorCard.getUserName() +"to reset  number of chances for users ");
			
		}catch(InvalidCardException ive) {
			ive.printStackTrace();
		}
	}
	
//	validate user creadintials including PIN.
	public static IATMServices validCreadintials(long cardNumber, int pinNumber)
			throws InvalidCardException,InvalidPinException,IncorrectPintLimitReachedException{
		if(dataBase.containsKey(cardNumber)) {
			card=dataBase.get(cardNumber);
		}else {
			throw new InvalidCardException("This not a Valid Card");
		}
		try {
			if(card.getUserType().equals("operator")) {
				if(card.getPinNumber() !=pinNumber) {
					throw new InvalidPinException("");
				}else {
					return card;
				}
			}
		}catch(NotOperatorException noe) {
			noe.printStackTrace();
		}
		if (card.getChances() <= 0) {
			throw new IncorrectPintLimitReachedException(
					" you have Reached wrong limit of Pin Number, Which is 3 attempts");
		}
		if (card.getPinNumber() != pinNumber) {
			card.decreaseChances();// decrease the number of remaing chances.
			throw new InvalidPinException(" You have entered A wrong pin Number");
		} else {
			return card;
		}
	}

				
//	validate the amount for Withdrawl to ensure sufficient machine balance
	public static void validateAmount(double amount)throws InsufficientMachineBalanceException{
		if(amount > ATM_MACHINE_BALANCE) {
			throw new InsufficientMachineBalanceException("Insufficient cash in ");
		}
	}
	
	public static void validateDepositAmount(double amount)throws InvalidAmountException,InsufficientMachineBalanceException{
		if(amount % 100 !=0) {
			throw new InvalidAmountException("Please Deposit multiple of 100");
		}
		if(amount + ATM_MACHINE_BALANCE>200000.00) {
			ACTIVITY.add("UNable to Depoist Cast in the ATM Machine..");
			throw new InsufficientMachineBalanceException("you can't depoist cash as the limit of the machine");
		}
	}
	
	public static void operatorMode(IATMServices card) {
		Scanner scanner= new Scanner(System.in);
		double amount;
		boolean flag=true;
		while(flag) {
			System.out.println("Operator Mode : Operater name "+card.getUserName());
			System.out.println("=====================================================================================");
			System.out.println("");
			System.out.println("||                  0. Switch of the Machine                          ||");
			System.out.println("||                  1. Check ATM Machine Balance                      ||");
			System.out.println("||                  2. Depoist Cash in the Machine                    ||");
			System.out.println("||                  3. Reset user pin Atempts                         ||");
			System.out.println("||                  4. To check Activities performed in Atm Machine   ||");
			System.out.println("||                  5. Exit Operoter Mode                             ||");
			System.out.println("====================================================================================");
			System.out.println("Enter your Choice.....");
			int option=scanner.nextInt();
			switch(option) {
			case 0:
				MACHINE_ON = false;
				ACTIVITY.add("Accessed By "+card.getUserName()+"Activity Performed:Switching off the ATM Machine");
				flag = false;
				break;
			case 1:
				ACTIVITY.add(("Accessed By "+card.getUserName()+"Activity Performed"));
				System.out.println("Balance of the Atm Machine is : "+ ATM_MACHINE_BALANCE);
				break;
			case 2:
				System.out.println("Enter the amount to Depoist");
				amount = scanner.nextDouble();
				try {
					validateDepositAmount(amount);// validate the Deposit Amount
					ATM_MACHINE_BALANCE += amount;// update ATM Machine Balance
					ACTIVITY.add("Accessed By " + card.getUserName()
							+ " Activity Performed :  Deposit Cah in the ATM Machine");
					System.out.println("===================================================================");
					System.out.println("======================== Cash Added in the ATM Machine =====================");
					System.out.println(
							"=================================================================================");
				} catch (InvalidAmountException | InsufficientMachineBalanceException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				resetUserAttempts(card);// reset user Pin Attempts
				System.out.println("===================================================================");
				System.out.println("======================== User Attempts are Reset =====================");
				System.out.println("========================================================================");
				ACTIVITY.add("Accessed By " + card.getUserName()
						+ " Activity Performed : Resetting the Pin Attempts of user");
				break;
			case 4:
				checkATMMachineActivities();// Display ATM Activities.
				break;
			case 5:
				flag = false;
				break;
			default:
				System.out.println("You Have Entered A Wrong Option..");
			}
		}
	}

	public static void main(String[] args) throws NotOperatorException {
		dataBase.put(22222222221l, new AxisDebitCards("Mallesh",22222222221l,50000.0,2222));
		dataBase.put(33333333331l, new SBIDebitCards("Nagesh",33333333331l,25000.0,3333));
		dataBase.put(44444444441l, new HDFCDebitCards("Guna",44444444441l,35000.0,4444));
		dataBase.put(11111111111l, new OperatorCard(11111111111l,1111,"operator 1"));
		Scanner scanner= new Scanner(System.in);
		long cardNumber =0;
		double depoistAmount = 0.0;
		double withdrawAmount=0.0;
		int pin= 0;
		while(MACHINE_ON) {
			System.out.println("Please Enter the Debit card Number : ");
			cardNumber=scanner.nextLong();
			try {
				System.out.println("Please Enter PIN Number : ");
				pin=scanner.nextInt();
				card=validCreadintials(cardNumber,pin);
				
				if (card==null) {
					System.out.println("Card Validation Failed....");
					continue;
				}
				ACTIVITY.add("Accessed By "+card.getUserName()+"Status: Access Approved");
				if(card.getUserType().equals("operator")) {
					operatorMode(card);
					continue;
				}
				while(true) {
					System.out.println("USER MODE : "+card.getUserName());
					System.out.println("||============================================||");
					System.out.println("||                 1.Withdraw Amount          ||");
					System.out.println("||                 2.Depoist Amount           ||");
					System.out.println("||                 3.Check Balance            ||");
					System.out.println("||                 4.Change Pin               ||");
					System.out.println("||                 5.MINI Statement           ||");
					System.out.println("||============================================||");
					System.out.println("Enter Your Choice : ");
					int option=scanner.nextInt();
					try {
						switch(option) {
						case 1:
							System.out.println("Please Enter Withdraw Amount : ");
							withdrawAmount=scanner.nextDouble();
							validateAmount(withdrawAmount);
							card.withdrawAmount(withdrawAmount);
							ATM_MACHINE_BALANCE-=withdrawAmount;
							ACTIVITY.add("Accessed BY" +card.getUserName()+"Activity: amount withdraw"+withdrawAmount+"From Machine");
							break;
						case 2:
							System.out.println("Please Enter Depoist Amount : ");
							depoistAmount=scanner.nextDouble();
							validateDepositAmount(depoistAmount);
							card.depositAmount(depoistAmount);
							ATM_MACHINE_BALANCE-=depoistAmount;
							ACTIVITY.add("Accessed BY" +card.getUserName()+"Activity: Amount Depoist"+depoistAmount+"From Machine");
							break;
						case 3:
							System.out.println("Your Account Balance is :"+card.checkBalance());
							ACTIVITY.add("Accessed BY" +card.getUserName()+"Activity: Checking the Balance");
							break;
						case 4:
							System.out.println("Enter a New PIN");
							pin=scanner.nextInt();
							ACTIVITY.add("Accessed BY" +card.getUserName()+"Activity: Chhanged Pin Number");
							break;
						case 5:
							ACTIVITY.add("Accessed BY" +card.getUserName()+"Activity: Genarating MINI Statement");
							card.getMiniStatement();
							break;
							default:
								System.out.println("You have Entered A Wrong Option.......");
								break;
						}
						System.out.println("Do you Want to Continue...");
						String nextOption =scanner.next();
						if(nextOption.equalsIgnoreCase("n")) {
							break;
						}
						
					}catch (InvalidAmountException|InsufficientBalanceException|InsufficientMachineBalanceException e) {
						e.printStackTrace();
					}
				}
					
				}catch(InvalidPinException| InvalidCardException | IncorrectPintLimitReachedException e) {
					ACTIVITY.add("Accessed by "+card.getUserName()+"Status : Access Denied...");
					e.printStackTrace();
			}
			System.out.println("=============================================================================");
			System.out.println("==================Thanks For Using Axis Bank ATM Machine=====================");
			System.out.println("=============================================================================");
		}

	}

	}
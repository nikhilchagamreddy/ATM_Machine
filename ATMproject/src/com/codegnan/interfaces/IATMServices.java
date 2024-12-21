package com.codegnan.interfaces;

import com.codegnanexceptions.InsufficientBalanceException;
import com.codegnanexceptions.InsufficientMachineBalanceException;
import com.codegnanexceptions.InvalidAmountException;
import com.codegnanexceptions.NotOperatorException;


public interface IATMServices {
public abstract String getUserType() throws NotOperatorException;
	
	public abstract double withdrawAmount(double withAmount)throws InvalidAmountException,InsufficientBalanceException,InsufficientMachineBalanceException;
	
//	to deposit Amount
	public abstract double depositAmount(double dptAmount)throws InvalidAmountException;
	
//	to check Balance
	public abstract double checkBalance();
	
//	Change Pin Number
	public abstract void changePinNumber(int pinNumber);
	
//	to get pin number
	public abstract int getPinNumber();
	
//	to get user Name
	public abstract String getUserName();
//	to get changes in pin
	public abstract int getChances();
//	
	public abstract void decreaseChances();
	
	public abstract void resetPinChances();
	
//	to get the ministatement of an account
	public abstract void getMiniStatement();

}

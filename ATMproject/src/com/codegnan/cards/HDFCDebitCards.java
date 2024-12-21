package com.codegnan.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.codegnan.interfaces.IATMServices;
import com.codegnanexceptions.InsufficientBalanceException;
import com.codegnanexceptions.InsufficientMachineBalanceException;
import com.codegnanexceptions.InvalidAmountException;
import com.codegnanexceptions.NotOperatorException;

public class HDFCDebitCards implements IATMServices {
	String name;
	long debitCardNumber;
	double accountBalance;
	int pinNumber;
	ArrayList<String>statement;
	final String type="user";
	int chances;
	public HDFCDebitCards(String name, long debitCardNumber, double accountBalance, int pinNumber
			) {
		chances=3;
		
		this.name = name;
		
		this.accountBalance = accountBalance;
		this.pinNumber = pinNumber;
		statement = new ArrayList<>();
	}
	@Override
	public String getUserType() throws NotOperatorException {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public double withdrawAmount(double withAmount)
			throws InvalidAmountException, InsufficientBalanceException, InsufficientMachineBalanceException {
		if(withAmount<=0) {
			throw new InvalidAmountException("You can't enter Zero Amount to withdraw");
		}else if(withAmount%100!=0) {
			throw new InvalidAmountException("Please Withdraw multiple of 100");
		}else if(withAmount < 500) {
			throw new InvalidAmountException("Please Withdraw morethan 500..");
		}else if(withAmount >accountBalance) {
			throw new InsufficientBalanceException("you don't have sufficient Balance to Withdraw");
		}else {
			accountBalance = accountBalance -withAmount;
			statement.add("Debited : "+ withAmount);
			return withAmount;
		}
		
	}
	@Override
	public double depositAmount(double dptAmount) throws InvalidAmountException {
		if(dptAmount<=500) {
			throw new InvalidAmountException("You can't Deposit Zero Amount to withdraw.Please Depoist morethan 500..");
		}else if(dptAmount%100!=0) {
			throw new InvalidAmountException("Please Depoist multiple of 100");
		}else {
			accountBalance = accountBalance - dptAmount;
			statement.add("creadited : "+ dptAmount);
			return dptAmount;
		}
		
	}
	@Override
	public double checkBalance() {
		
		return accountBalance;
	}
	@Override
	public void changePinNumber(int pinNumber) {
		this.changePinNumber(pinNumber);
		
	}
	@Override
	public int getPinNumber() {
		// TODO Auto-generated method stub
		return pinNumber;
	}
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return name;
	}
	@Override
	public int getChances() {
		// TODO Auto-generated method stub
		return chances;
	}
	@Override
	public void decreaseChances() {
		--chances;
		
	}
	@Override
	public void resetPinChances() {
		chances=3;
		
	}
	@Override
	public void getMiniStatement() {
		int count =5;
		if(statement.size()==0) {
			System.out.println("There are no transactions happend..");
			return;
		}
		System.out.println("=====================================Last 5 transactions=================================");
		Collections.reverse(statement);
		for(String trans : statement) {
			if(count == 0) {
				break;
			}
			System.out.println(trans);
			count--;
		}
		Collections.reverse(statement);
		
	}
	

}

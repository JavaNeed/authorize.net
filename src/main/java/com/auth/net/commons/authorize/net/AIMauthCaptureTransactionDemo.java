package com.auth.net.commons.authorize.net;

import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CreditCard;

public class AIMauthCaptureTransactionDemo {
	public static final String apiLoginID= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";

	public static void main(String[] args) {
		Merchant merchant = Merchant.createMerchant(Environment.SANDBOX, apiLoginID, transactionKey);
		// create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber("4111 1111 1111 1111");
		creditCard.setExpirationMonth("12");
		creditCard.setExpirationYear("2018");

		// create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction
				(TransactionType.AUTH_CAPTURE, new BigDecimal("50.00"));

		authCaptureTransaction.setCreditCard(creditCard);

		@SuppressWarnings("unchecked")
		Result<Transaction> result = (Result<Transaction>)merchant.postTransaction(authCaptureTransaction);

		if(result.isApproved()) {
			System.out.println("Response Code : "+ result.getReasonResponseCode());
			System.out.println("Response Text : " + result.getResponseText());
			System.out.println("Transaction Id: " + result.getTarget().getTransactionId());
			System.out.println("AuthorizationCode : "+result.getTarget().getAuthorizationCode());
		} 
		else if (result.isDeclined()) {
			System.out.println(result.getReasonResponseCode() + " : " + result.getResponseText());
		}
		else {
			System.out.println(result.getReasonResponseCode() + " : " + result.getResponseText());
		}
	}

}
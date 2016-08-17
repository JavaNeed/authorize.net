package com.auth.net.commons.authorize.net;

import java.util.ArrayList;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.Transaction;
import net.authorize.data.xml.reporting.ReportingDetails;
import net.authorize.data.xml.reporting.TransactionDetails;
import net.authorize.reporting.Result;
import net.authorize.reporting.TransactionType;

public class UnsettledTransactionDetailsDemo {
	/*private static final String apiLoginID ="72mNC7gyq";
	private static final String transactionKey ="8W6YC22g58PrkEvA";*/
	
	public static final String apiLoginID= "6LaBc8HJ6Q";
	public static final String transactionKey= "5tn5n827E8YT23qk";
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		Merchant merchant = Merchant.createMerchant(Environment.SANDBOX, apiLoginID, transactionKey);
		
		// get the list of Unsettled transactions 
		net.authorize.reporting.Transaction transaction =
				merchant.createReportingTransaction(TransactionType.GET_UNSETTLED_TRANSACTION_LIST);

		ReportingDetails reportingDetails = ReportingDetails.createReportingDetails();
		reportingDetails.setBatchIncludeStatistics(true);
		transaction.setReportingDetails(reportingDetails);
		
		Result<Transaction> result =(Result<Transaction>) merchant.postTransaction(transaction);

		System.out.println("======= Unsettled Transaction List ==============");
		System.out.println("Result Code : ["+ result.getResultCode() +"]");
		System.out.println("Code        : ["+ result.getMessages().get(0).getCode() +"]");
		System.out.println("Code        : ["+ result.getMessages().get(0).getText() +"]");

		ArrayList<TransactionDetails> newReportingDetails = result.getReportingDetails().getTransactionDetailList();
		for (int i = 0; i < newReportingDetails.size(); i++) {
			TransactionDetails td = newReportingDetails.get(i);
			System.out.println("------------------------------------");
			System.out.println("Transaction ID     : [" + td.getTransId()+"]");
			System.out.println("Transaction Status : ["+td.getTransactionStatus()+"]");
			System.out.println("Transaction Type   : [" + td.getTransactionType()+"]");
			System.out.println("Account Number     : [" + td.getAccountNumber() +"]");
			System.out.println("Account Tyep       : ["+ td.getAccountType() +"]");
			System.out.println("Settled Amount     : ["+ td.getSettleAmount()+"]");
			System.out.println("Submit TIme Local  : ["+td.getSubmitTimeLocal()+"]");
			System.out.println("Submit TIme UTC    : ["+td.getSubmitTimeUTC()+"]");
		}
	}
}


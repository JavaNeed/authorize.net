package com.auth.net.commons.authorize.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.Transaction;
import net.authorize.data.xml.reporting.BatchDetails;
import net.authorize.data.xml.reporting.BatchStatistics;
import net.authorize.data.xml.reporting.ReportingDetails;
import net.authorize.reporting.Result;
import net.authorize.reporting.TransactionType;

public class SettledTransactionDetails {
	public static final String apiLoginID= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";
	
	/*public static final String apiLoginID= "6LaBc8HJ6Q";
	public static final String transactionKey= "5tn5n827E8YT23qk";*/
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ParseException {
		Merchant merchant = Merchant.createMerchant(Environment.SANDBOX, apiLoginID, transactionKey);

		// get the list of Unsettled transactions 
		net.authorize.reporting.Transaction transaction =
				merchant.createReportingTransaction(TransactionType.GET_SETTLED_BATCH_LIST);

		ReportingDetails reportingDetails = ReportingDetails.createReportingDetails();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		reportingDetails.setBatchFirstSettlementDate(formatter.parse("21/07/2015"));
		reportingDetails.setBatchLastSettlementDate(formatter.parse("18/08/2015"));
		reportingDetails.setBatchIncludeStatistics(true);
		transaction.setReportingDetails(reportingDetails);

		Result<Transaction> result =(Result<Transaction>) merchant.postTransaction(transaction);
		System.out.println("----------------------------------------------------------");
		System.out.println("Result Code : ["+ result.getResultCode() +"]");
		System.out.println("Code        : ["+ result.getMessages().get(0).getCode() +"]");
		System.out.println("Code        : ["+ result.getMessages().get(0).getText() +"]");

		ArrayList<BatchDetails> batchDetailsList = result.getReportingDetails().getBatchDetailsList();
		for (int i = 0; i < batchDetailsList.size(); i++) {
			ArrayList<BatchStatistics> batchStatisticsList = batchDetailsList.get(i).getBatchStatisticsList();
			for (int j = 0; j < batchStatisticsList.size(); j++) {
				BatchStatistics batchStatistics = batchStatisticsList.get(j);
				System.out.println("====================== "  + j+ " start");
				System.out.println("Account Type : [" + batchStatistics.getAccountType()+"]");
				System.out.println("Charge Amount : [" + batchStatistics.getChargeAmount()+"]");
				System.out.println("Charge BackAmount : [" + batchStatistics.getChargebackAmount()+"]");
				System.out.println("Charge Charge Back Amount : [" + batchStatistics.getChargeChargebackAmount()+"]");
				System.out.println("Charge Returned Items Amount [: " + batchStatistics.getChargeReturnedItemsAmount()+"]");
				System.out.println("Refund Amount : [" + batchStatistics.getRefundAmount()+"]");
				System.out.println("Refund Charge Back Amount : [" + batchStatistics.getRefundChargebackAmount()+"]");
				System.out.println("Account Type : [" + batchStatistics.getAccountType()+"]");
				System.out.println("====================== "  + j+ " end");
			}
		}
		
	}
}

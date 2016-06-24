package com.auth.net.commons.authorize.transactionReporting;

import java.util.List;

import net.authorize.Environment;
import net.authorize.api.contract.v1.ArrayOfTransactionSummaryType;
import net.authorize.api.contract.v1.GetUnsettledTransactionListRequest;
import net.authorize.api.contract.v1.GetUnsettledTransactionListResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.TransactionSummaryType;
import net.authorize.api.controller.GetUnsettledTransactionListController;
import net.authorize.api.controller.base.ApiOperationBase;

public class GetUnsettledTransactionList {
	public static final String apiLoginId= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";

	public static void main(String[] args) {
		// Set Environment
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		// Merchant Authentication Type
		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginId);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		// Get Unsettled Transactions
		GetUnsettledTransactionListRequest getRequest = new GetUnsettledTransactionListRequest();
		getRequest.setMerchantAuthentication(merchantAuthenticationType);


		GetUnsettledTransactionListController controller = new GetUnsettledTransactionListController(getRequest);
		controller.execute();
		GetUnsettledTransactionListResponse getResponse = controller.getApiResponse();

		if (getResponse!=null) {

			if (getResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {
				System.out.println(getResponse.getMessages().getMessage().get(0).getCode());
				System.out.println(getResponse.getMessages().getMessage().get(0).getText());
				
				ArrayOfTransactionSummaryType transactions = getResponse.getTransactions();
				List<TransactionSummaryType> transactionSummaryTypes = transactions.getTransaction();
				
				for (TransactionSummaryType transactionSummaryType : transactionSummaryTypes) {
					System.out.println("-------------------------");
					System.out.println("Transaction ID       : "+transactionSummaryType.getTransId());
					System.out.println("Account No           : "+transactionSummaryType.getAccountNumber());
					System.out.println("Account Type         : "+transactionSummaryType.getAccountType());
					System.out.println("Market Type          : "+transactionSummaryType.getMarketType());
					System.out.println("Product              : "+transactionSummaryType.getProduct());
					System.out.println("Settle Amount        : "+transactionSummaryType.getSettleAmount());
					System.out.println("Submit Time Lical    : "+transactionSummaryType.getSubmitTimeLocal());
					System.out.println("Submit Time UTC      : "+transactionSummaryType.getSubmitTimeUTC());
					System.out.println("TransactionStatus    : "+transactionSummaryType.getTransactionStatus());
				}
			}
			else{
				System.out.println("Failed to get unsettled transaction list:  " + getResponse.getMessages().getResultCode());
			}
		}

	}
}

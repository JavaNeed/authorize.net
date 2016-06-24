package com.auth.net.commons.authorize.transactionReporting;

import net.authorize.Environment;
import net.authorize.api.contract.v1.GetTransactionDetailsRequest;
import net.authorize.api.contract.v1.GetTransactionDetailsResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.TransactionDetailsType;
import net.authorize.api.controller.GetTransactionDetailsController;
import net.authorize.api.controller.base.ApiOperationBase;

public class GetTransactionDetails {
	public static void main(String[] args) {
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName("533f4XCx8"); //apiLoginId
		merchantAuthenticationType.setTransactionKey("2ppZ9M8K33aED4vt");

		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		//need valid transacaction Id to run  
		String transId = "2227140854";

		GetTransactionDetailsRequest getRequest = new GetTransactionDetailsRequest();
		getRequest.setMerchantAuthentication(merchantAuthenticationType);
		getRequest.setTransId(transId);

		GetTransactionDetailsController controller = new GetTransactionDetailsController(getRequest);
		controller.execute();
		GetTransactionDetailsResponse getResponse = controller.getApiResponse();

		TransactionDetailsType transactionDetailsType = getResponse.getTransaction();

		if (getResponse!=null) {
			if (getResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {
				System.out.println(getResponse.getMessages().getMessage().get(0).getCode());
				System.out.println(getResponse.getMessages().getMessage().get(0).getText());
			}else{
				System.out.println("Failed to get transaction details:  " + getResponse.getMessages().getResultCode());
			}
		}

		System.out.println("---------------------------------------");
		System.out.println("Auth Amount                 : "+transactionDetailsType.getAuthAmount());
		System.out.println("Auth Code                   : "+transactionDetailsType.getAuthCode());
		System.out.println("Response Reason Description : "+transactionDetailsType.getResponseReasonDescription());
		System.out.println("Transaction Status          : "+transactionDetailsType.getTransactionStatus());
		System.out.println("Submit Date                 : "+transactionDetailsType.getSubmitTimeLocal());
	}
}

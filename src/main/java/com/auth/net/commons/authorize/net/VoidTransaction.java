package com.auth.net.commons.authorize.net;

import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;

public class VoidTransaction {
	public static final String apiLoginID= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";

	public static String authtransactionId = null;
	public static String CaptureTransactionId = null;

	public static void main(String[] args) {
		//Common code to set for all requests
		ApiOperationBase.setEnvironment(Environment.SANDBOX);	

		MerchantAuthenticationType merchantAuthenticationType= new MerchantAuthenticationType();
		merchantAuthenticationType.setName(apiLoginID);
		merchantAuthenticationType.setTransactionKey(transactionKey);

		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		// Populate the payment data
		PaymentType paymentType = new PaymentType();
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4242424242424242");
		creditCard.setExpirationDate("0822");
		paymentType.setCreditCard(creditCard);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_ONLY_TRANSACTION.value());
		txnRequest.setPayment(paymentType);
		txnRequest.setAmount(new BigDecimal(140.00));

		// Make the API Request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();

		CreateTransactionResponse response = controller.getApiResponse();
		if (response!=null) {

			// If API Response is ok, go ahead and check the transaction response
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				TransactionResponse result = response.getTransactionResponse();
				if (result.getResponseCode().equals("1")) {
					System.out.println("-----------------------------");
					System.out.println("-------- Authorized Transaction Request --------");
					System.out.println("Transaction ID  :" +result.getTransId());
					authtransactionId = result.getTransId();
					System.out.println("Account No      : "+result.getAccountNumber());
					System.out.println("Account Type    : "+result.getAccountType());
					System.out.println("AuthCode        : "+result.getAuthCode());
					System.out.println("AVSResult Code  : "+result.getAvsResultCode());
					System.out.println("CAVV Result Code: "+result.getCavvResultCode());
					System.out.println("Response Code   : "+result.getResponseCode());
					System.out.println("Test Request    : "+result.getTestRequest());
					System.out.println("Trans Hash      : "+result.getTransHash());
					System.out.println("Result Code     : "+result.getResponseCode());
					System.out.println("Message Code    : "+result.getMessages().getMessage().get(0).getCode());
					System.out.println("Message Text    : "+result.getMessages().getMessage().get(0).getDescription());
					System.out.println("Successfully Authorized Transaction");
				}
			}
		}




		//
		// Create the payment transaction request
		TransactionRequestType captureTxnRequest = new TransactionRequestType();
		captureTxnRequest.setTransactionType(TransactionTypeEnum.PRIOR_AUTH_CAPTURE_TRANSACTION.value());
		captureTxnRequest.setRefTransId(authtransactionId);

		// Make the API Request
		CreateTransactionRequest captureApiRequest = new CreateTransactionRequest();
		captureApiRequest.setTransactionRequest(captureTxnRequest);
		CreateTransactionController captureController = new CreateTransactionController(captureApiRequest);
		captureController.execute(); 

		CreateTransactionResponse captureResponse = captureController.getApiResponse();


		if (captureResponse !=null) {

			// If API Response is ok, go ahead and check the transaction response
			if (captureResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {

				TransactionResponse result = captureResponse.getTransactionResponse();
				if (result.getResponseCode().equals("1")) {
					System.out.println("-------- Capture Transaction Request --------");
					System.out.println("Transaction ID  :" +result.getTransId());
					CaptureTransactionId = result.getTransId();
					System.out.println("Account No      : "+result.getAccountNumber());
					System.out.println("Account Type    : "+result.getAccountType());
					System.out.println("AuthCode        : "+result.getAuthCode());
					System.out.println("AVSResult Code  : "+result.getAvsResultCode());
					System.out.println("CAVV Result Code: "+result.getCavvResultCode());
					System.out.println("Response Code   : "+result.getResponseCode());
					System.out.println("Test Request    : "+result.getTestRequest());
					System.out.println("Trans Hash      : "+result.getTransHash());
					System.out.println("Result Code     : "+result.getResponseCode());
					System.out.println("Message Code    : "+result.getMessages().getMessage().get(0).getCode());
					System.out.println("Message Text    : "+result.getMessages().getMessage().get(0).getDescription());
					System.out.println("Successfully Captured Transaction");
				}
				else{
					System.out.println("Failed Transaction"+result.getResponseCode());
				}
			}
			else{
				System.out.println("Failed Transaction:  "+response.getMessages().getResultCode());
			}
		}


		// Void
		// Create the payment transaction request
		TransactionRequestType voidTxnRequest = new TransactionRequestType();
		voidTxnRequest.setTransactionType(TransactionTypeEnum.VOID_TRANSACTION.value());
		voidTxnRequest.setRefTransId(CaptureTransactionId);

		// Make the API Request
		CreateTransactionRequest voidApiRequest = new CreateTransactionRequest();
		voidApiRequest.setTransactionRequest(voidTxnRequest);
		CreateTransactionController voidController = new CreateTransactionController(voidApiRequest);
		voidController.execute(); 

		CreateTransactionResponse voidResponse = voidController.getApiResponse();

		if (voidResponse != null) {
			// If API Response is ok, go ahead and check the transaction response
			if (voidResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {

				TransactionResponse result = voidResponse.getTransactionResponse();
				if (result.getResponseCode().equals("1")) {
					System.out.println("--------- Transaction Voided ----------");
					System.out.println("Transaction ID  :" +result.getTransId());
					System.out.println("Account No      : "+result.getAccountNumber());
					System.out.println("Account Type    : "+result.getAccountType());
					System.out.println("AuthCode        : "+result.getAuthCode());
					System.out.println("AVSResult Code  : "+result.getAvsResultCode());
					System.out.println("CAVV Result Code: "+result.getCavvResultCode());
					System.out.println("Response Code   : "+result.getResponseCode());
					System.out.println("Test Request    : "+result.getTestRequest());
					System.out.println("Trans Hash      : "+result.getTransHash());
					System.out.println("Result Code     : "+result.getResponseCode());
					System.out.println("Message Code    : "+result.getMessages().getMessage().get(0).getCode());
					System.out.println("Message Text    : "+result.getMessages().getMessage().get(0).getDescription());
					System.out.println("Successfully Voided Transaction");
				}
				else{
					System.out.println("Failed Transaction"+result.getResponseCode());
				}
			}
			else{
				System.out.println("Failed Transaction:  "+response.getMessages().getResultCode());
			}
		}
	}
}

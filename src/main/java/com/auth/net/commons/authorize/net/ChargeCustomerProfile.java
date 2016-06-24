package com.auth.net.commons.authorize.net;

import java.math.BigDecimal;
import java.util.List;

import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.MessagesType;
import net.authorize.api.contract.v1.MessagesType.Message;
import net.authorize.api.contract.v1.PaymentProfile;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;

public class ChargeCustomerProfile {
	public static final String apiLoginId= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";

	public static void main(String[] args) {
		//Common code to set for all requests
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginId);
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
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		txnRequest.setPayment(paymentType);
		txnRequest.setAmount(new BigDecimal(500.00));

		PaymentProfile paymentProfile = new PaymentProfile();
		paymentProfile.setPaymentProfileId("25000332");

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
					System.out.println("----------------------------------");
					System.out.println("Account Number : "+result.getAccountNumber());
					System.out.println("Account Type   : "+result.getAccountType());
					System.out.println("Auth Code      : "+result.getAuthCode());
					System.out.println("AVS Result Code: "+result.getAvsResultCode());
					System.out.println("CAVV Result Code: "+result.getCavvResultCode());
					System.out.println("TransID     : "+result.getTransId());
					System.out.println("Response Code  : "+result.getResponseCode());

					List<Message> messagesType = response.getMessages().getMessage();
					for (Message message : messagesType) {
						System.out.println("Message Code : "+message.getCode());
						System.out.println("Message Text : "+message.getText());
					}
				} else{
					System.out.println("Failed Transaction"+result.getResponseCode());
				}
			} else{
				System.out.println("Failed Transaction:  "+response.getMessages().getResultCode());
			}
		}
	}
}

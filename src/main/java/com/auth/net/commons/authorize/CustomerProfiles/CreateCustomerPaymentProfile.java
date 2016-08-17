package com.auth.net.commons.authorize.CustomerProfiles;

import java.util.List;

import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.MessagesType.Message;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.controller.CreateCustomerPaymentProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

public class CreateCustomerPaymentProfile {
	public static final String apiLoginID= "5KP3u95bQpv";
	public static final String transactionKey= "4Ktq966gC55GAX7S";
	
	private static final String customerProfileId = "37400903";

	public static void main(String[] args) {
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginID);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		//private String getPaymentDetails(MerchantAuthenticationType merchantAuthentication, String customerprofileId, ValidationModeEnum validationMode) {
		CreateCustomerPaymentProfileRequest apiRequest = new CreateCustomerPaymentProfileRequest();
		apiRequest.setMerchantAuthentication(merchantAuthenticationType);
		apiRequest.setCustomerProfileId(customerProfileId);	

		//customer address
		CustomerAddressType customerAddress = new CustomerAddressType();
		customerAddress.setFirstName("Savani");
		customerAddress.setLastName("Khobragade");
		customerAddress.setAddress("123 Main Street");
		customerAddress.setCity("Bellevue");
		customerAddress.setState("WA");
		customerAddress.setZip("98004");
		customerAddress.setCountry("USA");
		customerAddress.setPhoneNumber("000-000-0000");

		//credit card details
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4111111111111111");
		creditCard.setExpirationDate("2023-12");
		creditCard.setCardCode("122");

		CustomerPaymentProfileType profile = new CustomerPaymentProfileType();
		profile.setBillTo(customerAddress);

		PaymentType payment = new PaymentType();
		payment.setCreditCard(creditCard);
		profile.setPayment(payment);

		apiRequest.setPaymentProfile(profile);

		CreateCustomerPaymentProfileController controller = new CreateCustomerPaymentProfileController(apiRequest);
		controller.execute();

		CreateCustomerPaymentProfileResponse response = new CreateCustomerPaymentProfileResponse();
		response = controller.getApiResponse();
		if (response != null) {
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				System.out.println(response.getCustomerPaymentProfileId());
				System.out.println(response.getMessages().getMessage().get(0).getCode());
				System.out.println(response.getMessages().getMessage().get(0).getText());
				if(response.getValidationDirectResponse() != null)
					System.out.println(response.getValidationDirectResponse());
			}
			else {
				System.out.println("Failed to create customer payment profile:  " + response.getMessages().getResultCode());
				System.out.println("~~~~ Details Are ~~~~");
				List<Message> messages = response.getMessages().getMessage();
				for (Message message : messages) {
					System.out.println("Message Code  : "+message.getCode());
					System.out.println("Message Text  : "+message.getText());
				}
			}
		}
	}
}

package com.auth.net.commons.authorize.CustomerProfiles;

import java.security.SecureRandom;

import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateCustomerProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerProfileResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.CustomerProfileType;
import net.authorize.api.contract.v1.CustomerTypeEnum;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.ValidationModeEnum;
import net.authorize.api.controller.CreateCustomerProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

public class CreateCustomerProfile {
	public static final String apiLoginID= "5KP3u95bQpv";
	public static final String transactionKey= "4Ktq966gC55GAX7S";
	
	static SecureRandom rgenerator = new SecureRandom();
	
	private static String getEmail() {
		return rgenerator.nextInt(10000) + "@test.com";
	}

	public static void main(String[] args) {
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginID);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		// Credit Card Details
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4111111111111111");
		creditCard.setExpirationDate("1222");

		// Payment Type
		PaymentType paymentType = new PaymentType();
		paymentType.setCreditCard(creditCard);

		// Customer Payment Profile
		CustomerPaymentProfileType customerPaymentProfileType = new CustomerPaymentProfileType();
		customerPaymentProfileType.setCustomerType(CustomerTypeEnum.INDIVIDUAL);
		customerPaymentProfileType.setPayment(paymentType);

		// Customer Profile Type
		CustomerProfileType customerProfileType = new CustomerProfileType();
		customerProfileType.setMerchantCustomerId("M_" + getEmail());
		customerProfileType.setDescription("Profile description here");
		customerProfileType.setEmail(getEmail());
		customerProfileType.getPaymentProfiles().add(customerPaymentProfileType);

		// Create Customer Profile Request
		CreateCustomerProfileRequest apiRequest = new CreateCustomerProfileRequest();
		apiRequest.setProfile(customerProfileType);
		apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);

		// create controller
		CreateCustomerProfileController controller = new CreateCustomerProfileController(apiRequest);
		controller.execute();

		// Response
		CreateCustomerProfileResponse response = controller.getApiResponse();
		if (response != null) {
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				System.out.println("Customer Profile ID   : " +response.getCustomerProfileId());
				if(!response.getCustomerPaymentProfileIdList().getNumericString().isEmpty())
					System.out.println("Customer Payment Profile ID : ["+response.getCustomerPaymentProfileIdList().getNumericString().get(0)+"]");

				if(!response.getCustomerShippingAddressIdList().getNumericString().isEmpty())
					System.out.println("Customer Shipping Address : [" +response.getCustomerShippingAddressIdList().getNumericString().get(0)+"]");

				if(!response.getValidationDirectResponseList().getString().isEmpty())
					System.out.println("Validation Direct Response : ["+response.getValidationDirectResponseList().getString().get(0)+"]");
			}
			else{
				System.out.println("Failed to create customer profile:  [" + response.getMessages().getResultCode()+"]");
			}
		}
	}
}

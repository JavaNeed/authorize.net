package com.auth.net.commons.authorize.transactionReporting;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import net.authorize.Environment;
import net.authorize.api.contract.v1.ArrayOfBatchDetailsType;
import net.authorize.api.contract.v1.BatchDetailsType;
import net.authorize.api.contract.v1.GetSettledBatchListRequest;
import net.authorize.api.contract.v1.GetSettledBatchListResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.controller.GetSettledBatchListController;
import net.authorize.api.controller.base.ApiOperationBase;

public class GetSettledBatchList {
	/*public static final String apiLoginId= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";*/

	public static final String apiLoginId= "6LaBc8HJ6Q";
	public static final String transactionKey= "5tn5n827E8YT23qk";
	
	public static void main(String[] args) throws ParseException, DatatypeConfigurationException {
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType= new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginId);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		GetSettledBatchListRequest getRequest = new GetSettledBatchListRequest();
		getRequest.setMerchantAuthentication(merchantAuthenticationType);

		try {
			// Set first settlement date in format (year, month, day)(should not be less that 31 days since last settlement date)
			GregorianCalendar pastDate = new GregorianCalendar();
			pastDate.add(Calendar.DAY_OF_YEAR, -7);
			System.out.println("Past Date : "+pastDate);
			getRequest.setFirstSettlementDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(pastDate));

			// Set last settlement date in format (year, month, day) (should not be greater that 31 days since first settlement date)
			GregorianCalendar currentDate = new GregorianCalendar();
			getRequest.setLastSettlementDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(currentDate));

		} catch (Exception ex) {
			System.out.println("Error : while setting dates");
			ex.printStackTrace();
		}

		GetSettledBatchListController controller = new GetSettledBatchListController(getRequest);
		controller.execute();
		GetSettledBatchListResponse getResponse = controller.getApiResponse();
		if (getResponse != null) {

			if (getResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {

				System.out.println(getResponse.getMessages().getMessage().get(0).getCode());
				System.out.println(getResponse.getMessages().getMessage().get(0).getText());

				ArrayOfBatchDetailsType batchList = getResponse.getBatchList();
				if (batchList != null) {
					System.out.println("List of Settled Transaction :");
					for (BatchDetailsType batch : batchList.getBatch()) {
						System.out.println(batch.getBatchId() + " - " + batch.getMarketType() + " - " + batch.getPaymentMethod() + " - " + batch.getProduct() + " - " + batch.getSettlementState());
					}
				}
			} else {
				System.out.println("Failed to get settled batch list:  " + getResponse.getMessages().getResultCode());
				System.out.println(getResponse.getMessages().getMessage().get(0).getText());
			}
		}
	}
}


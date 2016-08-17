package com.auth.net.commons.authorize.transactionReporting;

import net.authorize.Environment;
import net.authorize.api.contract.v1.GetBatchStatisticsRequest;
import net.authorize.api.contract.v1.GetBatchStatisticsResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.controller.GetBatchStatisticsController;
import net.authorize.api.controller.base.ApiOperationBase;

public class GetBatchStatistics {
	/*public static final String apiLoginId= "72mNC7gyq";
	public static final String transactionKey= "8W6YC22g58PrkEvA";*/
	
	public static final String apiLoginId= "6LaBc8HJ6Q";
	public static final String transactionKey= "5tn5n827E8YT23qk";

	public static void main(String[] args) {
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(apiLoginId);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		GetBatchStatisticsRequest getRequest = new GetBatchStatisticsRequest();
		getRequest.setMerchantAuthentication(merchantAuthenticationType);
		getRequest.setBatchId("12345");

		GetBatchStatisticsController controller = new GetBatchStatisticsController(getRequest);
		controller.execute();
		GetBatchStatisticsResponse getResponse = controller.getApiResponse();

		System.out.println("------------------------------------------------------");
		if (getResponse!=null) {

			if (getResponse.getMessages().getResultCode() == MessageTypeEnum.OK) {
				System.out.println("Code  : ["+getResponse.getMessages().getMessage().get(0).getCode()+"]");
				System.out.println("Text  : ["+getResponse.getMessages().getMessage().get(0).getText()+"]");
			}
			else {
				System.out.println("Failed to get batch statistics:  " + getResponse.getMessages().getResultCode());
			}
		}
	}
}

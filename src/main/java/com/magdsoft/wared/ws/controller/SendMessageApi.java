//created by ahmed elsayed 5_6_2017
package com.magdsoft.wared.ws.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.magdsoft.wared.ws.form.ApiSendMessage;


@Service
public class SendMessageApi {
	
	
	/**
	 * 
	 * @param api object of apidsendMessage that include username,pass,messgae,unicode,numbers,
	 * sendername,datetime and return result(json,xml,string)
	 * send map to this api include parameteres of SenfMessageApi
	 * @return error code from RestTemplate().postForObject that take api url ,parameter,
	 * type of result and Hashmap
	 */
	@Async
	public  Map< String , Object> sendSms(ApiSendMessage api) {
		Map<String, Object> ret = new HashMap<>();
	
		try{
		ret.put("username",api.getUserName());
		ret.put("password", api.getPassword());
		ret.put("message", api.getMessage());
		ret.put("numbers",api.getNumbers());
		ret.put("sender", api.getSender());
		ret.put("return",api.getReturnValue());
		ret.put("unicode",api.getUnicode());
//		ret.put("datetime",api.getDatetime());
		
		
		RestTemplate restTemplate = new RestTemplate();
		String apiSend =  restTemplate.getForObject(
     			"http://www.ksa-sms.com/api/sendsms.php?username={username}&password={password}&message={message}&numbers={numbers}"
     			+ "&sender={sender}&return={return}&unicode={unicode}"
//     			+ "&datetime={datetime}"
     			,String.class, ret);
//		ret.put("password", api.getPassword());
//		ret.put("message", api.getMessage());
//		ret.put("numbers",api.getNumbers());
//		ret.put("sender", api.getSender());
//		ret.put("return",api.getReturnValue());
//		ret.put("unicode",api.getUnicode());
//		ret.put("datetime",api.getDatetime());
//
				
		
		//String apiSend = new RestTemplate()
		
		ret.put("errorCode", apiSend);
		
		//return  apiSend;
		}
		catch(Exception e)
		{
			ret.put("errorCode",110 );
			
		}
		
		return ret;
		
	}
	
	@Async
	public Map<String , Object>getBalance(ApiSendMessage api){
		
		Map<String , Object> ret=new HashMap<>();
		
		try{
			ret.put("username",api.getUserName());
			ret.put("password", api.getPassword());
		
		
			Integer apiSend =  new RestTemplate().postForObject(
					"http://www.ksasms.com/api/getbalance.php",
					ret,
					Integer.class, //apiSendMessage.class,
					new HashMap<>());
			ret.put("errorCode", apiSend);
			
			
			}
			catch(Exception e)
			{
				ret.put("errorCode",110 );
				
			}
			
			return ret;
		
		
		
	}
}

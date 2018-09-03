package com.iwa_live.ws;
 
import javax.jws.WebService;
import com.iwa_live.screenshot.CaptureScreenshotTest;
//Service Implementation
@WebService(endpointInterface = "com.iwa_live.ws.Soap")
public class SoapImpl implements Soap{
 
	@Override
	public String getHelloWorldAsString(String url) {
		String fileName = "capture.png";
		try{
			fileName = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(12)+".png";
			(new CaptureScreenshotTest()).capture(new String[]{url,fileName});
		}catch(Exception e){
			e.printStackTrace(); 
		}
		return "Captured: " + fileName;
	}
 
}

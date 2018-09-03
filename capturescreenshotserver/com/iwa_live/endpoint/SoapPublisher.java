package com.iwa_live.endpoint;
 
import javax.xml.ws.Endpoint;
import com.iwa_live.ws.SoapImpl;
 
//Endpoint publisher
public class SoapPublisher{
 
	public static void main(String[] args) {
	   Endpoint.publish("http://localhost:9999/ws/hello", new SoapImpl());
    }
 
}

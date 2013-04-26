package com.coursemingle.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageException extends Exception implements Serializable {
	
	String message = "";
	
	public MessageException(){
		
	}
	
	public MessageException(String s){
		message = s;
	}
	
	public String getErrorMessage(){
		return message;
	}

}

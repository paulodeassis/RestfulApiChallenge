package br.com.pitang.challenge.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Response Class to get JSON Formated messeges
 * @author Paulo de Assis
 *
 */
public class Response {
	private int code;
	private String message;

	public String getMessage() {
	
		return message;

	}

	public void setMessage(String message) {
	
		this.message = message;

	}

	public int getCode() {
	
		return code;

	}

	public void setCode(int code) {
	
		this.code = code;

	}
	
	public String GetResponseMessage() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.writeValueAsString(this);
	}

}

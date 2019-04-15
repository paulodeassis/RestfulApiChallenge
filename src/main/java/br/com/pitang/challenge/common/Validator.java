package br.com.pitang.challenge.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	private static Validator INSTANCE;
	private Pattern pattern;
	private Matcher matcher;
	public static Validator getInstance (){
		if(INSTANCE == null) {
			INSTANCE = new Validator();
		}
		return INSTANCE;
	}
	
	public boolean isEmailValid(String email) {
		boolean isValid = false;
		pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); 
		
		if(email != null && pattern.matcher(email).find()) {
			isValid = true;
		}	
		return isValid;
	}
	
	public boolean isOnlyNumber(String value) {
		boolean isOnlyNumber = false;
		pattern = Pattern.compile("[0-9]");
		if(value != null && pattern.matcher(value).find()) {
			isOnlyNumber = true;
		}
		return isOnlyNumber;
	}
	
	public boolean isOnlyLetters(String value) {
		boolean isOnlyLetters = false;
		pattern = Pattern.compile("[a-zA-Z]");
		if(value != null && pattern.matcher(value).find()) {
			isOnlyLetters =  true;
		}
		return isOnlyLetters;
	}
	
	public boolean isFilledField(String value) {
		boolean isFilled = false;
		if(value != null && !value.equals("")) {
			isFilled = true;
		}
		return isFilled;
	}
}

package br.com.pitang.challenge.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hashing {
	private static Hashing INSTANCE;
	
	public static Hashing getInstance (){
		if(INSTANCE == null) {
			INSTANCE = new Hashing();
		}
		return INSTANCE;
	}	

	public String getHash(String plainText) throws NoSuchAlgorithmException {		
		StringBuilder builder = new StringBuilder();
		MessageDigest d = MessageDigest.getInstance("SHA-256");
		byte[] bytes;
		if(plainText != null) {
			bytes = d.digest(plainText.getBytes(StandardCharsets.UTF_8));
			for (byte byte_ : bytes) {
				builder.append(String.format("%02x", byte_));
			}
		}		
	    return builder.toString();
	}
	
	
	/*MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

	// bytes to hex
    StringBuilder sb = new StringBuilder();
    for (byte b : hashInBytes) {
        sb.append(String.format("%02x", b));
    }*/
}

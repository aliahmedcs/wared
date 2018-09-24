package com.magdsoft.sindbad.ws.helpers.password;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashPassword implements PasswordHasher{

	static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
	/***
	 * hashingpassword 
	 */
	
	@Override
	public String hashPassword(String password) {
		String result = encoder.encode(password);
		return result;
//		assertTrue(encoder.matches("myPassword", result));
		//		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/***
	 * password from client
	 */
	@Override
	public boolean isPasswordValid(String password, String hash) {
		return encoder.matches(password, hash);
//		if (BCrypt.checkpw(password, hash))
//			return true;
//		else
//			
//		return false;
	}

}

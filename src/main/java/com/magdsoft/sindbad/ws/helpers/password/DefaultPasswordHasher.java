package com.magdsoft.sindbad.ws.helpers.password;

//import org.mindrot.jbcrypt.BCrypt;

public class DefaultPasswordHasher extends HashPassword {
	private DefaultPasswordHasher() {
	}
	
	private static DefaultPasswordHasher instance = new DefaultPasswordHasher();
	
	public static DefaultPasswordHasher getInstance() {
		return instance;
	}
}

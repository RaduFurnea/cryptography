package main.java;

import main.java.crypto.Crypto;

public class Main{
	
	public static void main(String[] args) {

		
		try {
			Crypto c = new Crypto("123465");
			c.generateKeys("public", "private");
			c.signDocument("private", "file.txt", "signed");
			System.out.println(c.verifySigniature("public", "file.txt", "singiature"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

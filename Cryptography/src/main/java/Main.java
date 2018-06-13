package main.java;

import main.java.crypto.Crypto;

public class Main{
	
	public static void main(String[] args) {

//		UiMain.start(args);
		try {
			Crypto c = new Crypto("123465");
			c.generateKeys("public", "private");
			c.signDocument("private", "file", "singiature");
		
			c.encryptFile("file", "encypted");
			c.decryptFile("encypted", "decrypted");
			System.out.println(c.verifySigniature("public", "file", "singiature"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

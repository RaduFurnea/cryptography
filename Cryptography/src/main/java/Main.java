package main.java;



public class Main{
	
	public static void main(String[] args) {

////		UiMain.start(args);
		try {
			CryptographyServices c = new CryptographyServices("123465");
			c.generateKeys("public", "private");
			c.createSigniature("private", "file", "singiature");
		
			// encrypt
			c.performCrypto("file", "encypted", true);
			
			// decrypt
			c.performCrypto("encypted", "decrypted", false);
			
			System.out.println(c.checkSigniature("public", "file", "singiature"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

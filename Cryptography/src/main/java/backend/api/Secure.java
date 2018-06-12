package main.java.backend.api;

public interface Secure {

	void performEncrypt(String document, String out) throws Exception;

	void performDecrypt(String document, String out) throws Exception;

	void generateKeys(String publicKeyPath, String privateKeyPath) throws Exception;
	
	boolean verify(String documentPath, String keyloc, String out) throws Exception;

	void sign(String documentPath, String keyloc, String out) throws Exception;


}
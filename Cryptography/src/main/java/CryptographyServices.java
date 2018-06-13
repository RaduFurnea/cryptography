package main.java;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptographyServices {
	private static final int INIT_VALUE = 1024;

	private KeyPairGenerator keyGen;

	private Cipher cipher;
	private Signature rsa;
	private char[] passwordCharArr;

	public CryptographyServices(String password) throws Exception {
		this.cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		this.rsa = Signature.getInstance("SHA256withRSA");
		this.passwordCharArr = password.toCharArray();
	}

	private byte[] bytesEncrypt(byte[] bytesToEncypt) throws Exception {

		// Define key for he blowfish specification
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(passwordCharArr, "CONSTANTSALTING".getBytes(), 1000, 128);
		byte[] encodedKey = factory.generateSecret(spec).getEncoded();
		SecretKey finalkey = new SecretKeySpec(encodedKey, "Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, finalkey);

		return cipher.doFinal(bytesToEncypt);
	}

	private byte[] bytesDecrypt(byte[] bytesToDecrypt) throws Exception {
		
		// Define key for he blowfish specification
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(passwordCharArr, "CONSTANTSALTING".getBytes(), 1000, 128);
		byte[] encodedKey = factory.generateSecret(spec).getEncoded();
		SecretKey finalkey = new SecretKeySpec(encodedKey, "Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, finalkey);
		
		return cipher.doFinal(bytesToDecrypt);
	}

	public void generateKeys(String publicKeyPath, String privateKeyPath) throws Exception {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(INIT_VALUE);

		KeyPair keys = keyGen.generateKeyPair();
		byte[] publicKeyBytes = keys.getPublic().getEncoded();

		// Encrypt bytes
		byte[] privateKeyByte = bytesEncrypt(keys.getPrivate().getEncoded());

		Files.write(Paths.get(publicKeyPath), publicKeyBytes);
		Files.write(Paths.get(privateKeyPath), privateKeyByte);
	}

	public void createSigniature(String privateKeyPath, String filePath, String pathToSigniature) throws Exception {
		PrivateKey key = getPrivateKey(privateKeyPath);
		rsa.initSign(key);
		rsa.update(Files.readAllBytes(Paths.get(filePath)));

		// write the file
		Files.write(Paths.get(pathToSigniature), rsa.sign());
	}

	public void performCrypto(String fileToEncrypt, String outputPath, boolean encrypt) throws Exception {
		byte[] fileBytes = Files.readAllBytes(Paths.get(fileToEncrypt));
		if (encrypt) {
			Files.write(Paths.get(outputPath), bytesEncrypt(fileBytes));
		} else {
			Files.write(Paths.get(outputPath), bytesDecrypt(fileBytes));
		}
	}

	public boolean checkSigniature(String keyPath, String filePath, String pathToSigniature) throws Exception {
		PublicKey key = getPublicKey(keyPath);
		rsa.initVerify(key);
		rsa.update(Files.readAllBytes(Paths.get(filePath)));
		return rsa.verify(Files.readAllBytes(Paths.get(pathToSigniature)));
	}

	private PrivateKey getPrivateKey(String keyPathString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] key = bytesDecrypt(Files.readAllBytes(Paths.get(keyPathString)));
		EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(key);
		return keyFactory.generatePrivate(encodedKey);
	}

	private PublicKey getPublicKey(String keyPathString) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] key = Files.readAllBytes(Paths.get(keyPathString));
		EncodedKeySpec encodedKey = new X509EncodedKeySpec(key);
		return keyFactory.generatePublic(encodedKey);
	}

}

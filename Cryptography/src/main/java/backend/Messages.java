package main.java.backend;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import main.java.backend.api.Secure;

public class Messages implements Secure {
	private static final byte[] SALT_BYTES = "RADOM_SALT_BYTES".getBytes();

	private KeyPairGenerator keyGen;

	private String pass;
	KeyFactory factory;

	public Messages(String password) throws Exception {
		this.pass = password;
		if(password == null) {
			this.pass= "default";
		}
		this.factory = KeyFactory.getInstance("DSA");
	}

	private PrivateKey readPrivateKey(String keyLoc) throws Exception {
		byte[] key = decrypt(Utils.getFileAsBytes(keyLoc));
		EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		return factory.generatePrivate(keySpec);
	}

	private PublicKey readPublicKey(String keyLoc) throws Exception {
		byte[] key = Utils.getFileAsBytes(keyLoc);
		EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		return factory.generatePublic(keySpec);
	}

	private SecretKey deriveAesKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(pass.toCharArray(), SALT_BYTES, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	private byte[] encrypt(byte[] bytesToEncypt) throws Exception {
		Key aesKey = deriveAesKey();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		return cipher.doFinal(bytesToEncypt);
	}

	private byte[] decrypt(byte[] bytesToDecrypt) throws Exception {
		Key aesKey = deriveAesKey();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		return cipher.doFinal(bytesToDecrypt);
	}


	@Override
	public void performEncrypt(String fileToEncrypt, String outputPath) throws Exception {
		Utils.writeBytesToDisk(encrypt(Utils.getFileAsBytes(fileToEncrypt)), outputPath);
	}


	@Override
	public void performDecrypt(String fileToDecrypt, String outputPath) throws Exception {
		Utils.writeBytesToDisk(decrypt(Utils.getFileAsBytes(fileToDecrypt)), outputPath);
	}

	
	@Override
	public void generateKeys(String publicKeyPath, String privateKeyPath) throws Exception {
		this.keyGen = KeyPairGenerator.getInstance("DSA");
		keyGen.initialize(2048);

		KeyPair keys = keyGen.generateKeyPair();
		byte[] publicKeyBytes = keys.getPublic().getEncoded();
		byte[] privateKeyByte = keys.getPrivate().getEncoded();

		Utils.writeBytesToDisk(publicKeyBytes, publicKeyPath);
		Utils.writeBytesToDisk(encrypt(privateKeyByte), privateKeyPath);
	}
	
	@Override
	public boolean verify(String documentPath, String keyloc, String signiature) throws Exception {
		Signature dsa = Signature.getInstance("SHA256withDSA");

		PublicKey key = readPublicKey(keyloc);
		dsa.initVerify(key);
		
		dsa.update(Utils.getFileAsBytes(documentPath));

		return dsa.verify( Utils.getFileSigniature(signiature));
	}

	
	@Override
	public void sign(String documentPath, String keyloc, String out) throws Exception {
		PrivateKey key = readPrivateKey(keyloc);
		Signature dsa = Signature.getInstance("SHA256withDSA");
		
		dsa.initSign(key);
		byte[] bytes = Utils.getFileAsBytes(documentPath);

		dsa.update(bytes);
		Utils.writeBytesToDisk(dsa.sign(), out);
	}



}

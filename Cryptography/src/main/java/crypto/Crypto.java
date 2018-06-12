package main.java.crypto;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class Crypto {
	private KeyPairGenerator keyGen;

	private String password;

	public Crypto(String password) throws Exception {
		this.password = password;
	}

	private Key readKeyFromFs(String keyPath, boolean isPrivate) throws Exception {
		Key pk = null;
		System.out.println(keyPath);
		byte[] key;
		EncodedKeySpec keySpec;
		if (isPrivate) {
			key = decrypt(getFileAsBytes(keyPath));
			keySpec = new PKCS8EncodedKeySpec(key);
		} else {
			key = getFileAsBytes(keyPath);
			keySpec = new X509EncodedKeySpec(key);
		}

		KeyFactory kf = KeyFactory.getInstance("DSA");

		if (isPrivate)
			pk = kf.generatePrivate(keySpec);
		else
			pk = kf.generatePublic(keySpec);

		return pk;
	}

	private byte[] getFileAsBytes(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		return keyBytes;
	}

	private void writeBytesToDisk(byte[] arr, String path) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(arr);
		}
	}

	private SecretKey deriveAesKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	private byte[] readSigniature(String filePath) throws IOException {
		FileInputStream sigfis = new FileInputStream(filePath);
		byte[] sigToVerify = new byte[sigfis.available()];
		sigfis.read(sigToVerify);
		sigfis.close();

		return sigToVerify;
	}

	public byte[] encrypt(byte[] bytesToEncypt) throws Exception {
		Key aesKey = deriveAesKey();
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, aesKey);

		return cipher.doFinal(bytesToEncypt);
	}

	public byte[] decrypt(byte[] bytesToDecrypt) throws Exception {
		byte[] key = this.password.getBytes();
		Key aesKey = deriveAesKey();
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, aesKey);

		return cipher.doFinal(bytesToDecrypt);
	}

	public void generateKeys(String publicKeyPath, String privateKeyPath) throws Exception {
		this.keyGen = KeyPairGenerator.getInstance("DSA");
		keyGen.initialize(2048);

		KeyPair keys = keyGen.generateKeyPair();
		byte[] publicKeyBytes = keys.getPublic().getEncoded();
		byte[] privateKeyByte = keys.getPrivate().getEncoded();

		writeBytesToDisk(publicKeyBytes, publicKeyPath);
		writeBytesToDisk(encrypt(privateKeyByte), privateKeyPath);
	}

	public void signDocument(String privateKeyPath, String filePath, String pathToSigniature) throws Exception {

		PrivateKey key = (PrivateKey) readKeyFromFs(privateKeyPath, true);
		Signature dsa = Signature.getInstance("SHA256withDSA");

		/* Initializing the object with a private key */
		dsa.initSign(key);
		byte[] fileBytes = getFileAsBytes(filePath);

		/* Update and sign the data */
		dsa.update(fileBytes);
		this.writeBytesToDisk(dsa.sign(), "singiature");
	}

	public boolean verifySigniature(String keyPath, String filePath, String pathToSigniature) throws Exception {
		Signature dsa = Signature.getInstance("SHA256withDSA");

		PublicKey key = (PublicKey) readKeyFromFs(keyPath, false);

		/* Initializing the object with a public key */
		dsa.initVerify(key);

		byte[] sigToVerify = this.readSigniature(pathToSigniature);

		byte[] fileBytes = getFileAsBytes(filePath);

		dsa.update(fileBytes);

		return dsa.verify(sigToVerify);

	}

}

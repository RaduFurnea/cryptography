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
import java.security.spec.X509EncodedKeySpec;

public class Crypto {
	private KeyPairGenerator keyGen;

	private Key readKeyFromFs(String keyPath, boolean isPrivate) throws Exception {
		Key pk = null;
		File f = new File(keyPath);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("DSA");
		if(isPrivate)
			pk = kf.generatePrivate(spec);
		else 
			pk = kf.generatePublic(spec);
		
		return pk;
	}
	
	private byte[] readSigniature(String filePath) throws IOException {
		FileInputStream sigfis = new FileInputStream(filePath);
		byte[] sigToVerify = new byte[sigfis.available()]; 
		sigfis.read(sigToVerify);
		sigfis.close();
		
		return sigToVerify;	
	}
	
	private void writeBytesToDisk(byte[] arr) throws Exception {
		try (FileOutputStream fos = new FileOutputStream("pathname")) {
			   fos.write(arr);
			}
	}

	public void generateKeys(String password, String publicKeyPath, String privateKeyPath)
			throws NoSuchAlgorithmException {
		this.keyGen = KeyPairGenerator.getInstance("DSA");
		keyGen.initialize(2048);

		KeyPair keys = keyGen.generateKeyPair();
		byte[] publicKeyBytes = keys.getPublic().getEncoded();
		byte[] privateKeyByte = keys.getPrivate().getEncoded();

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(publicKeyPath);
			fos.write(publicKeyBytes);
			fos.close();

			fos = new FileOutputStream(privateKeyPath);
			fos.write(privateKeyByte);
			fos.close();
		} catch (IOException e) {
			System.err.println("Error saveing keys");
		}
	}

	public void signDocument(String privateKeyPath, String filePath) throws Exception {

		PrivateKey key = (PrivateKey) readKeyFromFs(privateKeyPath, true);

		Signature dsa = Signature.getInstance("SHA256withDSA");

		/* Initializing the object with a private key */
		dsa.initSign(key);
		byte[] fileBytes = null;
		try {
			// read file as byte array
			File f = new File(filePath);
			FileInputStream fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);
			fileBytes = new byte[(int) f.length()];
			dis.readFully(fileBytes);
			dis.close();
		} catch (Exception e) {
			throw new Exception("Error reading file " + e );
		}

		/* Update and sign the data */
		dsa.update(fileBytes);
		this.writeBytesToDisk(dsa.sign());
	}

	public boolean verifySigniature(String keyPath, String filePath) throws Exception {
		Signature dsa = Signature.getInstance("SHA256withDSA");

		PublicKey key = (PublicKey) readKeyFromFs(keyPath, false);
		
		/* Initializing the object with a public key */
		dsa.initVerify(key);
		
		byte[] sigToVerify = this.readSigniature(filePath);
		
		byte[] fileBytes = null;
		try {
			// read file as byte array
			File f = new File(filePath);
			FileInputStream fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);
			fileBytes = new byte[(int) f.length()];
			dis.readFully(fileBytes);
			dis.close();
		} catch (Exception e) {
			throw new Exception("Error reading file " + e );
		}

		dsa.update(fileBytes);
		
		return dsa.verify(sigToVerify);
	
	}

}

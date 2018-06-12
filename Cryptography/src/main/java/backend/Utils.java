package main.java.backend;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

	public static void writeBytesToDisk(byte[] arr, String path) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(arr);
		}
	}

	public static byte[] getFileAsBytes(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();
		return keyBytes;
	}

	public static byte[] getFileSigniature(String file) throws IOException {
		FileInputStream sigfis = new FileInputStream(file);
		byte[] sigToVerify = new byte[sigfis.available()];
		sigfis.read(sigToVerify);
		sigfis.close();
		return sigToVerify;
	}
}

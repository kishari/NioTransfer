package hu.msrp.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtil {
	
	private static Writer writer = null;
	private static Writer writer2 = null;
	private static boolean isInited = false;
	private static boolean isInited2 = false;

	public static void printToFile(String fileName, byte[] data) {
		if ( "/home/csaba/temp/sendData".equals(fileName) && !isInited) {
			try {
				writer = new BufferedWriter(new FileWriter(fileName));
				isInited = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ( "/home/csaba/temp/recvData".equals(fileName) && !isInited2) {
			try {
				writer2 = new BufferedWriter(new FileWriter(fileName));
				isInited2 = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			if ("/home/csaba/temp/sendData".equals(fileName)) {
				writer.append(new String(data));
				writer.flush();
			}
			else if ("/home/csaba/temp/recvData".equals(fileName)) {
				writer2.append(new String(data));
				writer2.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 
	}
}

package com.lzx.allenLee.util.encryptionUtil;

import java.io.FileNotFoundException;

import android.os.Environment;

import com.lzx.allenLee.global.Global;

public final class EncryptUtil {

	public EncryptUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String encrypt(String inputStr) throws FileNotFoundException{
//		System.err.println("原文:\t" + inputStr);
		String outputStr = null;
		try {
			String key = Global.getOutSideStorageKey();
//			System.err.println("密钥:\t" + key);
//			System.err.println("密钥debase64:\t" +  new String(Coder.decryptBASE64(key),"UTF-8"));
			byte[] inputData = inputStr.getBytes();
			outputStr = DESCoder.encrypt(inputData, key);
//			.replaceAll("\n", "");
//			System.err.println("加密后:\t" + outputStr);
//			System.err.println("加密后debase64:\t" + new String(Coder.decryptBASE64(outputStr),"UTF-8"));
		}catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return outputStr;
	}

	public static String decrypt(String inputStr) {
		String outputData = null;
		try {
			String key = Global.getOutSideStorageKey();
			outputData = DESCoder.decrypt(inputStr, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputData;
	}

}

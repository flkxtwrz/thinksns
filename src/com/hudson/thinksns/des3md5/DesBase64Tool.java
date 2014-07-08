package com.hudson.thinksns.des3md5;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * Java版3DES加密解密，适用于PHP版3DES加密解密(PHP语言开发的MCRYPT_3DES算法、MCRYPT_MODE_ECB模式、
 * PKCS7填充方式)
 * 
 * @author 贾焱超
 */
public class DesBase64Tool {
	private static final String MCRYPT_TRIPLEDES = "DESede";
	private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	private static Cipher cipher = null; // 私鈅加密对象Cipher

	private static IvParameterSpec iv = null;
	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
			(byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

	static {
		try {

			iv = new IvParameterSpec(iv1);

			/* 获得一个私鈅加密类Cipher，DESede是算法，ECB是加密模式，PKCS5Padding是填充方式 */
			cipher = Cipher.getInstance(TRANSFORMATION);
		} catch (Exception e) {

		}
	}

	public static String md5(String input)  {
		String result = input;
		if (input != null) {
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32) { // 40 for SHA-1
				result = "0" + result;
			}
		}
		return result;
	}

	/**
	 * 加密
	 * 
	 * @param message
	 * @return
	 */
	public static String desEncrypt(String message, String keyString) {
		String result = ""; // DES加密字符串
		String newResult = "";// 去掉换行符后的加密字符串

		try {
			DESedeKeySpec spec = new DESedeKeySpec(keyString.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(MCRYPT_TRIPLEDES);
			SecretKey secretKey = keyFactory.generateSecret(spec);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv); // 设置工作模式为加密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8")); // 正式执行加密操作	
			result=Base64Util.encode(resultBytes);
			newResult = filter(result); // 去掉加密串中的换行符
		} catch (Exception e) {
			// log.error(e.getMessage(), e);
		}
		return newResult;
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String desDecrypt(String message, String keyString)
			 {
		String result = "";
		try {
			 byte[] messageBytes=Base64Util.decode(message);
			DESedeKeySpec spec = new DESedeKeySpec(keyString.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(MCRYPT_TRIPLEDES);
			SecretKey secretKey = keyFactory.generateSecret(spec);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv); // 设置工作模式为解密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(messageBytes);// 正式执行解密操作
			result = new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 去掉加密字符串换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i + 1));
			}
		}
		output = new String(sb);
		return output;
	}

	public static String paddingkey(String key) {
		// System.out.println(key.length());
		int len = key.length();
		if (key.length() < 24) {
			for (int i = 0; i < 24 - len; i++) {
				key += "\0";
			}
		}

		return key;

	}

	/**
	 * 加密解密测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("md5结果：" + md5("hudson"));

			String strText = "thinksns";
			String deseResult = desEncrypt(strText, paddingkey("THINKSNS"));// 加密
			System.out.println("加密结果：" + deseResult);
			String desdResult = desDecrypt(deseResult, paddingkey("THINKSNS"));// 解密
			System.out.println("解密结果：" + desdResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
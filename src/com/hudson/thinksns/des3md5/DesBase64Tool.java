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
 * Java��3DES���ܽ��ܣ�������PHP��3DES���ܽ���(PHP���Կ�����MCRYPT_3DES�㷨��MCRYPT_MODE_ECBģʽ��
 * PKCS7��䷽ʽ)
 * 
 * @author ���ͳ�
 */
public class DesBase64Tool {
	private static final String MCRYPT_TRIPLEDES = "DESede";
	private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	private static Cipher cipher = null; // ˽�_���ܶ���Cipher

	private static IvParameterSpec iv = null;
	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
			(byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

	static {
		try {

			iv = new IvParameterSpec(iv1);

			/* ���һ��˽�_������Cipher��DESede���㷨��ECB�Ǽ���ģʽ��PKCS5Padding����䷽ʽ */
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
	 * ����
	 * 
	 * @param message
	 * @return
	 */
	public static String desEncrypt(String message, String keyString) {
		String result = ""; // DES�����ַ���
		String newResult = "";// ȥ�����з���ļ����ַ���

		try {
			DESedeKeySpec spec = new DESedeKeySpec(keyString.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(MCRYPT_TRIPLEDES);
			SecretKey secretKey = keyFactory.generateSecret(spec);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv); // ���ù���ģʽΪ����ģʽ��������Կ
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8")); // ��ʽִ�м��ܲ���	
			result=Base64Util.encode(resultBytes);
			newResult = filter(result); // ȥ�����ܴ��еĻ��з�
		} catch (Exception e) {
			// log.error(e.getMessage(), e);
		}
		return newResult;
	}

	/**
	 * ����
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
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv); // ���ù���ģʽΪ����ģʽ��������Կ
			byte[] resultBytes = cipher.doFinal(messageBytes);// ��ʽִ�н��ܲ���
			result = new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ȥ�������ַ������з�
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
	 * ���ܽ��ܲ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("md5�����" + md5("hudson"));

			String strText = "thinksns";
			String deseResult = desEncrypt(strText, paddingkey("THINKSNS"));// ����
			System.out.println("���ܽ����" + deseResult);
			String desdResult = desDecrypt(deseResult, paddingkey("THINKSNS"));// ����
			System.out.println("���ܽ����" + desdResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
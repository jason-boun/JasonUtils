package com.jason.jasonutils.tools;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.jason.jasonutils.pwdlock.Constant;

import android.util.Base64;

public class EncryptionTool {

	/**
	 * 解密
	 * @param decStr
	 * @return
	 */
	public static String decryptString(String decStr) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constant.PASSWORD_ENC_SECRET.getBytes("UTF-8"));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] plainTextPwdBytes = cipher.doFinal(Base64.decode(decStr, Base64.DEFAULT));
			return new String(plainTextPwdBytes);
		} catch (Exception e) {
		}
		return decStr;
	}

	/**
	 * 加密
	 * @param encStr
	 * @return
	 */
	public static String encryptString(String encStr) {
		try {
			DESKeySpec keySpec = new DESKeySpec(Constant.PASSWORD_ENC_SECRET.getBytes("UTF-8"));
			SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String encrypedPwd = Base64.encodeToString(cipher.doFinal(encStr.getBytes("UTF-8")), Base64.DEFAULT);
			return encrypedPwd;
		} catch (Exception e) {
		}
		return encStr;
	}

}

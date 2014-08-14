package org.trii.tinyspring.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: February 18, 2014
 * Time: 12:41
 */
public class Encryptor {

	private static Logger log = LoggerFactory.getLogger(Encryptor.class);

	public static String encodeBase64(String string) {

		return Base64.encodeBase64String(string.getBytes());
	}


	/**
	 * Calculate string's md5 digest.
	 *
	 * @param string
	 *
	 * @return
	 */
	public static String md5(String string) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(string.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuilder buf = new StringBuilder("");
			for(byte _byte : b) {
				i = _byte;
				if(i < 0) {
					i += 256;
				}
				if(i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			//32位加密
			return buf.toString();
			// 16位的加密
			//return buf.toString().substring(8, 24);
		} catch(NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}


	public static byte[] desEncrypt(byte[] array, String key) {

		javax.crypto.SecretKey generatedKey;
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("DES");

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			generator.init(secureRandom);
			generatedKey = generator.generateKey();
			generator = null;

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, generatedKey);
			return cipher.doFinal(array);
		} catch(NoSuchAlgorithmException e) {
			log.error("DES encryption failed. {}", e.getMessage());
		} catch(IllegalBlockSizeException | InvalidKeyException | BadPaddingException |
				NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public byte[] desDecrypt(byte[] array, String key) {

		javax.crypto.SecretKey generatedKey;
		KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("DES");

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			generator.init(secureRandom);
			generatedKey = generator.generateKey();
			generator = null;

			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, generatedKey);
			return cipher.doFinal(array);
		} catch(NoSuchAlgorithmException e) {
			log.error("DES decryption failed.", e);
		} catch(IllegalBlockSizeException | InvalidKeyException | BadPaddingException |
				NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

}

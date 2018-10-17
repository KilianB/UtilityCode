package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class CryptoUtilTest {

	@Test
	void deriveKeyIdentity() {
		SecretKey key = CryptoUtil.deriveKey(new char[] { 5, 1, 5, 1, 2, 3 });
		SecretKey key1 = CryptoUtil.deriveKey(new char[] { 5, 1, 5, 1, 2, 3 });
		assertTrue(key.equals(key1));
	}

	@Test
	void encryptDecrypt() {
		String original = "MySecret";
		SecretKey key = CryptoUtil.deriveKey(new char[] { 5, 1, 5, 1, 2, 3 });

		try {
			String text = CryptoUtil.encryptAES(key, original);
			assertNotEquals(original, text);
			assertEquals(original, CryptoUtil.decrypt(key, text));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

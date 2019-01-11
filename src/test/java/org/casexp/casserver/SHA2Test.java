package org.casexp.casserver;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import org.junit.Test;


public class SHA2Test {

	@Test
	public void test() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			String testString = "alice";
			byte[] hash = digest
					.digest(testString.getBytes(StandardCharsets.UTF_8));
			byte[] hash2 = digest
					.digest(testString.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02X", b));
			}
			System.out.println(sb.toString());
			if(hash.equals(hash2))
				System.out.println("simple_eq");
			else
				System.out.println("simple_ne");
			if(Arrays.equals(hash,hash2))
				System.out.println("deep_eq");
			else
				System.out.println("deep_ne");
			System.out.println("length : " + String.valueOf(hash.length));
		} catch (Exception e) {
		}
	}

}

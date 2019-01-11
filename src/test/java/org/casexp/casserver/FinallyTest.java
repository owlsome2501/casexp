package org.casexp.casserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class FinallyTest {

	public int returnInt() {
		try {
			System.out.println("in try");
			return 2;
		}
		finally {
			System.out.println("finally");
			return 1;
		}
	}
	@Test
	public void test() {
		int i = returnInt();
		System.out.println("return "+String.valueOf(i));
	}

}

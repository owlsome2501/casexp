package org.casexp.casserver;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

public class UUIDTest {

	@Test
	public void test() {
		UUID uuid = UUID.randomUUID();
		System.out.println("UUID ouput: "+uuid.toString());
	}

}

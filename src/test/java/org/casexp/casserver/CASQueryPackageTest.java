package org.casexp.casserver;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonSyntaxException;

public class CASQueryPackageTest {

	@Test
	public void GoodPackTest() {
		String qpack_test = "{\"ticket\":{\"type\":\"normal\",\"account\":\"casexp\",\"password\":\"good\"},"
				+ "\"query\":[{\"queryMethod\":\"createCASSess\","
				+ "\"parameters\":[\"def\",666]}]}";
		System.out.println(qpack_test);
		CASQueryPackage queryPackage = CASQueryPackage.unpack(qpack_test);
		assertNotEquals(null, queryPackage);

		System.out.println("CASTicketTP: " + queryPackage.ticket.type);
		System.out.println("CASTicketAC: " + queryPackage.ticket.account);
		System.out.println("CASTicketPW: " + queryPackage.ticket.password);
		System.out.println("CASQuery: " + queryPackage.query[0].queryMethod);
		for (String parameter : queryPackage.query[0].parameters) {
			System.out.println("CASQueryParameter: " + parameter);
		}
	}

	@Test(expected = JsonSyntaxException.class)
	public void MissElementPackTest() throws JsonSyntaxException {
		try {
			String qpack_test = "{\"ticket\":{\"not_type\":\"normal\",\"account\":\"casexp\",\"password\":\"good\"}}";
			System.out.println(qpack_test);
			CASQueryPackage.unpack(qpack_test);
		} catch (JsonSyntaxException e) {
			System.out.println(e.getMessage());
			throw e;
		}
		fail("not excepted");
	}

	@Test(expected = JsonSyntaxException.class)
	public void WrongElementPackTest() throws JsonSyntaxException {
		try {
			String qpack_test = "{\"ticket\":{\"type\":\"normal\",\"account\":[\"casexp\"],\"password\":\"good\"}}";
			System.out.println(qpack_test);
			CASQueryPackage.unpack(qpack_test);
		} catch (JsonSyntaxException e) {
			System.out.println(e.getMessage());
			throw e;
		}
		fail("not excepted");
	}
}

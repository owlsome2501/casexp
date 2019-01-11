package org.casexp.casserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class CASKeyMngrTest {

	private CASKeyMngr kMngr;

	@Before
	public void createNew() {
		kMngr = new CASKeyMngr();
	}

	@Test
	public void testLogin() {
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice";
		ticket.password = "alice";
		try {
			int uid = kMngr.login(ticket);
			assertEquals(1,uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = CASTicketWrongACException.class)
	public void testWrongLogin() throws CASTicketWrongACException {
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice_error";
		ticket.password = "alice";
		try {
			kMngr.login(ticket);
		} catch (CASTicketWrongACException e) {
			System.out.println(e.getMessage());
			throw e;
		} catch (Exception e) {
		}
		fail("more wo");
	}
}

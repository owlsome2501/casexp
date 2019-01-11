package org.casexp.casserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class SessionMngrTest {

	@Test
	public void CreateTicketTest() {
		try {
			SessionTicket sTicket = CASSessionMngr.createTicket(1);
			System.out.println("ticket="+sTicket.ticket);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void ReadTicketTest() {
		SessionTicket sTicket = new SessionTicket();
		sTicket.ticket = "742d0523ed5548a584bbd97fdc68d4d2";
		try {
			int uid = CASSessionMngr.readTicket(sTicket);
			System.out.println("UID: "+String.valueOf(uid));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

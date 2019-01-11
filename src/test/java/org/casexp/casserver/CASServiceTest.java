package org.casexp.casserver;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class CASServiceTest {

	@Test
	public void LoginTest() {
		CASQueryPackage casq = new CASQueryPackage();
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice";
		ticket.password = "alice";
		casq.ticket = ticket;
		casq.query = new CASQuery[] {};
		CASServiceMngr sMngr = new CASServiceMngr(casq);
		sMngr.exec();
		String answer = new Gson().toJson(sMngr.getAns());
		System.out.println("answer: " + answer);
	}

	@Test
	public void WrongPWLoginTest() {
		CASQueryPackage casq = new CASQueryPackage();
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice";
		ticket.password = "alice_error";
		casq.ticket = ticket;
		casq.query = new CASQuery[] {};
		CASServiceMngr sMngr = new CASServiceMngr(casq);
		sMngr.exec();
		String answer = new Gson().toJson(sMngr.getAns());
		System.out.println("answer: " + answer);
	}

	@Test
	public void CreateTicketTest() {
		CASQueryPackage casq = new CASQueryPackage();
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice";
		ticket.password = "alice";
		casq.ticket = ticket;
		CASQuery queryCreateTicket = new CASQuery();
		queryCreateTicket.queryMethod = "create_ticket";
		queryCreateTicket.parameters = new String[] {};
		casq.query = new CASQuery[] { queryCreateTicket };
		CASServiceMngr sMngr = new CASServiceMngr(casq);
		sMngr.exec();
		String answer = new Gson().toJson(sMngr.getAns());
		String queString = new Gson().toJson(casq);
		System.out.println("answer: " + answer);
		System.out.println("ques: "+queString);
	}

	@Test
	public void WrongMethodTest() {
		CASQueryPackage casq = new CASQueryPackage();
		CASTicket ticket = new CASTicket();
		ticket.type = "normal";
		ticket.account = "alice";
		ticket.password = "alice";
		casq.ticket = ticket;
		CASQuery queryCreateTicket = new CASQuery();
		queryCreateTicket.queryMethod = "create_error";
		queryCreateTicket.parameters = new String[] {};
		casq.query = new CASQuery[] { queryCreateTicket };
		CASServiceMngr sMngr = new CASServiceMngr(casq);
		sMngr.exec();
		String answer = new Gson().toJson(sMngr.getAns());
		System.out.println("answer: " + answer);
	}

	@Test
	public void ReadMoreTicketInfoTest() {
		CASQueryPackage casq = new CASQueryPackage();
		CASTicket ticket = new CASTicket();
		Gson gson = new Gson();
		ticket.type = "normal";
		ticket.account = "comp";
		ticket.password = "comp";
		casq.ticket = ticket;
		CASQuery queryCreateTicket = new CASQuery();

		SessionTicket sessionTicket = new SessionTicket();
		sessionTicket.ticket = "742d0523ed5548a584bbd97fdc68d4d2";
		String data = gson.toJson(sessionTicket);
		queryCreateTicket.queryMethod = "read_more_ticket_info";
		queryCreateTicket.parameters = new String[] {data};
		casq.query = new CASQuery[] { queryCreateTicket };
		CASServiceMngr sMngr = new CASServiceMngr(casq);
		sMngr.exec();
		String answer = gson.toJson(sMngr.getAns());
		System.out.println("answer: " + answer);
	}
}

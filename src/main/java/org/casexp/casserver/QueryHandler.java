package org.casexp.casserver;

import java.util.UUID;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public interface QueryHandler {
	public String[] getPermissions();
	public CASAnswer handle(int uid, String[] parameters);
}

class QueryHCreateTicket implements QueryHandler {
	private static String[] permissions = new String[] { "p_create_ticket" };

	public CASAnswer handle(int uid, String[] parameters) {
		UUID uuid = UUID.randomUUID();
		try {
			SessionTicket sessionTicket = CASSessionMngr.createTicket(uid);
			String data = new Gson().toJson(sessionTicket);
			return CASAnswer.OK(uuid, data);
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(e.getMessage());
			return CASAnswer.ERROR("FatalError");
		}
	}

	public String[] getPermissions() {
		return permissions;
	}
}

class QueryHReadMoreTicketInfo implements QueryHandler {
	private static String[] permissions = new String[] { "p_read_ticket","p_read_extra_acl" };

	public CASAnswer handle(int uid, String[] parameters) {
		if (parameters.length < 1)
			return CASAnswer.ERROR("ParameterSyntaxError");
		UUID uuid = UUID.randomUUID();
		Gson gson = new Gson();
		try {
		SessionTicket ticket = gson.fromJson(parameters[0],
				SessionTicket.class);
		System.out.println("recv ticket: " + ticket.ticket);
		int otherUid = CASSessionMngr.readTicket(ticket);
		CASACLMngr aclMngr = new CASACLMngr(otherUid);
		return CASAnswer.OK(uuid, gson.toJson(aclMngr.getExtraALC(uid)));
		}
		catch (SessionTicketException e) {
			return CASAnswer.ERROR(e.getMessage());
		}
		catch (JsonSyntaxException e) {
			return CASAnswer.ERROR("ParameterSyntaxError");
		}
		catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(e.getMessage());
			return CASAnswer.ERROR("FatalError");
		}
	}

	public String[] getPermissions() {
		return permissions;
	}
}
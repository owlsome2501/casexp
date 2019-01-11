package org.casexp.casserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

class CASQueryPackage {
	public CASTicket ticket;
	public CASQuery[] query;

	public static CASQueryPackage unpack(String qpack)
			throws JsonSyntaxException {
		CASQueryPackage casq = new Gson().fromJson(qpack,
				CASQueryPackage.class);
		if (casq != null && casq.ticket != null && casq.query != null
				&& casq.ticket.type != null && casq.ticket.account != null
				&& casq.ticket.password != null)
			return casq;
		throw new JsonSyntaxException("MissElement");
	}
}

class CASTicket {
	String type;
	String account;
	String password;
}

class CASQuery {
	String queryMethod;
	String[] parameters;
}
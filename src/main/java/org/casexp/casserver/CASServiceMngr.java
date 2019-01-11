package org.casexp.casserver;

import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.logging.Logger;

public class CASServiceMngr {
	private int uid;
	private CASACLMngr aclMngr = null;
	private CASQueryPackage casq = null;
	private CASAnswerPackage anspkg = null;
	private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static Map<String, QueryHandler> handlers = Map.of(
			"create_ticket", new QueryHCreateTicket(),
			"read_more_ticket_info", new QueryHReadMoreTicketInfo());

	public CASServiceMngr(CASQueryPackage casq) {
		this.casq = casq;
	}

	private boolean checkPermission(Map<String, Boolean> casAttrAcl,
			QueryHandler handler) {
		for (String key : casAttrAcl.keySet()) {
			System.out.println(key + ": " + casAttrAcl.get(key).toString());
		}
		for (String permission : handler.getPermissions()) {
			Boolean test = casAttrAcl.get(permission);
			System.out.println("need permission: " + permission);
			if(casAttrAcl.keySet().contains(permission))
				System.out.println("at least contain");
			if (test == null || !test) {
				if (test == null)
					System.out.println("real null permission");
				return false;
			}
		}
		return true;
	}

	private void login() throws SQLException, CASTicketException {
		CASKeyMngr keyMngr = new CASKeyMngr();
		this.uid = keyMngr.login(this.casq.ticket);
		aclMngr = new CASACLMngr(uid);
		logger.info(casq.ticket.account + " login successfully");
	}

	public void exec() {
		// have being executed before
		if (anspkg != null)
			return;
		anspkg = new CASAnswerPackage();
		anspkg.account = casq.ticket.account;
		anspkg.date = ZonedDateTime.now(ZoneOffset.UTC);
		try {

			login();

			CASQuery[] queries = casq.query;
			for (CASQuery query : queries) {
				CASAnswer answer;
				QueryHandler handler = handlers.get(query.queryMethod);
				if (handler != null) {
					if (checkPermission(aclMngr.getCasAttrACL(), handler)) {
						answer = handler.handle(uid, query.parameters);
					} else {
						answer = CASAnswer.ERROR("PermissionDenied");
					}
				} else {
					answer = CASAnswer.ERROR("UnknownMethod");
				}
				anspkg.answers.add(answer);
			}
		} catch (CASTicketException e) {
			logger.warning(
					casq.ticket.account + " login failed: " + e.getMessage());
			e.printStackTrace();
			anspkg.answers.add(CASAnswer.ERROR(e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warning("database error");
		}
	}

	public CASAnswerPackage getAns() {
		return anspkg;
	}

	public boolean hasAns() {
		return anspkg != null;
	}
}
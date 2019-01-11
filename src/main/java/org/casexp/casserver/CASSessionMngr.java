package org.casexp.casserver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class SessionTicketException extends Exception {
	public SessionTicketException(String msg) {
		super(msg);
	}
}

class SessionTicket {
	public String ticket;
}

public class CASSessionMngr {

	public static SessionTicket createTicket(int uid) throws SQLException {
		String ticket = UUID.randomUUID().toString().replace("-", "");
		Connection conn = CASDatabaseMngr.getConnection();
		String sql = "insert into cas_session "
				+ "values( ? , ? , ? , now() ,date_add(now(), interval 15 minute))";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ticket);
			pstmt.setInt(2, uid);
			pstmt.setString(3, "WA");
			pstmt.execute();
			SessionTicket sessionTicket = new SessionTicket();
			sessionTicket.ticket = ticket;
			return sessionTicket;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public static int readTicket(SessionTicket sessionTicket)
			throws SessionTicketException, SQLException {
		Connection conn = CASDatabaseMngr.getConnection();
		String sql = "select * from cas_session where uuid = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sessionTicket.ticket);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int uid = rs.getInt("uid");
				String state = rs.getString("state");
				Date create_time = rs.getDate("create_time");
				Date revoke_time = rs.getDate("revoke_time");
				System.out.println("state: "+state);
				System.out.println("create_time: "+create_time);
				System.out.println("revoke_time: "+revoke_time);
				return uid;
			} else
				throw new SessionTicketException("SessionTicketNotFound");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
		}
	}
}

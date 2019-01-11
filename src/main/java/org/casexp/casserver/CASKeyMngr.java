package org.casexp.casserver;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class CASUser {
	public int uid;
	public String account;
	public byte[] salt;
	public byte[] password;
	public Date reg_time;
}

class CASTicketException extends Exception {
	public CASTicketException(String msg) {
		super(msg);
	}
}

class CASTicketWrongPWException extends CASTicketException {
	public CASTicketWrongPWException(String msg) {
		super(msg);
	}
}

class CASTicketWrongACException extends CASTicketException {
	public CASTicketWrongACException(String msg) {
		super(msg);
	}
}

public class CASKeyMngr {

	public int login(CASTicket ticket) throws SQLException, CASTicketException {

		try {
			CheckCASTicket(ticket);
			CASUser user = findUserbyAccount(ticket.account);
			byte[] actualPW = user.password;
			if (CheckPassword(ticket.type, ticket.password, actualPW)) {
				return user.uid;
			} else {
				throw new CASTicketWrongPWException("WrongPassword");
			}
		} catch (IllegalArgumentException e) {
			throw new CASTicketException(e.getMessage());
		}
	}

	private CASUser findUserbyAccount(String account)
			throws CASTicketWrongACException, SQLException {
		Connection conn = CASDatabaseMngr.getConnection();
		String sql = "select * from cas_users where account = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, account);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				CASUser user = new CASUser();
				user.uid = rs.getInt("uid");
				user.account = rs.getString("account");
				user.salt = rs.getBytes("salt");
				user.password = rs.getBytes("password");
				user.reg_time = rs.getDate("reg_time");
				return user;
			} else
				throw new CASTicketWrongACException("WrongAccount");
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

	private boolean CheckCASTicket(CASTicket ticket)
			throws IllegalArgumentException {
		if (ticket.type.equals("normal"))
			return true;
		throw new IllegalArgumentException("UnknowCASTicketType");
	}

	private boolean CheckPassword(String type, String ticketPW,
			byte[] actualPW) {
		if (type.equals("normal"))
			return CheckPWSHA512(ticketPW, actualPW);
		return false;
	}

	private boolean CheckPWSHA512(String ticketPW, byte[] actualPW) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			byte[] hash = digest
					.digest(ticketPW.getBytes(StandardCharsets.UTF_8));
			if (Arrays.equals(hash, actualPW))
				return true;
		} catch (NoSuchAlgorithmException e) {
		}
		return false;
	}

}

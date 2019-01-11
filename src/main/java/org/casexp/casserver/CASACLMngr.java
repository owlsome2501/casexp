package org.casexp.casserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CASACLMngr {

	private Map<String, String> extra_acl = null;
	private int otherUid;
	private Map<String, Boolean> cas_attr_acl = null;
	int uid = 0;

	public CASACLMngr(int uid) {
		this.uid = uid;
	}

	public Map<String, Boolean> getCasAttrACL() throws SQLException {
		if (cas_attr_acl == null) {
			loadCASServiceACL();
		}
		return cas_attr_acl;
	}

	public Map<String, String> getExtraALC(int otherUid) throws SQLException {
		if (extra_acl == null || this.otherUid != otherUid) {
			this.otherUid = otherUid;
			loadExtraACL();
		}
		return extra_acl;
	}

	private void loadCASServiceACL() throws SQLException {
		PreparedStatement pstmt = null;
		String sql = "select "
				+ "ifnull(max(cas_attr_acl.p_create_user), false) as p_create_user, "
				+ "ifnull(max(cas_attr_acl.p_read_user), false) as p_read_user, "
				+ "ifnull(max(cas_attr_acl.p_update_u2a), false) as p_update_u2a, "
				+ "ifnull(max(cas_attr_acl.p_update_attr), false) as p_update_attr, "
				+ "ifnull(max(cas_attr_acl.p_create_ticket), false) as p_create_ticket, "
				+ "ifnull(max(cas_attr_acl.p_read_ticket), false) as p_read_ticket, "
				+ "ifnull(max(cas_attr_acl.p_create_extra_acl), false) as p_create_extra_acl, "
				+ "ifnull(max(cas_attr_acl.p_read_extra_acl), false) as p_read_extra_acl "
				+ "from "
				+ "cas_users join cas_u2a on cas_users.uid = cas_u2a.uid "
				+ "join cas_attr_acl on cas_u2a.aid = cas_attr_acl.aid "
				+ "where cas_users.uid = ?";
		Connection conn = CASDatabaseMngr.getConnection();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, this.uid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Map<String, Boolean> acl = new HashMap<String, Boolean>();
				acl.put("p_create_user", rs.getBoolean("p_create_user"));
				acl.put("p_read_user", rs.getBoolean("p_read_user"));
				acl.put("p_update_u2a", rs.getBoolean("p_update_u2a"));
				acl.put("p_update_attr", rs.getBoolean("p_update_attr"));
				acl.put("p_create_ticket", rs.getBoolean("p_create_ticket"));
				acl.put("p_read_ticket", rs.getBoolean("p_read_ticket"));
				acl.put("p_create_extra_acl",
						rs.getBoolean("p_create_extra_acl"));
				acl.put("p_read_extra_acl", rs.getBoolean("p_read_extra_acl"));
				this.cas_attr_acl = Collections.unmodifiableMap(acl);
			}
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

	private void loadExtraACL() throws SQLException {
		PreparedStatement pstmt = null;
		String sql = "select * from extra_acl " + "where "
				+ "extra_acl.aid in ( select cas_u2a.aid from cas_u2a where cas_u2a.uid = ?) "
				+ "and "
				+ "extra_acl.coo_aid in ( select cas_u2a.aid from cas_u2a where cas_u2a.uid = ?)";
		Connection conn = CASDatabaseMngr.getConnection();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, this.uid);
			pstmt.setInt(2, this.otherUid);
			rs = pstmt.executeQuery();
			Map<String, String> eacl = new HashMap<String, String>();
			while (rs.next()) {
				eacl.put(rs.getString("attr_name"), rs.getString("attr_value"));
			}
			this.extra_acl = Collections.unmodifiableMap(eacl);
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

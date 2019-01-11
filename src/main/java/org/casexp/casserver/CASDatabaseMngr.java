package org.casexp.casserver;

import java.sql.Connection;

import java.sql.DriverManager;

public class CASDatabaseMngr {
	private static final String dbClassName = "org.mariadb.jdbc.Driver";
	private static final String CONNECTION = 
			"jdbc:mariadb://127.0.0.1:3306/casexp";
	private static final String USER = "root";
	private static final String PASSWPRD = "my-secret-pw";
	private static Connection conn = null;
	
	static {
		try {
			Class.forName(dbClassName);
			conn = DriverManager.getConnection(CONNECTION,USER,PASSWPRD);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return conn;
	}
}
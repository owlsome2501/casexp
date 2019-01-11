package org.casexp.casserver;

public class CASExp {
	private static String keypath = "sslsocket.keystore";
	private static char[] passwd = "casexp".toCharArray();
	
	public static void main(String[] args) {
		try {
			CASServer server = new CASServer(8012,keypath,passwd);
			server.run();
			server.join();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
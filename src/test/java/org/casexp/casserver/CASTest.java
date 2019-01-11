package org.casexp.casserver;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class CASTest {

	private static String keypath = "sslclient.keystore";
	private static char[] passwd = "casexp".toCharArray();
	private SSLSocketFactory sFactory = null;

	@Before
	public void init() {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(new FileInputStream(keypath), passwd);
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance("SunX509");
			trustManagerFactory.init(keyStore);
			TrustManager[] trustManagers = trustManagerFactory
					.getTrustManagers();
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, trustManagers, null);
			sFactory = context.getSocketFactory();
		} catch (Exception e) {
		}
	}

	@Test
	public void GlobalTest() {
		SSLSocket socket = null;
		try {
			socket = (SSLSocket) sFactory.createSocket("127.0.0.1", 8012);

			PrintWriter pWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			CASQueryPackage casq = new CASQueryPackage();
			CASTicket ticket = new CASTicket();
			ticket.type = "normal";
			ticket.account = "alice";
			ticket.password = "alice";
			casq.ticket = ticket;
			casq.query = new CASQuery[] {};

			Gson gson = new Gson();
			pWriter.println(gson.toJson(casq));
			pWriter.flush();
			String answer = bReader.readLine();
			System.out.println(answer);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
			}
		}
	}

	@Test
	public void WrongQuerySyntaxTest() {
		SSLSocket socket = null;
		try {
			socket = (SSLSocket) sFactory.createSocket("127.0.0.1", 8012);

			PrintWriter pWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			CASQueryPackage casq = new CASQueryPackage();
			CASTicket ticket = new CASTicket();
			ticket.type = "normal";
			ticket.password = "alice";
			casq.ticket = ticket;
			casq.query = new CASQuery[] {};

			Gson gson = new Gson();
			pWriter.println(gson.toJson(casq));
			pWriter.flush();
			String answer = bReader.readLine();
			System.out.println(answer);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
			}
		}
	}
}

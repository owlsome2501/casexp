package org.casexp.casserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import java.util.logging.*;

class CASServerFault extends Exception {
	public CASServerFault(String msg) {
		super(msg);
	}
}

public class CASServer extends Thread {

	SSLServerSocketFactory sslServerSocketFactory = null;
	SSLServerSocket sslServerSocket = null;
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	int port;

	public CASServer(int port, String keypath, char[] passwd) {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(new FileInputStream(keypath), passwd);
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance("SunX509");
			keyManagerFactory.init(keyStore,passwd);
			KeyManager[] kManagers = keyManagerFactory.getKeyManagers();
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(kManagers, null, null);
			sslServerSocketFactory = context.getServerSocketFactory();

			this.port = port;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			if (sslServerSocketFactory != null) {
				sslServerSocket = (SSLServerSocket) sslServerSocketFactory
						.createServerSocket(port);
				logger.info("CASServer started");
				while (true) {
					Socket socket = sslServerSocket.accept();
					logger.info("CASServer lined");
					new CASWorker(socket).run();
				}
			} else {
				throw new CASServerFault("sslSocketFactory init failed");
			}
		} catch (Exception e) {
			logger.warning("FatalError " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (sslServerSocket != null)
					sslServerSocket.close();
			} catch (IOException e) {
			}
		}
	}
}

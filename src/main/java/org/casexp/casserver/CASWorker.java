package org.casexp.casserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import com.google.gson.JsonSyntaxException;

public class CASWorker extends Thread {

	private Socket socket = null;
	private PrintWriter pWriter = null;
	private BufferedReader bReader = null;
	Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public CASWorker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			bReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String packedQuery = bReader.readLine();
			logger.info("recv: "+packedQuery);
			CASQueryPackage casq = CASQueryPackage.unpack(packedQuery);
			CASServiceMngr serviceMngr = new CASServiceMngr(casq);
			serviceMngr.exec();
			if (serviceMngr.hasAns()) {
				String answer = serviceMngr.getAns().pack();
				pWriter = new PrintWriter(socket.getOutputStream());
				pWriter.println(answer);
				pWriter.flush();
			}
		}catch (JsonSyntaxException e) {
			logger.warning(e.getMessage());
		} catch (Exception e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bReader != null)
					bReader.close();
				if (pWriter != null)
					pWriter.close();
				if (socket != null && !socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
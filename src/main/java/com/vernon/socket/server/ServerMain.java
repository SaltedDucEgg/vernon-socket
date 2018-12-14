package com.vernon.socket.server;

import org.apache.log4j.Logger;

/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-6-9
 */
public class ServerMain {

	private static Logger LOGGER = Logger.getLogger(ServerMain.class);

	public static void main(String[] args) {
		LOGGER.info(" *************** server started !!! ****************");
		new ChatServer().start();
	}

}

package com.vernon.socket.client;

import org.apache.log4j.Logger;



/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-6-9
 */
public class Main {
	private static Logger LOGGER = Logger.getLogger(Main.class) ;
	
	public static void main(String[] args) {
		LOGGER.info(" ******************* a new client created *******************") ;
		new ChatFrame().init() ;
	}

}

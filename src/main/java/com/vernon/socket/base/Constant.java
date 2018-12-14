package com.vernon.socket.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-6-9
 */
public class Constant
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826654788240594811L;

	private static Logger LOGGER = Logger.getLogger(Constant.class);

	public static String HOST = "http://www.vernon.com";

	public static int PORT = 8099;

	private static Properties properties = new Properties();

	static {
		try {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("constant.properties");
			if (urls == null || !urls.hasMoreElements()) {
				LOGGER.info(" no constant config find ! please put constant.properties in your classpath ");
			}
			// 遍历
			if (urls.hasMoreElements()) {
				String filepath = urls.nextElement().getFile();
				LOGGER.info("constant filepath: " + filepath);
				properties.load(new FileInputStream(new File(filepath)));

				if (properties.getProperty("HOST") != null) {
					HOST = properties.getProperty("HOST");
					LOGGER.info("init constant.HOST: " + HOST);
				}

				if (properties.getProperty("PORT") != null) {
					PORT = Integer.parseInt(properties.getProperty("PORT"));
					LOGGER.info("init constant.PORT: " + PORT);
				}

			}
		} catch (IOException e) {
			LOGGER.error("load constant.properties error");
		}
	}

	public static String getProperty(String key) {
		return getProperty(key, "");
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static void main(String[] args) {
		// Constant c = new Constant();
	}
}

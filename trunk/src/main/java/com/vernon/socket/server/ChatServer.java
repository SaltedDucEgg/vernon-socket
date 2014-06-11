package com.vernon.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vernon.socket.base.Constant;

/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-6-13
 */
public class ChatServer {

	// ---------------------------------- field names ---------------------------------- 

	private static Logger LOGGER = Logger.getLogger(Main.class);
	boolean started = false;
	ServerSocket serverSocket = null;

	List<Client> clients = new ArrayList<Client>();

	// ---------------------------------- other methods ---------------------------------- 

	public void start() {
		try {
			serverSocket = new ServerSocket(Constant.PORT);
			started = true;
		} catch (SocketException e) {
			LOGGER.error("port be used , please stop your server or restart your server.");
		} catch (IOException e) {
			LOGGER.error(e);
		}
		try {
			while (started) {
				Socket socket = serverSocket.accept();
				LOGGER.info("a client connected");
				Client client = new Client(socket);
				clients.add(client);
				new Thread(client).start();
			}
		} catch (IOException e) {
			LOGGER.error(e);
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 客户端
	 * 
	 * @author Vernon.Chen
	 * @date 2013-6-13
	 */
	private class Client
		implements Runnable {

		// ----------------------------- filed names -----------------------------

		private Socket socket;
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isConnected = false;

		// ----------------------------- constructor methods -----------------------

		public Client(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				isConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// ----------------------------------- other methods -----------------------

		@Override
		public void run() {
			try {
				while (isConnected) {
					String message = dis.readUTF();
					LOGGER.info("message: " + message);
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						c.send(message);
					}
				}
			} catch (EOFException e) {
				LOGGER.info("client closed!");
			} catch (IOException e) {
				LOGGER.error(e);
			} finally {
				try {
					if (dis != null)
						dis.close();
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}

		/**
		 * 发送消息
		 * 
		 * @param message
		 */
		public void send(String message) {
			try {
				dos.writeUTF(message);
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}

	}

}

package com.vernon.socket.client;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.vernon.socket.base.Constant;

/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-6-9
 */
public class ChatFrame
		extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1399044476804987597L;

	private static Logger LOGGER = Logger.getLogger(ClientMain.class);
	private TextField input = new TextField();
	private TextArea contentShow = new TextArea();
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean isConnected = false;
	Thread recvMessage = new Thread(new RecvMessage());

	/**
	 * 初始化
	 */
	public void init() {
		setTitle("与 " + System.currentTimeMillis() + " 聊天中...");
		setLocation(400, 300);
		setSize(300, 300);
		add(input, BorderLayout.SOUTH);
		add(contentShow, BorderLayout.NORTH);
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconect();
				System.exit(0);
			}
		});
		input.addActionListener(new InputListener());
		setVisible(true);
		// 获取链接
		connect();
		recvMessage.start();
	}

	/**
	 * 获取链接
	 */
	public void connect() {
		try {
			socket = new Socket(Constant.HOST, Constant.PORT);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			LOGGER.info("connected!");
			isConnected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接
	 */
	public void disconect() {
		try {
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输入监听
	 * 
	 * @author Vernon.Chen
	 * @date 2013-6-13
	 */
	private class InputListener
		implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String newInput = input.getText();
			input.setText("") ;
			// 并且发动到服务端
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(newInput);
				dos.flush();
			} catch (IOException e1) {
				LOGGER.error(e1);
			}
		}

	}

	/**
	 * 消息接收
	 * 
	 * @author Vernon.Chen
	 * @date 2013-6-13
	 */
	private class RecvMessage
		implements Runnable {

		public void run() {
			try {
				while (isConnected) {
					String str = dis.readUTF();
					contentShow.setText(contentShow.getText() + str + '\n');
				}
			} catch (SocketException e) {
				System.out.println("quit , bye-bye !");
				LOGGER.info("");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}

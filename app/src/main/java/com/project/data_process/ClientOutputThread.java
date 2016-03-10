package com.project.data_process;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientOutputThread extends Thread {
	private Socket socket;
	private byte[] info;
	private boolean flag = true;

	public ClientOutputThread() {
		super();
	}

	public ClientOutputThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			OutputStream out = socket.getOutputStream();
			// socket will be going on working if there has message to be sent
			while (flag) {
				if (info != null) {
					out.write(info);
					// avoid sending the same message once again.
					info = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public byte[] getInfo() {
		return info;
	}

	public void setInfo(byte[] info) {
		this.info = info;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}

package com.project.data_process;

import android.os.Handler;
import android.os.Message;

public class InfoProcessor {
	/**
	 * find the responding method according to the input message
	 * 
	 * @param info
	 *            information of the message.
	 * @param len
	 *            the length of the message.
	 */
	public static void processInfo(Handler handler, byte[] info, int len) {
		Constant command = Constant.Integer2Constant(info[0]);
		switch (command) {
		case CONNECT:
			processConnect(handler, info, len);
			break;
		case LAST_DATA:
			processLastData(handler, info, len);
			break;
		case HISTORICAL_DATA:
			processHistoricalData(handler, info, len);
			break;
		case DELETE:
			processDelete(handler, info, len);
			break;
		case FINISH:
			processFinish(handler, info, len);
			break;
		default:
			processPostMessage(handler);
			break;
		}
	}

	/**
	 * this method will be called after receiving the connecting message from server.
	 * 
	 * @param info
	 * @param lens
	 */
	private static void processConnect(Handler handler, byte[] info, int len) {
		Constant reply = Constant.Integer2Constant(info[2]);
		System.out.println("client processconnect");
		Message msg = Message.obtain();
		msg.arg1 = reply.getValue();
		msg.obj = new String(info, 4, len - 4);
		handler.sendMessage(msg);
	}

	private static void processLastData(Handler handler, byte[] info, int len) {
		Message msg = Message.obtain();
		msg.obj = info;
		handler.sendMessage(msg);
	}

	private static void processHistoricalData(Handler handler, byte[] info,
			int len) {

	}

	private static void processDelete(Handler handler, byte[] info, int len) {

	}

	public static void processFinish(Handler handler, byte[] info, int len) {

	}

	public static void processPostMessage(Handler handler) {
		System.out.println("postmessage");
	}

}

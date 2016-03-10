package com.project.data_process;

public class Command {
	/**
	 * get the connecting request message from client
	 * 
	 * @param username user name
	 * @param pws password
	 * @return
	 */
	public static byte[] sendConnectRequest(String username, String pws, String code) {
		String str = username + "," + pws + "," + code;
		int length = 3 + str.getBytes().length;
		byte[] info1 = new byte[3];
		// we have set the first three bytes.
		info1[0] = (byte) Constant.CONNECT.getValue();
		info1[1] = (byte) Constant.REQUEST.getValue();
		info1[2] = (byte) length;
		// warning the order.
		String result = new String(info1, 0, info1.length) + str;
		return result.getBytes();
	}

	/**
	 * send a command that request update the last data to server.
	 * 
	 * @param index
	 *            index of message package
	 * @return
	 */
	public static byte[] sendLastDataRequest(byte index) {
		byte[] info1 = new byte[4];
		// we have set the first three bytes.
		info1[0] = (byte) Constant.LAST_DATA.getValue();
		info1[1] = (byte) Constant.REQUEST.getValue();
		info1[2] = 4; // length of package.s
		info1[3] = index;
		return info1;
	}
}

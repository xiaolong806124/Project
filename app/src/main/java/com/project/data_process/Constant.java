package com.project.data_process;

public enum Constant {
	/* Command*/
	CONNECT(1), LAST_DATA(2), HISTORICAL_DATA(3), DELETE(4), FINISH(5),
	/* Command Type */
	REQUEST(6), RESPONSE(7),
	/* Result type of server respond */
	SUCCESS(8), FAIL(9);
	private int value;

	private Constant(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * transform from byte value to Constant value.
	 * 
	 * @param v
	 *            byte value
	 * @return Constant value
	 */
	public static Constant Integer2Constant(int v) {
		Constant[] values = Constant.values();
		for (Constant con : values) {
			if (con.value == v)
				return con;
		}
		return null;
	}
}

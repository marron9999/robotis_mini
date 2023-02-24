package packet;

public class ERROR {

	public static final int RESULT_FAIL = 1;
	public static final int INSTRUCTION = 2;
	public static final int CRC = 3;
	public static final int DATA_RANGE = 4;
	public static final int DATA_LENGTH = 5;
	public static final int DATA_LIMIT = 6;
	public static final int ACCESS = 7;
	public static final int NONE = 0;

	public static String toString(int v) {
		v &= 0x00ff;
		if(v == NONE) return "NONE";
		if(v == RESULT_FAIL) return "RESULT_FAIL";
		if(v == INSTRUCTION) return "INSTRUCTION";
		if(v == CRC) return "CRC";
		if(v == DATA_RANGE) return "DATA_RANGE";
		if(v == DATA_LENGTH) return "DATA_LENGTH";
		if(v == DATA_LIMIT) return "DATA_LIMIT";
		if(v == ACCESS) return "ACCESS";
		return "" + v;
	}
}

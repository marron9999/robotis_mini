package packet;

public class INSTRUCTION {

	public static final byte PING = 0x01;
	public static final byte READ = 0x02;
	public static final byte WRITE = 0x03;
	public static final byte REG_WRITE = 0x04;
	public static final byte ACTION = 0x05;
	public static final byte BULK_READ = (byte) 0x92;
	public static final byte BULK_WRITE = (byte) 0x93;
	public static final byte RESET = 0x06;
	public static final byte CLEAR = 0x10;
	public static final byte STATUS = 0x55;
	public static final byte SYNC_READ = (byte) 0x82;
	public static final byte SYNC_WRITE = (byte) 0x83;
	public static final byte REBOOT = 0x08;

	public static String toString(int v) {
		v &= 0x00ff;
		if(v == WRITE) return "WRITE";
		if(v == SYNC_WRITE) return "SYNC_WRITE";
		if(v == BULK_WRITE) return "BULK_WRITE";
		if(v == SYNC_READ) return "SYNC_READ";
		if(v == READ) return "READ";
		if(v == STATUS) return "STATUS";
		if(v == PING) return "PING";
		if(v == REG_WRITE) return "REG_WRITE";
		if(v == ACTION) return "ACTION";
		if(v == RESET) return "RESET";
		if(v == CLEAR) return "CLEAR";
		if(v == REBOOT) return "REBOOT";
		return "" + v;
	}
}

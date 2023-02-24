package packet;

public class ID {

	public static final int BROADCAST = 0x00fe;

	public static String toString(int v) {
		v &= 0x00ff;
		if(v == BROADCAST) return "BROADCAST";
		if(v == CM904.ID) return "CM904";
		return "" + v;
	}
}

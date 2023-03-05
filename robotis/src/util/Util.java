package util;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Util {
	public static Node next(Node node, String name) {
		while( ! node.getNodeName().equalsIgnoreCase(name)) {
			node = node.getNextSibling();
			if(node == null) return null;
		}
		return node;
	}

	public static String parseString(NamedNodeMap attrs, String name) {
		return attrs.getNamedItem(name).getNodeValue().trim();
	}

	public static double parseDouble(NamedNodeMap attrs, String name) {
		try {
			return Double.parseDouble(attrs.getNamedItem(name).getNodeValue().trim());
		} catch (Exception e) {
			// NONE
		}
		return 0;
	}

	public static int parseInt(NamedNodeMap attrs, String name) {
		try {
			return Integer.parseInt(attrs.getNamedItem(name).getNodeValue().trim());
		} catch (Exception e) {
			// NONE
		}
		return 0;
	}

	public static boolean parseBoolean(NamedNodeMap attrs, String name) {
		try {
			return Boolean.parseBoolean(attrs.getNamedItem(name).getNodeValue().trim());
		} catch (Exception e) {
			// NONE
		}
		return false;
	}

	public static double parseDouble(String value) {
		try {
			return Double.parseDouble(value.trim());
		} catch (Exception e) {
			// NONE
		}
		return 0;
	}

	public static int parseInt(String value) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			// NONE
		}
		return 0;
	}

	public static boolean parseBoolean(String value) {
		try {
			return Boolean.parseBoolean(value.trim());
		} catch (Exception e) {
			// NONE
		}
		return false;
	}
	
	public static String double2string(double v) {
		String d = "" + v;
		int p = d.indexOf(".");
		if(p > 0) d = (d + "00").substring(0, p + 3);
		return d;
	}

	public static String _byte(byte b) {
		int bb = b & 0x00ff;
		String d = "" + bb;
		Character.UnicodeBlock block = Character.UnicodeBlock.of(bb);
		if( (!Character.isISOControl(bb)) &&	
	            b != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS)
			d += " (" + ((char) bb) + ")";
		return d;
	}

//	public 	static int degree2position(double degree) {
//		//  degree = -150.0 �` +150.0
//		//  position =         0 �` 1023
//		double position = degree + XL320.POSITION_DEGREE/*�x*/;
//		position = position * (double) XL320.POSITION_MAX;
//		position = position  / (XL320.POSITION_DEGREE * (double) 2/*�x*/);
//		return (int) position;
//	}

//	public 	static double position2degree(int position) {
//		//  position = 0 �` 1023
//		//  =>   -150.0 �` +150.0
//		double degree = (double) position - XL320.POSITION_CENTER;
//		degree = degree * XL320.POSITION_DEGREE * (double) 2/*�x*/;
//		degree = degree / (double) XL320.POSITION_MAX;
//		return degree;
//	}

//	public 	static int position2speed(int position, int msec) {
//		//  position = 0 �` 1023  
//		//  =>   1 �` 1023
//		double speed = XL320.SPEED_RPM * (double) 360/*�x*/;
//		speed = speed /  (double) 60/*�b*/ /  (double) 1000/*�~���b*/;
//		speed = speed * (double) msec;
//		//		��		�w��~���b�ŉ�]�\�ȓx / �P��
//		double degree = (double) position * (double) 300/*�x*/ / (double) 1023;
//		//		��		��]�x
//		 speed = degree / speed;
//		if(speed < 1.0) return 1;
//		if(speed > 1023) return 1023;
//		return (int) speed;
//	}

//	public 	static int position2point(int position, int length) {
//		double degree = Util.position2degree(position);
//		double radian = Math.toRadians(degree);
//		int x = (int) ( (double) length * Math.sin(radian));
//		int y = (int) ( (double) length * Math.cos(radian));
//		return (x << 16) | y;
//	}

	public static String toHex(byte n) {
		return toHex((int) n);
	}
	public static String toHex(int n) {
		char[] c = new char[2];
		c[0] = "0123456789ABCDEF".charAt((n >> 4) & 0x0f);
		c[1] = "0123456789ABCDEF".charAt(n & 0x0f);
		return new String(c);
	}

	public static byte getLowByte(int a) {
		return (byte) (a & 0x00ff);
	}
	public static byte getHighByte(int a) {
		return (byte) ((a >> 8) & 0x00ff);
	}

	public static int getLowWord(int a) {
		return 0x00ffff & a;
	}
	public static int getHighWord(int a) {
		return (a >> 16) & 0x00ffff;
	}
	public static int getLowShort(int a) {
		return (short)(0x00ffff & a);
	}
	public static int getHighShort(int a) {
		return (short)(a >> 16);
	}

	public static int toWord(int low, int high) {
		return (low & 0x00ff) | ((high & 0x00ff) << 8);
	}
	public static int toWord(byte low, byte high) {
		return toWord((int)low, (int) high);
	}

	public static void sleep(int milliseconds) {
		if(milliseconds < 10) return;
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			// NONE
		}
	}

	public static Properties loadProperties(String propfile) throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(propfile);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		properties.load(isr);
		isr.close();
		fis.close();
		return properties;
	}
	public static void  saveProperties(String propfile, Properties properties)  throws Exception {
		FileOutputStream fos = new FileOutputStream(propfile);
		properties.store(fos, propfile);
		fos.close();
	}

	public static String right(String s, int len) {
		return s.substring(s.length() - len);
	}

	public static String sec() {
		return sec(0);
	}
	public static String sec(long t0) {
		long t1 = System.currentTimeMillis();
		t1 -= t0;
		long t2 = t1 % 1000; 
		t1 = (t1 /= 1000) % 60;
		return right("0" + t1, 2) + "." + right("00" + t2, 3);
	}

//	public static int[] list2array(List<Integer> buffer) {
//		int[] data = new int[buffer.size()];
//		for (int i = 0; i < buffer.size(); i++) {
//			data[i] = buffer.get(i).intValue();
//		}
//		return data;
//	}

	public static boolean VERBOSE = false;
	public static PrintStream out = System.out;
	public static PrintStream ps;

	public static void info(String arg1, String arg2) {
		println("[I] " + arg1 + ": " + arg2);
	}

	public static void error(String arg1, String arg2) {
		println("[E] " + arg1 + ": " + arg2);
	}

	public static void error(String arg1, Exception e) {
		println("[E] " + arg1 + ": " + e.getMessage());
	}

	public static void print(String str)  {
		if(str.length() > 0) {
			out.print(str);
			if(ps != null)
				ps.print(str);
		}
	}
	public static void println(String str)  {
		if(str.length() > 0) {
			out.println(str);
			if(ps != null)
				ps.println(str);
		}
	}
	public static void println()  {
		out.println();
		if(ps != null)
			ps.println();
	}

	public static void verbose(String str)  {
		if(VERBOSE) {
			print(str);
		}
	}
	public static void verboseln(String str)  {
		if(VERBOSE) {
			println(str);
		}
	}
	public static void verboseln()  {
		if(VERBOSE) {
			println();
		}
	}
}

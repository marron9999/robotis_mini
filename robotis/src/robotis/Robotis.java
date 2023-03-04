package robotis;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

import bluetooth.Bluetooth;
import packet.MOTION;
import packet.CM904;
import packet.PACKET;
import packet.XL320;
import util.Util;

public class Robotis {
	public static Robotis instance;
	public final String PROPFILE = "robotis.properties";
	public PrintStream out;
	public PrintStream ps;
	public Properties properties;
	public Bluetooth bluetooth;
	public boolean VERBOSE = false;
	public int internal_sleep = 100;
	public int timeout_packet = 1000;
	public int timeout_motion = 3000;

	public void verbose_packet(byte[] buffer, int length)  {
		String str = PACKET.format(buffer, length);
		verboseln(str);
	}

	public Robotis() {
		super();
		instance = this;
		out = System.out;
		properties = new Properties();
		try {
			properties = Util.loadProperties(PROPFILE);
		} catch (Exception e) {
			try {
				InputStream is = getClass().getResourceAsStream("/robotis/" + PROPFILE);
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				properties.load(isr);
				isr.close();
				is.close();
			} catch (Exception e2) {
				// NONE
			}
		}

		try {
			XL320.MAX = Integer.parseInt(properties.getProperty("servo.max"));
		} catch (Exception e) { }
//		try {
//			CM904.IR = Integer.parseInt(properties.getProperty("sensor.ir"));
//		} catch (Exception e) { }
//		try {
//			CM904.GYRO_X = Integer.parseInt(properties.getProperty("sensor.gyro.x"));
//		} catch (Exception e) { }
//		try {
//			CM904.GYRO_Y = Integer.parseInt(properties.getProperty("sensor.gyro.y"));
//		} catch (Exception e) { }
	}

	public void motion(int no) {
		motion(no, timeout_motion);
	}
	public void motion(int no, int tout) {
		long endTime = 0;
		if(tout > 0) {
			endTime = System.currentTimeMillis() + tout;
		}
		try {
			write_word(CM904.ID, CM904._MotionPlayPage[0], no);
			sleep_timeout(internal_sleep);
			while(true) {
				byte rc = read_byte(CM904.ID, CM904._MotionPlayStatus[0], (byte) 0);
				if(rc == 0) break;
				sleep_timeout(internal_sleep);
				if(endTime > 0) {
					if(System.currentTimeMillis() >= endTime) break;
				}
			}
		} catch (Exception e) {
			error("robotis_motion",  e);
		}
	}

	public void initialize() {
		if(ps == null) {
			try {
				String v = properties.getProperty("file.log");
				ps = new PrintStream(v, "utf-8");
			} catch (Exception e) {
				// NONE
			}
		}
		if(bluetooth == null) {
			try {
				String prefix = properties.getProperty("bluecove.prefix");
				//String uuid = properties.getProperty("bluecove.uuid");
				// Serial Port Profile (SPP)
				String uuid = "00001101-0000-1000-8000-00805F9B34FB";
				bluetooth = new Bluetooth();
				if( ! bluetooth.open(prefix, uuid)) {
					bluetooth = null;
				}
			} catch (Exception e) {
				error("Robotis.initialize",  e);
			}
			if(bluetooth != null) {
				motion(MOTION.SIT_DOWN, timeout_motion);
			}
		}
	}

	public void terminate() {
		close();
		if(ps != null) {
			ps.close();
			ps = null;
		}
	}

	public void close() {
		if(bluetooth == null) return;
		synchronized (bluetooth) {
			try {
				byte rc = read_byte(CM904.ID, CM904._MotionPlayStatus[0], (byte) 0);
				if(rc != 0) {
					motion(MOTION.STOP);
				}
				motion(MOTION.SIT_DOWN, timeout_motion);
			} catch (Exception e) {
				error("robotis_close",  e);
			}
		}
		try {
			bluetooth.close();
		} catch (Exception e) {
			error("bluetooth_close",  e);
		}
		bluetooth = null;
	}

	public void info(String arg1, String arg2) {
		println("[I] " + arg1 + ": " + arg2);
	}

	public void error(String arg1, String arg2) {
		println("[E] " + arg1 + ": " + arg2);
	}

	public void error(String arg1, Exception e) {
		println("[E] " + arg1 + ": " + e.getMessage());
	}

	public void print(String str)  {
		if(str.length() > 0) {
			out.print(str);
			if(ps != null)
				ps.print(str);
		}
	}
	public void println(String str)  {
		if(str.length() > 0) {
			out.println(str);
			if(ps != null)
				ps.println(str);
		}
	}
	public void println()  {
		out.println();
		if(ps != null)
			ps.println();
	}

	public void verbose(String str)  {
		if(VERBOSE) {
			print(str);
		}
	}
	public void verboseln(String str)  {
		if(VERBOSE) {
			println(str);
		}
	}
	public  void verboseln()  {
		if(VERBOSE) {
			println();
		}
	}
	
	public void sleep_timeout(int milliseconds) {
		long endTime = System.currentTimeMillis() + (long) milliseconds;
		while (System.currentTimeMillis() < endTime) {
			try {
				if (this.bluetooth != null) {
					if (this.bluetooth.available() > 0) {
						return;
					}
				} else {
					Thread.sleep(internal_sleep);
				}
			} catch (Exception e) {
				// NONE
			}
		}
	}

//	public void syncread_param(int address, int length, SyncReadParam... syncReadParams) throws Exception {
//		if (this.bluetooth == null) return;
//		if( ! this.bluetooth.isWritable()) return;
//		byte[] buffer = PACKET.buffer_syncread(address, length, syncReadParams);
//		_print_request(buffer);
//		this.bluetooth.write(buffer);
//		sleep_timeout(internal_sleep);
//		status.read(address, syncReadParams.length);
//	}
//	public void syncread_param(int address, int length, List<SyncReadParam> syncReadParams) throws Exception {
//		syncread_param(address, length, syncReadParams.toArray(new SyncReadParam[syncReadParams.size()]));
//	}

//	public void syncread_byte(int id_start, int id_end, int address) throws Exception {
//		syncread_length(address, id_start, id_end, 1);
//	}
//	public void syncread_word(int id_start, int id_end, int address) throws Exception {
//		syncread_length( id_start, id_end, address,2);
//	}
//	public void syncread_length(int id_start, int id_end, int address, int length) throws Exception {
//		SyncReadParam[] params = new SyncReadParam[id_end - id_start + 1];
//		for(int id = 0; id < params.length; id++) {
//			params[id] = new SyncReadParam(id_start + id);
//		}
//		syncread_param(address, 2, params);
//	}
	
	public int read_word(byte id, int address, int defvalue) throws Exception {
		if( ! read_length(id, address, 2))
			return defvalue;
		return PACKET.data_word(0);
	}
	public byte read_byte(byte id, int address, byte defvalue) throws Exception {
		if( ! read_length(id, address, 1))
			return defvalue;
		return PACKET.data_byte(0);
	}
	private boolean read_length(byte id, int address, int read_length) throws Exception {
		synchronized (PACKET.write_buffer) {
			if (this.bluetooth == null) return false;
			if( ! this.bluetooth.isWritable()) return false;
			int length = PACKET.buffer_read(id, address, read_length);
			verbose_packet(PACKET.write_buffer, length);
			this.bluetooth.clear();
			this.bluetooth.write(PACKET.write_buffer, length);
			sleep_timeout(internal_sleep);
			return PACKET.read_status();
		}
	}

	public boolean write_byte(byte id, int address, byte value) throws Exception {
		synchronized (PACKET.write_buffer) {
			if (this.bluetooth == null) return false;
			if( ! this.bluetooth.isWritable()) return false;
			int length = PACKET.buffer_write_byte(id, address, value);
			verbose_packet(PACKET.write_buffer, length);
			this.bluetooth.clear();
			this.bluetooth.write(PACKET.write_buffer, length);
			sleep_timeout(internal_sleep);
			return PACKET.read_status();
		}
	}
	public boolean write_word(byte id, int address, int value) throws Exception {
		synchronized (PACKET.write_buffer) {
			if (this.bluetooth == null) return false;
			if( ! this.bluetooth.isWritable()) return false;
			int length = PACKET.buffer_write_word(id, address, value);
			verbose_packet(PACKET.write_buffer, length);
			this.bluetooth.clear();
			this.bluetooth.write(PACKET.write_buffer, length);
			sleep_timeout(internal_sleep);
			return PACKET.read_status();
		}
	}

//	public void syncwrite_param(int address, SyncWriteParam... syncWriteParams) throws Exception {
//		if(this.bluetooth == null) return;
//		if( ! this.bluetooth.isWritable()) return;
//		if(syncWriteParams.length <= 0) return;
//		byte[] buffer = PACKET.buffer_syncwrite(address, syncWriteParams);
//		_print_request(buffer);
//		this.bluetooth.write(buffer);
//	}
//	public void syncwrite_param(int address, List<SyncWriteParam> syncWriteParams) throws Exception {
//		syncwrite_param(address, syncWriteParams.toArray(new SyncWriteParam[syncWriteParams.size()]));
//	}
//	public void syncwrite_byte(int id_start, int id_end, int ope, int arg) throws Exception {
//		SyncWriteParam[] params = new SyncWriteParam[id_end - id_start + 1];
//		for(int id = 0; id < params.length; id++) {
//			params[id] = new SyncWriteParam(id_start + id, (byte)arg);
//		}
//		syncwrite_param(ope, params);
//	}
//	public void syncwrite_word(int id_start, int id_end, int ope, int arg) throws Exception {
//		SyncWriteParam[] params = new SyncWriteParam[id_end - id_start + 1];
//		for(int id = 0; id < params.length; id++) {
//			params[id] = new SyncWriteParam(id_start + id, Util.getLowByte(arg), Util.getHighByte(arg));
//		}
//		syncwrite_param(ope, params);
//	}
}

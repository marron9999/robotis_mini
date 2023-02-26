package packet;

import java.util.Arrays;

import robotis.Robotis;
import util.CRC16;
import util.Util;

public class PACKET {

	// http://emanual.robotis.com/docs/en/dxl/protocol2/

	public static final int _HEADER0 = 0;
	public static final int _HEADER1 = 1;
	public static final int _HEADER2 = 2;
	public static final int _RESERVED = 3;
	public static final int _ID = 4;
	public static final int _LENGTH_L = 5;
	public static final int _LENGTH_H = 6;
	public static final int _INSTRUCTION = 7;
	public static final int _PARAMETER = 8;
	public static final int _ERROR = 8;
	public static final int _DATA = 9;

	public static final int SIZE_HEADER = _ID;
	public static final int SIZE_CRC = 2;
	public static final int SIZE_ERROR = 1;
	public static final int SIZE_INSTRUCTION = 1;
	public static final int SIZE_ID = 1;
	public static final int SIZE_ADDRESS = 2;
	public static final int SIZE_LENGTH = 2;
	public static final int SIZE_VALUE_WORD = 2;
	public static final int SIZE_VALUE_BYTE = 1;

	public static final byte HEADER0_FF = (byte) 0xff;
	public static final byte HEADER1_FF = (byte) 0xff;
	public static final byte HEADER2_FD = (byte) 0xfd;

	public static byte[] write_buffer = new byte[256];
	public static byte[] read_buffer = new byte[256];
	public static int read_length = 0;

	public static byte data_byte(int idx) throws Exception {
		idx += _DATA;
		return read_buffer[idx];
	}

	public static int data_word(int idx) throws Exception {
		idx += _DATA;
		return Util.toWord(read_buffer[idx], read_buffer[idx + 1]);
	}

	public static boolean read_status() throws Exception {
		Arrays.fill(read_buffer, (byte) 0);
		Robotis robotis = Robotis.instance;
		if (robotis.bluetooth == null) return false;
		read_length = 0;
		long endTime = System.currentTimeMillis() + robotis.timeout_packet;
		int length = -1;
		while (System.currentTimeMillis() < endTime) {
			if (robotis.bluetooth == null) return false;
			if(robotis.bluetooth.available() <= 0) {
				robotis.sleep_timeout(robotis.internal_sleep);
				continue;
			}
			read_buffer[read_length++] = robotis.bluetooth.read();
			endTime = System.currentTimeMillis() + robotis.timeout_packet;
			if(read_length == (PACKET._HEADER0 + 1)) {
				if(read_buffer[PACKET._HEADER0] != PACKET.HEADER0_FF) {
					robotis.verbose_packet(read_buffer, read_length);
					length = -1;
					read_length = 0;
					continue;
				}
			} else if(read_length == (PACKET._HEADER1 + 1)) {
				if(read_buffer[PACKET._HEADER1] != PACKET.HEADER1_FF) {
					robotis.verbose_packet(read_buffer, read_length);
					length = -1;
					read_length = 0;
					continue;
				}
			} else if(read_length == (PACKET._HEADER2 + 1)) {
				if(read_buffer[PACKET._HEADER2] != PACKET.HEADER2_FD) {
					robotis.verbose_packet(read_buffer, read_length);
					length = -1;
					read_length = 0;
					continue;
				}
			} else if(read_length == (PACKET._RESERVED + 1)) {
				if(read_buffer[PACKET._RESERVED] != 0) {
					robotis.verbose_packet(read_buffer, read_length);
					length = -1;
					read_length = 0;
					continue;
				}
			} else if(read_length == (PACKET._LENGTH_H + 1)) {
				length = Util.toWord(read_buffer[PACKET._LENGTH_L], read_buffer[PACKET._LENGTH_H]);
				length += PACKET.SIZE_HEADER + PACKET.SIZE_ID + PACKET.SIZE_LENGTH;
				continue;
			} else if(read_length == length) break;
		}
		robotis.verbose_packet(read_buffer, read_length);
		if(read_length == length) {
			return true;
		}
		return false;
	}
	
	private static int _buffer(byte id, byte instruction) {
		Arrays.fill(write_buffer, (byte) 0);
		write_buffer[_HEADER0] = HEADER0_FF;
		write_buffer[_HEADER1] = HEADER1_FF;
		write_buffer[_HEADER2] = HEADER2_FD;
		write_buffer[_RESERVED] = (byte) 0;
		write_buffer[_ID] = id;
		write_buffer[_INSTRUCTION] = instruction;
		return _INSTRUCTION + SIZE_INSTRUCTION;
	}
	private static void _size(int size) {
		write_buffer[_LENGTH_L] = Util.getLowByte(size);
		write_buffer[_LENGTH_H] = Util.getHighByte(size);
	}
	private static void _crc(int idx) {
		int len = Util.toWord(write_buffer[_LENGTH_L], write_buffer[_LENGTH_H]);
		int crc = CRC16.calc(0, write_buffer,  ((len + _LENGTH_H) + 1 ) - 2);
		write_buffer[idx] = Util.getLowByte(crc);	idx++;
		write_buffer[idx] = Util.getHighByte(crc);	idx++;
	}

	public static int _size() {
		return Util.toWord(read_buffer[_LENGTH_L], read_buffer[_LENGTH_H]);
	}

	public static boolean isStatus(byte[] buffer) {
		if (isPacket(buffer)
		&& buffer[_INSTRUCTION] == INSTRUCTION.STATUS
		) return true;
		return false;
	}
	public static boolean isPacket(byte[] buffer) {
		if (buffer[_HEADER0] == HEADER0_FF
		&& buffer[_HEADER1] == HEADER1_FF
		&& buffer[_HEADER2] == HEADER2_FD
		&& buffer[_RESERVED] == 0
		) return true;
		return false;
	}

	public static String format(byte[] buffer, int length) {
		if(buffer == null) return "";
		if(length == 0) return "";
		StringBuffer sb = new StringBuffer();
		if(isPacket(buffer)) {
			int size = 0;
			try {
				int instruction = buffer[_INSTRUCTION] & 0x00ff;
				size = Util.toWord(buffer[_LENGTH_L], buffer[_LENGTH_H]);
				size += SIZE_HEADER + SIZE_ID + SIZE_LENGTH;
				int i = 0;
				for( ; i< size; i++) {
					if(i == _HEADER0
					|| i == _ID
					|| i == _LENGTH_L
					|| i == _INSTRUCTION
					|| i == size - 2
					) {
						sb.append("[");
					} else {
						if(instruction == INSTRUCTION.STATUS
						&& i == _ERROR
						) {
							sb.append("[");
						} else {
							sb.append(" ");
						}
					}
					sb.append(Util.toHex(buffer[i]));
					if(i == _RESERVED
					|| i == _ID
					|| i == _LENGTH_H
					|| i == _INSTRUCTION
					|| i == size - 1
					) {
						sb.append("]");
					} else if(i == length - 3) {
						sb.append(" ");
					} else {
						if(instruction == INSTRUCTION.STATUS) {
							if(i == _ERROR) {
								sb.append("]");
							}
						}
					}
				}
				for(i++; i< length; i++) {
					sb.append(" ");
					sb.append(Util.toHex(buffer[i]));
				}
				return sb.toString();
			} catch (Exception e) {
				//
			}
		}
		sb.setLength(0);
		for(int i = 0; i< length; i++) {
			if(i != 0)
				sb.append(" ");
			sb.append(Util.toHex(buffer[i]));
		}
		return sb.toString();
	}

	public static int buffer_write_word(byte id, int address, int... values) {
		int idx = _buffer(id, INSTRUCTION.WRITE);
		write_buffer[idx++] = Util.getLowByte(address);
		write_buffer[idx++] = Util.getHighByte(address);
		for (int value : values) {
			write_buffer[idx++] = Util.getLowByte(value);
			write_buffer[idx++] = Util.getHighByte(value);
		}
		_size(idx - _INSTRUCTION + SIZE_CRC);
		_crc(idx); idx+=SIZE_CRC;
		return idx;
	}
	public static int buffer_write_byte(byte id, int address, byte value) {
		int idx = _buffer(id, INSTRUCTION.WRITE);
		write_buffer[idx++] = Util.getLowByte(address);
		write_buffer[idx++] = Util.getHighByte(address);
		write_buffer[idx++] = value;
		_size(idx - _INSTRUCTION + SIZE_CRC);
		_crc(idx); idx+=SIZE_CRC;
		return idx;
	}

//	private byte[] packet_bulkWrite(BulkWriteParam... bulkWriteParams) {
//	int size = PACKET.SIZE_BASE;
//	for (BulkWriteParam param : bulkWriteParams) {
//		size += PACKET.SIZE_ID
//				+ PACKET.SIZE_ADDRESS
//				+ PACKET.SIZE_WORD
//				+ param.data.length;
//	}
//	byte[] buffer = PACKET.buffer(size, ID.BROADCAST, INSTRUCTION.BULK_WRITE);
//	int idx = PACKET._PARAMETER;
//	for (BulkWriteParam param : bulkWriteParams) {
//		buffer[idx] = (byte) param.id;		idx++;
//		buffer[idx] = Util.getLowByte(param.address);	idx++;
//		buffer[idx] = Util.getHighByte(param.address);	idx++;
//		buffer[idx] = Util.getLowByte(param.data.length);	idx++;
//		buffer[idx] = Util.getHighByte(param.data.length);	idx++;
//		for(int i = 0; i < param.data.length; i++) {
//			buffer[idx] = param.data[i];	idx++;
//		}
//		listeners_address[param.id] = param.address;
//	}
//	PACKET.size(buffer,
//			idx - PACKET._PARAMETER
//			+ PACKET.SIZE_INSTRUCTION 
//			+ PACKET.SIZE_CRC);
//	PACKET.crc(buffer, idx);
//	return buffer;
//}

//	public static byte[] buffer_syncwrite(int address, SyncWriteParam... syncWriteParams) {
//		int size = SIZE_BASE + SIZE_ADDRESS + SIZE_LENGTH;
//		for (SyncWriteParam param : syncWriteParams) {
//			size += SIZE_ID + param.data.length;
//		}
//		byte[] buffer = buffer(size, ID.BROADCAST, INSTRUCTION.SYNC_WRITE);
//		int idx = _PARAMETER;
//		buffer[idx] = Util.getLowByte(address);	idx++;
//		buffer[idx] = Util.getHighByte(address);	idx++;
//		buffer[idx] = Util.getLowByte(syncWriteParams[0].data.length);	idx++;
//		buffer[idx] = Util.getHighByte(syncWriteParams[0].data.length);	idx++;
//		for (SyncWriteParam param : syncWriteParams) {
//			buffer[idx] = (byte) param.id;	idx++;
//			for(int i = 0; i < param.data.length; i++) {
//				buffer[idx] = param.data[i];	idx++;
//			}
//		}
//		size(buffer, idx - _PARAMETER + SIZE_INSTRUCTION + SIZE_CRC);
//		crc(buffer, idx);
//		return buffer;
//	}

	public static int buffer_read(byte id, int address, int length) {
		int idx = _buffer(id, INSTRUCTION.READ);
		write_buffer[idx++] = Util.getLowByte(address);
		write_buffer[idx++] = Util.getHighByte(address);
		write_buffer[idx++] = Util.getLowByte(length);
		write_buffer[idx++] = Util.getHighByte(length);
		_size(idx - _INSTRUCTION + SIZE_CRC);
		_crc(idx); idx += SIZE_CRC;
		return idx;
	}

//	private byte[] packet_bulkRead(BulkReadParam... bulkReadParams) {
//	int size = PACKET.SIZE_BASE;
//	for (BulkReadParam param : bulkReadParams) {
//		size += PACKET.SIZE_ID
//				+ PACKET.SIZE_ADDRESS
//				+ PACKET.SIZE_WORD
//				+ param.length;
//	}
//	byte[] buffer = PACKET.buffer(size, ID.BROADCAST, INSTRUCTION.BULK_READ);
//	int idx = PACKET._PARAMETER;
//	for (BulkReadParam param : bulkReadParams) {
//		buffer[idx] = (byte) param.id;		idx++;
//		buffer[idx] = Util.getLowByte(param.address);	idx++;
//		buffer[idx] = Util.getHighByte(param.address);	idx++;
//		buffer[idx] = Util.getLowByte(param.length);	idx++;
//		buffer[idx] = Util.getHighByte(param.length);	idx++;
//	}
//	PACKET.size(buffer,
//			idx - PACKET._PARAMETER
//			+ PACKET.SIZE_INSTRUCTION 
//			+ PACKET.SIZE_CRC);
//	PACKET.crc(buffer, idx);
//	return buffer;
//}

//	public static byte[] buffer_syncread(int address, int length, SyncReadParam... syncReadParams) {
//		int size = SIZE_BASE + SIZE_ADDRESS + SIZE_LENGTH;
//		size += SIZE_ID * syncReadParams.length;
//		byte[] buffer = buffer(size, ID.BROADCAST, INSTRUCTION.SYNC_READ);
//		int idx = _PARAMETER;
//		buffer[idx] = Util.getLowByte(address);	idx++;
//		buffer[idx] = Util.getHighByte(address);	idx++;
//		buffer[idx] = Util.getLowByte(length);	idx++;
//		buffer[idx] = Util.getHighByte(length);	idx++;
//		for (SyncReadParam param : syncReadParams) {
//			buffer[idx] = (byte) param.id;	idx++;
//		}
//		size(buffer, idx - _PARAMETER + SIZE_INSTRUCTION + SIZE_CRC);
//		crc(buffer, idx);
//		return buffer;
//	}
}

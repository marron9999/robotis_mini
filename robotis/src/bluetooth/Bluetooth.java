package bluetooth;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import util.Util;

public class Bluetooth extends Bluecove {
	public StreamConnection stream;
	private OutputStream output;
	private InputStream input;
	public String address;
	public String name;
	public RemoteDevice device;
	
	public Bluetooth() {
		super();
	}

	public boolean detect(String prefix) throws Exception {
		if( ! localDevice()) {
			return false;
		}
		if(this.device != null) {
			return true;
		}
		try {
			Vector<RemoteDevice> devices = devices(prefix);
			if(devices.size() > 0) {
				device = devices.get(0);
				name = device.getFriendlyName(true);
				address = device.getBluetoothAddress();
				return true;
			}
		} catch (Exception e) {
			Util.error("bluetooth.open.", e);
		}
		Util.println("No device");
		return false;
	}

	public boolean open(String SERVICE_UUID) throws Exception {
		if(device == null) {
			return false;
		}
		if(this.stream != null) {
			return true;
		}
		try {
			String uuid = SERVICE_UUID.replace("-", "");
			UUID uuids[] = new UUID[] { new UUID(uuid, false) };
			Vector<String> urls = services(device, uuids);
			for (String url : urls) {
				this.stream = (StreamConnection) Connector.open(url, Connector.READ_WRITE);
				if(this.stream != null) {
					this.output = this.stream.openOutputStream();
					this.input = this.stream.openInputStream();
					Util.println("Open " + url);
					//Robotis.info("name", this.name);
					return true;
				}
			}
		} catch (Exception e) {
			Util.error("bluetooth.open.", e);
		}
		return false;
	}

	public void close() throws Exception {
		if(this.output != null) {
			try {
				this.output.close();
				this.output = null;
			} catch (Exception e) {
				Util.error("bluetooth.close", e);
			}
		}
		if(this.input != null) {
			try {
				this.input.close();
				this.input = null;
			} catch (Exception e) {
				Util.error("bluetooth.close", e);
			}
		}
		if (this.stream != null) {
			try {
				this.stream.close();
				this.stream = null;
			} catch (Exception e) {
				Util.error("bluetooth.close", e);
			}
		}
	}

	public void clear() throws Exception {
		if (this.input != null ) {
			while (this.input.available() > 0) {
				this.input.read();
			}
		}
	}

	public void write(byte[] buffer, int length) throws Exception {
		if (this.output != null) {
			this.output.write(buffer, 0, length);
			this.output.flush();
		}
	}

	public int available() throws Exception {
		if (this.input != null) {
			return this.input.available();
		}
		return 0;
	}

	public byte read() throws Exception {
		if (this.input != null ) {
			return (byte) this.input.read();
		}
		return 0;
	}

	public boolean isWritable() throws Exception {
		if(this.output == null) {
			return false;
		}
		return true;
	}


}

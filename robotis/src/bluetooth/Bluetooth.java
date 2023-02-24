package bluetooth;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import robotis.Robotis;

public class Bluetooth extends Bluecove {
	public StreamConnection stream;
	private OutputStream output;
	private InputStream input;
	public String address;
	public String name;
	public String url;

	public Bluetooth() {
		super();
	}

	public boolean open(String prefix, String SERVICE_UUID) throws Exception {
		if( ! localDevice()) return false;
		String uuid = SERVICE_UUID.replace("-", "");
		UUID uuids[] = new UUID[] { new UUID(uuid, false) };
		try {
			for(RemoteDevice device : devices(prefix)) {
				Vector<String> urls = services(device, uuids); 
				for (String url : urls) {
					this.stream = (StreamConnection) Connector.open(url, Connector.READ_WRITE);
					if(this.stream != null) {
						this.output = this.stream.openOutputStream();
						this.input = this.stream.openInputStream();
						this.name = device.getFriendlyName(true);
						this.address = device.getBluetoothAddress();
						this.url = url;
						Robotis.instance.info("bluetooth.open", this.url);
						//Robotis.info("name", this.name);
						return true;
					}
				}
			}
		} catch (Exception e) {
			Robotis.instance.error("bluetooth.open.", e);
		}
		Robotis.instance.info("bluetooth.open",  "No device");
		return false;
	}


	public void close() throws Exception {
		if(this.output != null) {
			try {
				this.output.close();
				this.output = null;
			} catch (Exception e) {
				Robotis.instance.error("bluetooth.close", e);
			}
		}
		if(this.input != null) {
			try {
				this.input.close();
				this.input = null;
			} catch (Exception e) {
				Robotis.instance.error("bluetooth.close", e);
			}
		}
		if (this.stream != null) {
			try {
				this.stream.close();
				this.stream = null;
			} catch (Exception e) {
				Robotis.instance.error("bluetooth.close", e);
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
		if( this.stream == null
		|| this.output == null) {
			return false;
		}
		return true;
	}


}

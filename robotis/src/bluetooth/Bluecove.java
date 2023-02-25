package bluetooth;


import java.util.Properties;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import robotis.Robotis;
import util.Util;

public class Bluecove  implements DiscoveryListener {

	private Vector<RemoteDevice> devices;
	private Vector<String> services;

	// object used for waiting
	private static Object lock = new Object();

	private LocalDevice localDevice;
	private DiscoveryAgent agent;
	private String prefix = null;

	public Bluecove() {
		try {
			if(LocalDevice.isPowerOn()) {
				localDevice = LocalDevice.getLocalDevice();
				agent = localDevice.getDiscoveryAgent();
			}
		} catch (Exception e) {
			// NONE
		}
	}
	
	public boolean localDevice() throws Exception {
		if( ! LocalDevice.isPowerOn()) {
			Robotis.instance.error("bluecove.localDevice", "PowerOff");
			return false;
		}
		if(localDevice == null) {
			Robotis.instance.error("bluecove.localDevice", "No LocalDevice");
			return false;
		}
		if(agent == null) {
			Robotis.instance.error("bluecove.localDevice", "No DiscoveryAgent");
			return false;
		}
		return true;
	}
	
	protected Vector<RemoteDevice> inquiry() throws Exception {
		return devices(null);
	}
	protected Vector<RemoteDevice> devices(String prefix) throws Exception {
		if(prefix != null)
			this.prefix = prefix.toLowerCase();
		devices = new Vector<RemoteDevice>();
		//Robotis.info("getDevices", "startInquiry");
		agent.startInquiry(DiscoveryAgent.LIAC, this);
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				Robotis.instance.error("bluecove.devices", e);
			}
		}
		return devices;
	}

	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		try {
			Robotis.instance.println("Find " + btDevice.getBluetoothAddress());
		} catch (Exception e) {
			// NONE
		}
		// add the device to the vector
		if (!devices.contains(btDevice)) {
			if(prefix == null) {
				devices.addElement(btDevice);
			} else {
				try {
					String name = btDevice.getFriendlyName(false);
					if(name.toLowerCase().startsWith(prefix) ) {
						devices.addElement(btDevice);
					}
				} catch (Exception e) {
					// NONE
				}
			}
		}
	}

	@Override
	public void inquiryCompleted(int discType) {
		switch (discType) {
		case DiscoveryListener.INQUIRY_COMPLETED:
			//Robotis.info("bluecove.inquiryCompleted",  "INQUIRY_COMPLETED");
			break;

		case DiscoveryListener.INQUIRY_TERMINATED:
			Robotis.instance.info("bluecove.inquiryCompleted",  "INQUIRY_TERMINATED");
			break;

		case DiscoveryListener.INQUIRY_ERROR:
			Robotis.instance.info("bluecove.inquiryCompleted",  "INQUIRY_ERROR");
			break;

		default:
			//Robotis.info("bluecove.inquiryCompleted",  "" + discType);
			break;
		}
		synchronized (lock) {
			lock.notify();
		}
	}

	protected Vector<String> services(RemoteDevice rd, UUID uuids[]) throws Exception {
		services = new Vector<String>();
		Robotis.instance.println("Detect " + rd.getFriendlyName(false));
		agent.searchServices(null, uuids, rd, this);
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				Robotis.instance.error("bluecove.services", e);
			}
		}
		return services;
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		for (ServiceRecord sr : servRecord) {
			String url = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			services.add(url);
			Robotis.instance.info("bluecove.servicesDiscovered",  url);
		}
	}

	@Override
	public void serviceSearchCompleted(int transID, int respCode) {
		//Robotis.info("serviceSearchCompleted",  "" + respCode);
		synchronized (lock) {
			lock.notify();
		}
	}

	public static void main(String[] args) {
		String PROPFILE = "robotis.properties";
		String BT_PREFIX = "ROBOTIS";
		String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		try {
			Properties properties = Util.loadProperties(PROPFILE);
			Bluetooth bluecove = new Bluetooth();
			if(bluecove.open(BT_PREFIX, BT_UUID)) {
				System.out.println("Connect successful");
			}
			if(bluecove.stream != null) {
				bluecove.stream.close();
				System.out.println("close connection");
			}
			Util.saveProperties(PROPFILE, properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

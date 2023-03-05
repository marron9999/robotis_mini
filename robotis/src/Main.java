import packet.MOTION;
import robotis.Robotis;

public class Main {

//	class Area {
//		int address; int length; String name;
//		Area(int a, int l, String n) { address = a; length = l; name = n; }
//	}
//	void read(Area area) throws Exception {
//		if(bluetooth == null) return;
//		//_packet_push(false);
//		synchronized (bluetooth) {
//			if(area.length == 2) {
//				if(read_word(CM904.ID, area.address)) {
//					print(area.address + "\t" + area.length + "\t");
//					print("" + PACKET.data_word(0));
//				} else {
//					print(area.address + "\t" + area.length + "\t");
//					print("???");
//				}
//				_println("\t" + area.name);
//			} else if(area.length == 1) {
//				if(read_byte(CM904.ID, area.address)) {
//					print(area.address + "\t" + area.length + "\t");
//					print(Util._byte(PACKET.data_byte(0)));
//				} else {
//					print(area.address + "\t" + area.length + "\t");
//					print("???");
//				}
//				_println("\t" + area.name);
//			}
//		}
//		//_packet_pop();
//	}
	
	public void run() throws Exception {
		Robotis rbotis = Robotis.instance;
		rbotis.initialize();

		rbotis.motion(MOTION.GET_UP);
		rbotis.motion(MOTION.INIT_POS);
		rbotis.sleep_timeout(rbotis.timeout_packet);

		int no = 2;
		String s = rbotis.properties.getProperty("motion." + no, null);
		while(s != null) {
			System.out.println(no + " " + s);
			rbotis.motion(no);
			rbotis.motion(MOTION.STOP);
			rbotis.motion(MOTION.INIT_POS);
			rbotis.sleep_timeout(rbotis.timeout_packet);
			no++;
			s = rbotis.properties.getProperty("motion." + no, null);
		}

		rbotis.terminate();
	}


	public static void main(String[] args) {
		try {
			new Main().run();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

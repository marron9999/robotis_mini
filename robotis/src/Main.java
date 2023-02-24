import packet.MOTION;
import robotis.Robotis;

public class Main extends Robotis{

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
		initialize();

		motion(MOTION.GET_UP);
		motion(MOTION.INIT_POS);
		sleep_timeout(timeout_packet);

		int no = 2;
		String s = properties.getProperty("motion." + no, null);
		while(s != null) {
			System.out.println(no + " " + s);
			motion(no);
			motion(MOTION.STOP);
			motion(MOTION.INIT_POS);
			sleep_timeout(timeout_packet);
			no++;
			s = properties.getProperty("motion." + no, null);
		}

		terminate();
	}


	public static void main(String[] args) {
		try {
			new Main().run();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

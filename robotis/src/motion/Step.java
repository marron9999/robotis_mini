package motion;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class Step {
	public Page page;
	public int frame;
	public double[] pose;

	public String toString() {
		String v = "frame=" + frame
				+ " pose="
				;
		for(double p : pose) {
			v += Util.double2string(p) + " ";
		}
		return v.trim();
	}

//	public void play(Robotis robotis, int frame) {
//		Util.info("Step", toString());
//		int time =  100;
//		long etime = 0;
//		if(frame >= 0) {
//			time = (this.frame - frame) * 10;
//			etime = System.currentTimeMillis() + time;
//		}
//
//		double[] inc = new double[XL320.MAX];
//		int[] pos = new int[XL320.MAX];
//		int[] end = new int[XL320.MAX];
//		int rem = 0;
//		for(int i=0; i<inc.length; i++) {
//			pos[i] = end[i] = robotis.xl320.position[i];
//			inc[i] = 0;
//		}
//		for(int i=0; i<pose.length; i++) {
//			end[i] = (int)Util.degree2position(this.pose[i]);
//			inc[i] = end[i]  - pos[i];
//			if(i < page.softness.length) {
//				inc[i] /= (double) page.softness[i];
//						//	1回の呼び出しで動かす距離
//				if(inc[i] != 0) {
//					rem++;
//					//	動かすモーターをカウント
//					if(inc[i] < 0) {
//						if(inc[i] > -1.0) inc[i] = -1;
//					} else {
//						if(inc[i] < 1.0) inc[i] = 1;
//					}
//				}
//			}
//		}
//		//	動かすモーターがなくなるまで
//		while(rem > 0) {
//			for(int i=0; i<inc.length; i++) {
//				if(inc[i] == 0) continue;
//				pos[i] = (int)( (double) pos[i] +  inc[i] );
//				if(inc[i] > 0) {
//					if(pos[i] > end[i]) {
//						//	最終を超えたら、最終のまま（以後は加算しない）
//						pos[i] = end[i];
//						inc[i] = 0;
//						rem--;
//					}
//				} else {
//					if(pos[i] < end[i]) {
//						//	最終を超えたら、最終のまま（以後は加算しない）
//						pos[i] = end[i];
//						inc[i] = 0;
//						rem--;
//					}
//				}
//			}
//			robotis.servo_position(pos, time);
//		}
//		if(frame >= 0) {
//			etime -= System.currentTimeMillis();
//			if(etime > 0)
//				Util.sleep((int)etime);
//		}
//	}

	public static Step parse(Node node) {
		Step step = new Step();
		NamedNodeMap attrs = node.getAttributes();
		step.frame = Util.parseInt(attrs, "frame");
		String[] v = Util.parseString(attrs, "pose").split(" ");
		step.pose = new double[v.length];
		for(int i=0; i<v.length; i++) {
			step.pose[i] = Double.parseDouble(v[i].trim());
		}
		return step;
	}
}

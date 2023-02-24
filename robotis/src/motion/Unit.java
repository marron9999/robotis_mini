package motion;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class Unit {
	public Flow flow;
	public String main;
	public double mainSpeed;
	public int loop;
	public int exit;
	public double exitSpeed;
	public boolean callSite;

	public String toString() {
		return main + ":"
				+ " mainSpeed=" + Util.double2string(mainSpeed)
				+ " loop=" + loop
				+ " exit=" + exit
				+ " exitSpeed=" + Util.double2string(exitSpeed)
				+ " callSite=" + callSite
				;
	}

//	public void play(Robotis robotis) {
//		Util.info("Unit", toString());
//		// loop分だけ繰り返す
//		Page page = flow.mtnx.pages.get(main);
//		for(int n=0; n < loop; n++) {
//			page.play(robotis);
//		}
//	}

	public static Unit parse(Node node) {
		Unit unit = new Unit();
		NamedNodeMap attrs = node.getAttributes();
		unit.main = Util.parseString(attrs, "main");
		unit.mainSpeed = Util.parseDouble(attrs, "mainSpeed");
		unit.loop = Util.parseInt(attrs, "loop");
		unit.exit = Util.parseInt(attrs, "exit");
		unit.exitSpeed = Util.parseDouble(attrs, "exitSpeed");
		unit.callSite = Util.parseBoolean(attrs, "callSite");
		return unit;
	}
}

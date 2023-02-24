package motion;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class Flow {
	public MTNX mtnx;
	public String name;
	public int retcode;
	public List<Unit> units;

	public String toString() {
		return name + ":"
				+ " retcode=" + retcode
				;
	}

//	public void play(Robotis robotis) {
//		robotis.servo_position();
//		Util.info("Flow", toString());
//		// unit分だけ繰り返す
//		for(int n=0; n < units.size(); n++) {
//			Unit unit = units.get(n);
//			unit.play(robotis);
//		}
//	}

	public static Flow parse(Node node) {
		Flow flow = new Flow();
		NamedNodeMap attrs = node.getAttributes();
		flow.name = Util.parseString(attrs, "name");
		flow.retcode = Util.parseInt(attrs, "return");
		flow.units  = new ArrayList<>();
		return flow;
	}
}

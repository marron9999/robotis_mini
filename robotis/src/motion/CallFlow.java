package motion;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class CallFlow {
	public Bucket bucket;
	public int callIndex;
	public int subIndex;
	public int exitIndex;
	public String flow;

	public String toString() {
		String v = flow + ":"
				+ " callIndex=" + callIndex
				+ " subIndex=" + subIndex
				+ " exitIndex=" + exitIndex
				;
		return v.trim();
	}

	public static CallFlow parse(Node node) {
		CallFlow callflow = new CallFlow();
		NamedNodeMap attrs = node.getAttributes();
		callflow.flow = Util.parseString(attrs, "flow");
		callflow.callIndex = Util.parseInt(attrs, "callIndex");
		callflow.subIndex = Util.parseInt(attrs, "subIndex");
		callflow.exitIndex = Util.parseInt(attrs, "exitIndex");
		return callflow;
	}
}

package motion;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class Bucket {
	public MTNX mtnx;
	public String name;
	public Map<String, CallFlow> callflows;
	public Map<Integer, CallFlow> i2callflow;

	public String toString() {
		String v = name;
		return v.trim();
	}

	public static Bucket parse(Node node) {
		Bucket bucket = new Bucket();
		NamedNodeMap attrs = node.getAttributes();
		bucket.name = Util.parseString(attrs, "name");
		bucket.callflows = new HashMap<>();
		bucket.i2callflow = new HashMap<>();
		return  bucket;
	}
}

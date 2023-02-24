package motion;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import util.Util;

public class Page {
	public MTNX mtnx;
	public String name;
	public int compileSize;
	public int acceleration;
	public int[] softness;
	public List<Step> steps;

	public String toString() {
		String v = name + ":"
				+ " compileSize=" + compileSize
				+ " acceleration=" + acceleration
				+ " softness="
				;
		for(int s : softness) {
			v += s + " ";
		}
		return v.trim();
	}

//	public void play(Robotis robotis) {
//		Util.info("Page", toString());
//		int frame = 0;
//		for(Step step : steps) {
//			step.play(robotis, frame);
//			frame = step.frame;
//		}
//	}

	public static Page parse(Node node) {
		Page page= new Page();
		NamedNodeMap attrs = node.getAttributes();
		page.name = Util.parseString(attrs, "name");
		page.steps = new ArrayList<>();
		node = Util.next(node.getFirstChild(), "param");
		attrs = node.getAttributes();
		page.compileSize = Util.parseInt(attrs, "compileSize");
		page.acceleration = Util.parseInt(attrs, "acceleration");
		String[] v = Util.parseString(attrs, "softness").split(" ");
		page.softness = new int[v.length];
		for(int i=0; i<v.length; i++) {
			page.softness[i] = Integer.parseInt(v[i].trim());
		}
		return page;
	}
}

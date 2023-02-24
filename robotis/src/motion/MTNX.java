package motion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import robotis.Robotis;
import util.Util;

public class MTNX  {
	public Map<String, Flow> flows = new HashMap<>();
	public List<Flow> i2flow = new ArrayList<>();
	public Map<String, Page> pages = new HashMap<>();
	public List<Page> i2page = new ArrayList<>();
	public List<Page> m2page = new ArrayList<>();
	public Map<String, Bucket> buckets = new HashMap<>();
	public List<Bucket> i2bucket = new ArrayList<>();
	
	public MTNX(InputStream is) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);

			i2flow.add(null);
			i2page.add(null);
			m2page.add(null);
			i2bucket.add(null);

			NodeList list = document.getElementsByTagName("FlowRoot");
			Node node = list.item(0).getFirstChild();
			while(node != null) {
				if(node.getNodeName().equalsIgnoreCase("flow")) {
					Flow flow = Flow.parse(node);
					flow.mtnx = this;
					flows.put(flow.name, flow);
					i2flow.add(flow);
					Node child = Util.next(node.getFirstChild(), "units");
					child = child.getFirstChild();
					while(child != null) {
						if(child.getNodeName().equalsIgnoreCase("unit")) {
							Unit unit = Unit.parse(child);
							flow.units.add(unit);
							unit.flow = flow;

						}
						child = child.getNextSibling();
					}
				}
				node = node.getNextSibling();
			}

			list = document.getElementsByTagName("PageRoot");
			node = list.item(0).getFirstChild();
			while(node != null) {
				if(node.getNodeName().equalsIgnoreCase("page")) {
					Page page = Page.parse(node);
					page.mtnx = this;
					pages.put(page.name, page);
					i2page.add(page);
					Node child = Util.next(node.getFirstChild(), "steps");
					child = child.getFirstChild();
					while(child != null) {
						if(child.getNodeName().equalsIgnoreCase("step")) {
							Step step = Step.parse(child);
							page.steps.add(step);
							step.page = page;
						}
						child = child.getNextSibling();
					}
				}
				node = node.getNextSibling();
			}

			list = document.getElementsByTagName("BucketRoot");
			node = list.item(0).getFirstChild();
			while(node != null) {
				if(node.getNodeName().equalsIgnoreCase("Bucket")) {
					Bucket bucket = Bucket.parse(node);
					bucket.mtnx = this;
					buckets.put(bucket.name, bucket);
					i2bucket.add(bucket);
					Node child = Util.next(node.getFirstChild(), "callFlows");
					child = child.getFirstChild();
					while(child != null) {
						if(child.getNodeName().equalsIgnoreCase("callFlow")) {
							CallFlow callflow = CallFlow.parse(child);
							callflow.bucket = bucket;
							bucket.callflows.put(callflow.flow, callflow);
							bucket.i2callflow.put(callflow.callIndex, callflow);
						
						}
						child = child.getNextSibling();
					}
				}
				node = node.getNextSibling();
			}
			{
				List<String> kf = new ArrayList<>();
				for(String kk : flows.keySet())
					kf.add(kk);
				Bucket bucket = i2bucket.get(1);
				for(int kc=1; kc<=bucket.i2callflow.size(); kc++) {
					CallFlow callflow = bucket.i2callflow.get(kc);
					Flow flow= flows.get(callflow.flow);
					Unit unit = flow.units.get(0);
					m2page.add(pages.get(unit.main));
					kf.remove(callflow.flow);
				}
				m2page.add(null);
				m2page.add(null);
				m2page.add(null);
				for(int kc=1; kc<=bucket.i2callflow.size(); kc++) {
					CallFlow callflow = bucket.i2callflow.get(kc);
					Flow flow= flows.get(callflow.flow);
					for(int u=1; u<flow.units.size(); u++) {
						Unit unit = flow.units.get(u);
						m2page.add(pages.get(unit.main));
					}
				}
			}
		} catch (Exception e) {
			Robotis.instance.error("MTNX", e);
		}
	}
}

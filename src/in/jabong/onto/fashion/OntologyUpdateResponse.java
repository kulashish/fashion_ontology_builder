package in.jabong.onto.fashion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class OntologyUpdateResponse {

	private Map<OntologyNode, OntologyNodes> ontoRootInstancesMap;

	private Map<OntologyNode, OntologyNodes> getOntoRootInstancesMap() {
		if (ontoRootInstancesMap == null)
			ontoRootInstancesMap = new HashMap<OntologyNode, OntologyNodes>();
		return ontoRootInstancesMap;
	}

	public void addResponse(OntologyNode rootNode, OntologyNodes instanceNodes) {
		getOntoRootInstancesMap().put(rootNode, instanceNodes);
	}

	public OntologyNodes getResponse(OntologyNode node) {
		return ontoRootInstancesMap.get(node);
	}

	public void addResponseEntry(OntologyNode rNode, String str) {
		OntologyNodes nodes = getOntoRootInstancesMap().get(rNode);
		if (nodes == null) {
			nodes = new OntologyNodes();
			ontoRootInstancesMap.put(rNode, nodes);
		}
		nodes.addNode(new OntologyNode(str));
	}

	public void printRoots() {
		Iterator<OntologyNode> rootNodes = ontoRootInstancesMap.keySet().iterator();
		while (rootNodes.hasNext())
			System.out.println(rootNodes.next());
	}

	public void printAll() {
		Iterator<Entry<OntologyNode, OntologyNodes>> entriesIter = ontoRootInstancesMap.entrySet().iterator();
		Entry<OntologyNode, OntologyNodes> entry = null;
		while (entriesIter.hasNext()) {
			entry = entriesIter.next();
			System.out.println("KEY NODE : " + entry.getKey());
			System.out.println("VALUE NODES : " + entry.getValue());
		}
	}

	public String[] getSubClasses() {
		String[] subClasses = null;
		Iterator<OntologyNode> keys = ontoRootInstancesMap.keySet().iterator();
		if (ontoRootInstancesMap.size() == 1)
			subClasses = ontoRootInstancesMap.get(keys.next()).asStringArray();
		else {
			subClasses = new String[ontoRootInstancesMap.keySet().size()];
			for (int index = 0; index < subClasses.length; index++)
				subClasses[index] = keys.next().getNodeLabel();
		}
		System.out.println("Received subclasses: ");
		for (String sub : subClasses)
			System.out.println(sub);
		return subClasses;
	}

}

package in.jabong.onto.fashion;

import java.util.ArrayList;
import java.util.List;

public class OntologyNodes {

	private List<OntologyNode> ontoNodes;

	public void addNode(OntologyNode node) {
		if (ontoNodes == null)
			ontoNodes = new ArrayList<OntologyNode>();
		ontoNodes.add(node);
	}

	public int getSize() {
		return ontoNodes.size();
	}

	public OntologyNode getNode(int index) {
		return getSize() > 0 ? ontoNodes.get(index) : null;
	}

	@Override
	public String toString() {
		return "OntologyNodes [ontoNodes=" + ontoNodes + "]";
	}

	public String[] asStringArray() {
		String[] strArray = null;
		if (ontoNodes != null) {
			strArray = new String[getSize()];
			for (int index = 0; index < getSize(); index++)
				strArray[index] = getNode(index).getNodeLabel();
		}
		return strArray;
	}

}

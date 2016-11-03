package in.jabong.onto.fashion;

public class OntologyNode {

	private String nodeLabel;

	public OntologyNode(String name) {
		nodeLabel = name;
	}

	public String getNodeLabel() {
		return nodeLabel;
	}

	public void setNodeLabel(String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}

	@Override
	public String toString() {
		return "OntologyNode [nodeLabel=" + nodeLabel + "]";
	}

}

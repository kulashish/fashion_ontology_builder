package in.jabong.onto.json;

public class NodeJSON {
	private String nodeName;
	private NodeTypeJSON nodeType;
	private boolean blnRootNode;
	private NodeJSON nextNode;

	public NodeJSON(String token) {
		int arrIndex = token.indexOf("[]");
		nodeName = arrIndex == -1 ? token : token.substring(0, arrIndex);
		nodeType = arrIndex == -1 ? NodeTypeJSON.OBJ : NodeTypeJSON.ARR;
		blnRootNode = token.startsWith("$");
		nodeName = blnRootNode ? nodeName.substring(1) : nodeName; // Remove '$'
																	// from the
																	// node name
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public NodeTypeJSON getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeTypeJSON nodeType) {
		this.nodeType = nodeType;
	}

	public NodeJSON getNextNode() {
		return nextNode;
	}

	public void setNextNode(NodeJSON nextNode) {
		this.nextNode = nextNode;
	}

	public boolean isBlnRootNode() {
		return blnRootNode;
	}

	public void setBlnRootNode(boolean blnRootNode) {
		this.blnRootNode = blnRootNode;
	}

	@Override
	public boolean equals(Object obj) {
		return this.nodeName.equals(((NodeJSON) obj).getNodeName());
	}

	public NodeJSON findNode(NodeJSON nodeToFind) {
		NodeJSON foundNode = null;
		NodeJSON currentNode = this;
		do {
			if (currentNode.equals(nodeToFind)) {
				foundNode = currentNode;
				break;
			} else
				currentNode = currentNode.nextNode;
		} while (currentNode != null);
		return foundNode;
	}

}

package in.jabong.onto.json;

import in.jabong.onto.fashion.OntologyUpdateRequest;

public class OntologyUpdateRequestJSON extends OntologyUpdateRequest {

	private String jsonFilePath;
	private String jsonRootPath;
	private String jsonInstPath;

	private NodeJSON jsonRootNode;
	private NodeJSON jsonInstNode;

	@Override
	public void parse(String[] reqParams) {
		for (int i = 0; i < reqParams.length; i++) {
			switch (reqParams[i]) {
			case "-f":
				jsonFilePath = reqParams[++i];
				break;
			case "-rp":
				jsonRootPath = reqParams[++i];
				jsonRootNode = buildJsonPath(jsonRootPath);
				break;
			case "-ip":
				jsonInstPath = reqParams[++i];
				jsonInstNode = buildJsonPath(jsonInstPath);
				break;
			}
		}
	}

	public String getJsonFilePath() {
		return jsonFilePath;
	}

	public String getJsonRootPath() {
		return jsonRootPath;
	}

	public NodeJSON getJsonRootNode() {
		return jsonRootNode;
	}

	public NodeJSON getJsonInstNode() {
		return jsonInstNode;
	}

	public String getJsonInstPath() {
		return jsonInstPath;
	}

	public void setJsonInstPath(String jsonInstPath) {
		this.jsonInstPath = jsonInstPath;
	}

	public NodeJSON buildJsonPath(String path) {
		NodeJSON currentNode = null;
		NodeJSON prevNode = null;
		NodeJSON rootNode = null;
		for (String token : path.split(":")) {
			currentNode = new NodeJSON(token);
			if (rootNode == null)
				rootNode = currentNode;
			if (prevNode != null)
				prevNode.setNextNode(currentNode);
			prevNode = currentNode;
		}
		return rootNode;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonFilePath = jsonPath;
	}

	public void setJsonRootNode(NodeJSON jsonRootNode) {
		this.jsonRootNode = jsonRootNode;
	}

	public void setJsonRootPath(String jsonRootPath) {
		this.jsonRootPath = jsonRootPath;
	}

	public void setJsonInstNode(NodeJSON jsonInstNode) {
		this.jsonInstNode = jsonInstNode;
	}

}

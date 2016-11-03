package in.jabong.onto.fashion;

import in.jabong.onto.csv.OntologyUpdateRequestCSV;
import in.jabong.onto.dbpedia.OntologyUpdateRequestDBP;
import in.jabong.onto.json.OntologyUpdateRequestJSON;

public abstract class OntologyUpdateRequest {
	protected OntologyUpdateRequestType requestType;
	protected OntologyUpdateSource updateSource;
	protected String rootNode;

	public OntologyUpdateRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(OntologyUpdateRequestType requestType) {
		System.out.println("Setting request type: " + requestType);
		this.requestType = requestType;
	}

	public OntologyUpdateSource getUpdateSource() {
		return updateSource;
	}

	public void setUpdateSource(OntologyUpdateSource updateSource) {
		this.updateSource = updateSource;
	}

	public String getRootNode() {
		return rootNode;
	}

	public void setRootNode(String rootNode) {
		System.out.println("Setting root node: " + rootNode);
		this.rootNode = rootNode;
	}

	public static OntologyUpdateRequest createInstance(OntologyUpdateSource source) {
		OntologyUpdateRequest reqInstance = null;
		System.out.println("Source: " + source);
		switch (source) {
		case DBP:
			reqInstance = new OntologyUpdateRequestDBP();
			break;
		case CSV:
			reqInstance = new OntologyUpdateRequestCSV();
			break;
		case JSON:
			reqInstance = new OntologyUpdateRequestJSON();
			break;
		}
		return reqInstance;
	}

	public abstract void parse(String[] reqParams);

}

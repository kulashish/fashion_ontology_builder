package in.jabong.onto.json;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import in.jabong.onto.dbpedia.OntologyRequestHandlerException;
import in.jabong.onto.fashion.OntologyNode;
import in.jabong.onto.fashion.OntologyRequestHandler;
import in.jabong.onto.fashion.OntologyUpdateRequest;
import in.jabong.onto.fashion.OntologyUpdateResponse;
import in.jabong.onto.fashion.OntologyUpdater;

public class OntologyRequestHandlerJSON extends OntologyRequestHandler {
	private OntologyUpdateRequestJSON jsonRequest;

	public OntologyRequestHandlerJSON(OntologyUpdateRequest request, OntologyUpdater updater) {
		super(request, updater);
		jsonRequest = (OntologyUpdateRequestJSON) request;
	}

	@Override
	public void handleRequest() throws OntologyRequestHandlerException {
		JSONParser jsonParser = new JSONParser();
		try {
			Object parseObj = jsonParser.parse(new FileReader(jsonRequest.getJsonFilePath()));
			response = new OntologyUpdateResponse();
			if (jsonRequest.getJsonRootNode() != null)
				browseJson(parseObj, jsonRequest.getJsonRootNode());
			else
				browseInstJson(new OntologyNode("root"), parseObj, jsonRequest.getJsonInstNode());
			forwardRequest(response);
		} catch (IOException | ParseException e) {
			throw new OntologyRequestHandlerException(e);
		}

	}

	private String browseJson(Object parseObj, NodeJSON jsonNode) {
		String val = null;
		if (jsonNode == null) {
			// resultInstances.add((String) parseObj);
			val = (String) parseObj;
		} else {
			JSONObject jsonObj = (JSONObject) parseObj;
			Object obj = jsonObj.get(jsonNode.getNodeName());
			if (jsonNode.getNodeType() == NodeTypeJSON.ARR) {
				for (Object o : (JSONArray) obj) {
					val = browseJson(o, jsonNode.getNextNode());
					if (jsonNode.isBlnRootNode()) {
						OntologyNode rNode = new OntologyNode(val);
						response.addResponse(rNode, null);
						if (jsonRequest.getJsonInstNode() != null)
							browseInstJson(rNode, o, jsonRequest.getJsonInstNode().findNode(jsonNode).getNextNode());
					}
				}
			} else
				val = browseJson(obj, jsonNode.getNextNode());
		}
		return val;
	}

	private void browseInstJson(OntologyNode rNode, Object o, NodeJSON jsonNode) {
		if (jsonNode == null) {
			response.addResponseEntry(rNode, (String) o);
			return;
		}
		JSONObject jsonObj = (JSONObject) o;
		Object obj = jsonObj.get(jsonNode.getNodeName());
		if (obj != null)
			if (jsonNode.getNodeType() == NodeTypeJSON.ARR)
				for (Object arrObj : (JSONArray) obj)
					browseInstJson(rNode, arrObj, jsonNode.getNextNode());
			else
				browseInstJson(rNode, obj, jsonNode.getNextNode());
	}

	public static void main(String[] args) {
		OntologyUpdateRequestJSON request = new OntologyUpdateRequestJSON();
		request.setJsonPath(
				"/Users/ashish/Documents/workspace/eclipse/fashion_ontology/fashiondictionary/attributes_shoes.json");
		request.setJsonRootPath("data:$attributes[]:label");
		request.setJsonRootNode(request.buildJsonPath(request.getJsonRootPath()));
		request.setJsonInstPath("data:$attributes[]:options[]:name");
		request.setJsonInstNode(request.buildJsonPath(request.getJsonInstPath()));
		OntologyRequestHandlerJSON handler = new OntologyRequestHandlerJSON(request, null);
		try {
			handler.handleRequest();
		} catch (OntologyRequestHandlerException e) {
			e.printStackTrace();
		}
		handler.response.printAll();
	}

}

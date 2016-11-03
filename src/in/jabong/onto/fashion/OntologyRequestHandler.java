package in.jabong.onto.fashion;

import in.jabong.onto.csv.OntologyRequestHandlerCSV;
import in.jabong.onto.dbpedia.OntologyRequestHandlerDBP;
import in.jabong.onto.dbpedia.OntologyRequestHandlerException;
import in.jabong.onto.dbpedia.OntologyUpdateException;
import in.jabong.onto.json.OntologyRequestHandlerJSON;

public abstract class OntologyRequestHandler {

	protected OntologyUpdateRequest request;
	protected OntologyUpdater ontoUpdater;
	protected OntologyUpdateResponse response;

	public OntologyRequestHandler(OntologyUpdateRequest request, OntologyUpdater updater) {
		this.request = request;
		this.ontoUpdater = updater;
	}

	public static OntologyRequestHandler createHandler(OntologyUpdateRequest request, OntologyUpdater updater) {
		OntologyRequestHandler handler = null;
		System.out.println("Creating handler for request source: " + request.getUpdateSource());
		switch (request.getUpdateSource()) {
		case DBP:
			handler = new OntologyRequestHandlerDBP(request, updater);
			break;
		case CSV:
			handler = new OntologyRequestHandlerCSV(request, updater);
			break;
		case JSON:
			handler = new OntologyRequestHandlerJSON(request, updater);
			break;
		}
		// if (request.getUpdateSource() == OntologyUpdateSource.DBP)
		// handler = new OntologyRequestHandlerDBP(request, updater);
		// else if (request.getUpdateSource() == OntologyUpdateSource.CSV)
		// handler = new OntologyRequestHandlerCSV(request, updater);
		return handler;
	}

	public abstract void handleRequest() throws OntologyRequestHandlerException;

	protected void forwardRequest(String[] instances) throws OntologyRequestHandlerException {
		System.out.println("Request type: " + request.getRequestType());
		switch (request.getRequestType()) {
		case SUB:
			ontoUpdater.addHierarchy(request.getRootNode(), instances);
			break;
		case INST:
			ontoUpdater.addInstances(request.getRootNode(), instances);
			break;
		}
		try {
			ontoUpdater.serializeModel();
		} catch (OntologyUpdateException e) {
			throw new OntologyRequestHandlerException(e);
		}
	}

	protected void forwardRequest(OntologyUpdateResponse response) throws OntologyRequestHandlerException {
		forwardRequest(response.getSubClasses());
	}

}

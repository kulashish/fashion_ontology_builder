package in.jabong.onto.csv;

import java.io.IOException;

import in.jabong.onto.dbpedia.OntologyRequestHandlerException;
import in.jabong.onto.fashion.OntologyRequestHandler;
import in.jabong.onto.fashion.OntologyUpdateRequest;
import in.jabong.onto.fashion.OntologyUpdater;
import in.jabong.onto.util.CSVUtil;

public class OntologyRequestHandlerCSV extends OntologyRequestHandler {

	public OntologyRequestHandlerCSV(OntologyUpdateRequest request, OntologyUpdater updater) {
		super(request, updater);
	}

	@Override
	public void handleRequest() throws OntologyRequestHandlerException {
		System.out.println("handleRequest in OntologyRequestHandlerCSV");
		OntologyUpdateRequestCSV csvRequest = (OntologyUpdateRequestCSV) request;
		String[] instances = null;
		try {
			CSVUtil csvUtil = new CSVUtil(csvRequest.getCsvPath());
			instances = csvUtil.getColumn(csvRequest.getColumnIndex());
			System.out.println("Received instances from CSV " + instances.length);
		} catch (IOException e) {
			throw new OntologyRequestHandlerException(e);
		}
		forwardRequest(instances);
	}

}

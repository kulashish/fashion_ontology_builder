package in.jabong.onto.dbpedia;

import in.jabong.onto.fashion.OntologyUpdateRequest;

public class OntologyUpdateRequestDBP extends OntologyUpdateRequest {

	private QueryType qType;

	@Override
	public void parse(String[] reqParams) {
		for (int pIndex = 0; pIndex < reqParams.length; pIndex++) {
			switch (reqParams[pIndex]) {
			case "-q":
				qType = QueryType.valueOf(reqParams[++pIndex]);
				break;
			}
		}
	}

	public QueryType getqType() {
		return qType;
	}

}

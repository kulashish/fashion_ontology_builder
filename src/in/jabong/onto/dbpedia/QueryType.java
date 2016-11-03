package in.jabong.onto.dbpedia;

public enum QueryType {
	SKOS_BROADER("broad"), DCT_SUBJECT("subj");

	private String typeValue;

	private QueryType(String val) {
		typeValue = val;
	}
}

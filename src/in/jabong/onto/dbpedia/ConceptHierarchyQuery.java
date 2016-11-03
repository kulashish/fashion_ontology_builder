package in.jabong.onto.dbpedia;

public abstract class ConceptHierarchyQuery {

	protected String concept;

	public ConceptHierarchyQuery(String conceptName) {
		concept = conceptName;
	}

	public abstract String buildQuery();

}

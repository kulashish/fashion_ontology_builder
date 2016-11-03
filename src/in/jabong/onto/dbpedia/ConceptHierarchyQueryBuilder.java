package in.jabong.onto.dbpedia;

public class ConceptHierarchyQueryBuilder {

	private static ConceptHierarchyQueryBuilder instance = new ConceptHierarchyQueryBuilder();

	private ConceptHierarchyQueryBuilder() {

	}

	public static ConceptHierarchyQueryBuilder getInstance() {
		return instance;
	}

	public ConceptHierarchyQuery createQueryInstance(QueryType type, String conceptName) {
		ConceptHierarchyQuery query = null;
		switch (type) {
		case SKOS_BROADER:
			query = new SkosBroaderQuery(conceptName);
			break;
		case DCT_SUBJECT:
			query = new DctSubjectQuery(conceptName);
			break;
		}
		return query;
	}
}

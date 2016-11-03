package in.jabong.onto.dbpedia;

public class SkosBroaderQuery extends ConceptHierarchyQuery {

	public SkosBroaderQuery(String conceptName) {
		super(conceptName);
	}

	@Override
	public String buildQuery() {
		String query = DBPediaConstants.PREFIX_SKOS + DBPediaConstants.PREFIX_DBC + DBPediaConstants.PREFIX_RDFS
				+ DBPediaConstants.PREFIX_RDF;
		query += "SELECT ?sub WHERE {" + "?super rdf:type skos:Concept ." + "?super rdfs:label " + "'" + concept + "'"
				+ "@en ." + "?sub skos:broader ?super ." + "}";
		return query;
	}

}

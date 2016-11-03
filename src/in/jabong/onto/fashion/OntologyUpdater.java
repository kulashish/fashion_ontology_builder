package in.jabong.onto.fashion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import in.jabong.onto.dbpedia.ConceptHierarchyQueryBuilder;
import in.jabong.onto.dbpedia.DBPediaConstants;
import in.jabong.onto.dbpedia.OntologyRequestHandlerException;
import in.jabong.onto.dbpedia.OntologyUpdateException;
import in.jabong.onto.dbpedia.QueryType;
import in.jabong.onto.util.OntologyUtil;

public class OntologyUpdater {

	private String ontoFile;
	private String ontoIRI;
	private OntModel ontoModel;
	private String ontoOutFile;

	public OntologyUpdater(String sourceFilePath, String outFilePath, String iri) throws OntologyUpdateException {
		ontoFile = sourceFilePath;
		ontoIRI = iri;
		ontoOutFile = outFilePath;
		try {
			ontoModel = readOnto(ontoFile);
		} catch (FileNotFoundException e) {
			throw new OntologyUpdateException(e);
		}
	}

	private OntModel readOnto(String filePath) throws FileNotFoundException {
		OntModel oModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		oModel.getDocumentManager().addAltEntry("http://www.semanticweb.org/jabong/ontologies/fashion/v1",
				"file:" + OntologyConstants.ONTO_FILE);
		InputStream ontoStream = new FileInputStream(filePath);
		oModel.read(ontoStream, null);
		return oModel;
	}

	private ResultSet queryDBPediaConceptHierarchy(QueryType qType, String conceptLabel) {
		String queryString = ConceptHierarchyQueryBuilder.getInstance().createQueryInstance(qType, conceptLabel)
				.buildQuery();
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qExec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		ResultSet rSet = null;
		try {
			rSet = qExec.execSelect();
			rSet = ResultSetFactory.copyResults(rSet);
		} finally {
			qExec.close();
		}
		return rSet;
	}

	public void addHierarchy(String subRoot, ResultSet subClasses) {
		OntClass subRootClass = ontoModel.getOntClass(subRoot);
		System.out.println(ontoIRI + subRoot);
		for (; subClasses.hasNext();) {
			QuerySolution s = subClasses.nextSolution();
			Resource r = s.getResource("sub");
			String uri = r.getURI();
			OntClass subClass = ontoModel.createClass(uri);
			String label = r.getLocalName();
			if (label == null || label.equals(""))
				label = OntologyUtil.createLabel(uri);
			subClass.addLabel(label, "en");
			subRootClass.addSubClass(subClass);
		}
	}

	public void addHierarchy(String subRoot, String[] subClasses) {
		OntClass subRootClass = ontoModel.getOntClass(subRoot);
		System.out.println("Found root class: " + subRootClass);
		for (String subClass : subClasses) {
			String label = OntologyUtil.createLabelFromName(subClass);
			OntClass oClass = ontoModel.createClass(ontoIRI + label);
			oClass.addLabel(label, "en");
			subRootClass.addSubClass(oClass);
		}
	}

	public void addInstances(String root, String[] instances) {
		System.out.println("Creating instances for root node: " + root);
		OntClass rootClass = ontoModel.getOntClass(root);

		OntModel importedModel = null;
		if (null == rootClass) {
			importedModel = ontoModel.getImportedModel("http://www.semanticweb.org/jabong/ontologies/fashion/v1");
			// Iterator<OntClass> iter = importedModel.listClasses();
			// while (iter.hasNext())
			// System.out.println(iter.next());
			rootClass = importedModel.getOntClass(root);
		}
		System.out.println("Root class: " + rootClass);
		String rootClassNS = OntologyUtil.getNamespace(rootClass.getURI());
		System.out.println("Root class NS: " + rootClassNS);
		for (String instance : instances) {
			System.out.println("Creating instance: " + instance);
			Individual i = rootClass.createIndividual(rootClassNS + instance);
			i.addLabel(instance, "en");
//			System.out.println("Created instance: " + i.getLocalName());
		}
	}

	public void addInstances(String root, ResultSet instances) {
		OntClass rootClass = ontoModel.getOntClass(root);
		for (; instances.hasNext();) {
			QuerySolution s = instances.nextSolution();
			Resource r = s.getResource("sub");
			String uri = r.getURI();
			Individual i = rootClass.createIndividual(uri);
			String label = r.getLocalName();
			if (label == null || label.equals(""))
				label = OntologyUtil.createLabel(uri);
			i.addLabel(label, "en");
		}
	}

	public void serializeModel() throws OntologyUpdateException {
		try {
			ontoModel.write(new FileOutputStream(ontoOutFile));
		} catch (FileNotFoundException e) {
			throw new OntologyUpdateException(e);
		}
	}

	/*
	 * Usage: OntologyUpdater -s <DBP/CSV/JSON> -t <SUB/INST> -n <root-node>
	 * [source-specific-options]
	 */
	public static void main(String[] args) {
		OntologyUpdater ontoUpdater = null;
		try {
			ontoUpdater = new OntologyUpdater(OntologyConstants.RDF_FILE, OntologyConstants.RDF_OUT,
					OntologyConstants.ONTO_IRI);
		} catch (OntologyUpdateException e) {
			e.printStackTrace();
		}
		// OntModel model = ontoUpdater.readOnto(OntologyConstants.ONTO_FILE);
		// String ontoIRI = OntologyConstants.ONTO_IRI;
		String ns = DBPediaConstants.DBC;

		OntologyUpdateSource updateSource = null;
		OntologyUpdateRequest updateRequest = null;
		for (int argIndex = 0; argIndex < 6; argIndex++) {
			System.out.println("Received argument " + args[argIndex]);
			switch (args[argIndex]) {
			case "-s":
				updateSource = OntologyUpdateSource.valueOf(args[++argIndex]);
				updateRequest = OntologyUpdateRequest.createInstance(updateSource);
				updateRequest.setUpdateSource(updateSource);
				break;
			case "-t":
				updateRequest.setRequestType(OntologyUpdateRequestType.valueOf(args[++argIndex]));
				break;
			case "-n":
				updateRequest.setRootNode(args[++argIndex]);
				break;
			}
		}
		updateRequest.parse(Arrays.copyOfRange(args, 6, args.length));
		OntologyRequestHandler requestHandler = OntologyRequestHandler.createHandler(updateRequest, ontoUpdater);
		System.out.println("Created request handler" + requestHandler);
		try {
			requestHandler.handleRequest();
		} catch (OntologyRequestHandlerException e) {
			e.printStackTrace();
		}

		// String subRootNode = "Indoor sports";
		// ResultSet results =
		// ontoUpdater.queryDBPediaConceptHierarchy(QueryType.SKOS_BROADER,
		// subRootNode);
		// // ResultSet results =
		// // ontoUpdater.queryDBPediaConceptHierarchy(QueryType.DCT_SUBJECT,
		// // subRootNode);
		// // ontoUpdater.addHierarchy(model, ns, subRootNode.replace(' ', '_'),
		// // results);
		// // ontoUpdater.addHierarchy(model, ontoIRI, "Sports", results);
		// ontoUpdater.addInstances(model, ns, subRootNode.replace(' ', '_'),
		// results);
		// // ResultSetFormatter.out(System.out, results);
		// // while (results.hasNext()) {
		// // QuerySolution s = results.nextSolution();
		// //
		// // Resource r = s.getResource("sub");
		// // System.out.println(r.getLocalName());
		// // }
		// try {
		// model.write(new FileOutputStream(OntologyConstants.ONTO_OUT));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}
}

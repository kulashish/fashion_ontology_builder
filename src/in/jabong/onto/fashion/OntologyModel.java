package in.jabong.onto.fashion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public class OntologyModel {

	private String iri;
	private OntModel model;

	public OntologyModel(String iri) {
		this.iri = iri;
	}

	public void loadFromFile(String file) throws FileNotFoundException {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		InputStream ontoStream = new FileInputStream(file);
		model.read(ontoStream, null);
	}

	public List<String> getAllLeafNodes(String parentNode) {
		List<String> leafNodes = new ArrayList<String>();
		OntClass parentClaz = model.getOntClass(iri + parentNode);
		Stack<OntClass> nodes = new Stack<OntClass>();
		nodes.push(parentClaz);
		while (!nodes.empty()) {
			OntClass node = nodes.pop();
			if (node.hasSubClass()) {
				Iterator<OntClass> subIter = node.listSubClasses();
				while (subIter.hasNext())
					nodes.push(subIter.next());
			} else
				leafNodes.add(node.getLabel("en") != null ? node.getLabel("en") : node.getLocalName());
		}
		return leafNodes;
	}

	public static void main(String[] args) {
		OntologyModel oModel = new OntologyModel(OntologyConstants.ONTO_IRI);
		try {
			oModel.loadFromFile(OntologyConstants.ONTO_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (String node : oModel.getAllLeafNodes("Fashion_and_lifestyle_product"))
			System.out.println(node);
	}
}

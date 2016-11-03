package in.jabong.onto.util;

public class OntologyUtil {

	public static String createLabel(String uri) {
		int colonIndex = uri.lastIndexOf(':');
		int slashIndex = uri.lastIndexOf('/');
		int index = (colonIndex > slashIndex ? colonIndex : slashIndex) + 1;
		return uri.substring(index);
	}

	public static String getNamespace(String uri) {
		int hashIndex = uri.lastIndexOf('#');
		return uri.substring(0, hashIndex + 1);
	}

	public static String createLabelFromName(String name) {
		return name.replaceAll(" ", "_");
	}

}

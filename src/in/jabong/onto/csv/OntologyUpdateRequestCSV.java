package in.jabong.onto.csv;

import in.jabong.onto.fashion.OntologyUpdateRequest;

public class OntologyUpdateRequestCSV extends OntologyUpdateRequest {

	private String csvPath;
	private int columnIndex;

	@Override
	public void parse(String[] reqParams) {
		for (int pIndex = 0; pIndex < reqParams.length; pIndex++) {
			switch (reqParams[pIndex]) {
			case "-f":
				csvPath = reqParams[++pIndex];
				System.out.println("Set CSV Path " + csvPath);
				break;
			case "-i":
				columnIndex = Integer.parseInt(reqParams[++pIndex]);
				System.out.println("Received column index : " + columnIndex);
				break;
			}
		}
	}

	public String getCsvPath() {
		return csvPath;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

}

package in.jabong.onto.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

public class CSVUtil {

	private File csvFile;
	private CSVReader csvReader;
	private List<String[]> rows;

	public CSVUtil(String file) throws IOException {
		csvFile = new File(file);
		csvReader = new CSVReader(new FileReader(csvFile));
		readAllData();
	}

	public CSVUtil(String file, char separator) throws IOException {
		csvFile = new File(file);
		csvReader = new CSVReader(new FileReader(csvFile), separator);
		readAllData();
	}

	private void readAllData() throws IOException {
		rows = csvReader.readAll();
	}

	public String[] getColumn(int colNumber) {
		String[] columnValues = new String[rows.size()];
		for (int i = 0; i < rows.size(); i++)
			columnValues[i] = rows.get(i)[colNumber];
		return columnValues;
	}

}

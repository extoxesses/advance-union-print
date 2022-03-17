package aup.models.data;

import aup.interfaces.IKeyExtractor;
import aup.interfaces.IRawDataSource;
import aup.models.extractors.SimpleKeysExtractor;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.collections4.map.HashedMap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvRawData implements IRawDataSource {

	private static final String CSV_FORMAT_MSG = "Invalid csv format: CSV must have an header and at least one row";

	private IKeyExtractor extractor;

	private List<Map<String, String>> data;

	public CsvRawData() {
		super();
		this.data = new ArrayList<>();
		this.extractor = new SimpleKeysExtractor();
	}

	@Override
	public List<Map<String, String>> getData() {
		return new ArrayList<>(data);
	}

	@Override
	public void setKeysExtractor(IKeyExtractor extractor) {
		this.extractor = extractor;
	}

	public void loadFromFile(String filePath, Charset encoding) throws IOException {
		CsvReader csvReader = new CsvReader();
		csvReader.setSkipEmptyRows(true);
		CsvContainer csv = csvReader.read(new File(filePath), encoding);

		List<CsvRow> rowsList = csv.getRows();
		createData(rowsList);
	}

	/// --- Private methods ---------

	private void createData(List<CsvRow> rowsList) throws IllegalArgumentException {
		if (rowsList.size() < 2) {
			throw new IllegalArgumentException(CSV_FORMAT_MSG);
		}

		List<String> keys = rowsList.get(0).getFields().stream().map(key -> extractor.getKey(key))
				.collect(Collectors.toList());

		for (int i = 1; i < rowsList.size(); i++) {
			List<String> values = rowsList.get(i).getFields();
			Map<String, String> replacementMap = createMap(keys, values);
			data.add(replacementMap);
		}
	}

	private Map<String, String> createMap(List<String> keys, List<String> values) {
		Map<String, String> map = new HashedMap<>();
		for (int i = 0; i < keys.size(); i++) {
			map.put(keys.get(i), values.get(i));
		}
		return map;
	}
}

package aup.interfaces;

import java.util.List;
import java.util.Map;

public interface IRawDataSource {

	List<Map<String, String>> getData();
	
	void setKeysExtractor(IKeyExtractor extractor);
	
}

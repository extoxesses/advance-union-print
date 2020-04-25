package aup.interfaces;

import java.util.List;
import java.util.Map;

public interface IRawDataSource {

	public List<Map<String, String>> getData();
	
	public void setExtractor(IKeyExtractor extractor);
	
}

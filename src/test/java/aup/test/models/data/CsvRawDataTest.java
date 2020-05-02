package aup.test.models.data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.interfaces.IKeyExtractor;
import aup.models.data.CsvRawData;

public class CsvRawDataTest {

	@Test
	@DisplayName("CsvRawData - Simple test")
	public void csvTest() throws Exception {
		CsvRawData raw = new CsvRawData();
		raw.loadFromFile("src/test/resources/email/source.csv", StandardCharsets.UTF_8);
		Assertions.assertEquals(1, raw.getData().size());
	}
	
	@Test
	@DisplayName("CsvRawData - Empty source")
	public void csvEmptyTest() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			CsvRawData raw = new CsvRawData();
			raw.loadFromFile("src/test/resources/csv/empty.csv", StandardCharsets.UTF_8);
		});
	}
	
	@Test
	@DisplayName("CsvRawData - Only head source")
	public void csvOnlyHeadTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CsvRawData raw = new CsvRawData();
			raw.loadFromFile("src/test/resources/csv/only-header.csv", StandardCharsets.UTF_8);
		});
	}
	
	@Test
	@DisplayName("CsvRawData - Extractor")
	public void csvExtractorTest() throws Exception {
		CsvRawData raw = new CsvRawData();
		
		raw.setKeysExtractor(new IKeyExtractor() {	
			@Override
			public String getKey(String fieldName) throws IllegalArgumentException {
				return fieldName.concat("_test");
			}
		});
		
		raw.loadFromFile("src/test/resources/email/source.csv", StandardCharsets.UTF_8);
		List<String> keys = new ArrayList<>(raw.getData().get(0).keySet());
		
		Assertions.assertEquals("receiver_test", keys.get(0));
		Assertions.assertEquals("creator_test", keys.get(1));
		Assertions.assertEquals("name_test", keys.get(2));
	}
}

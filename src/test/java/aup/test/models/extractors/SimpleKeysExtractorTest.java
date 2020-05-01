package aup.test.models.extractors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.models.extractors.SimpleKeysExtractor;

public class SimpleKeysExtractorTest {

	@Test
	@DisplayName("SimpleKeysExtractor - Correct argument")
	public void getKeyValidTest() {
		SimpleKeysExtractor extractor = new SimpleKeysExtractor();
		Assertions.assertNotNull(extractor);
		Assertions.assertEquals("{{argument}}", extractor.getKey("argument"));
	}
	
	@Test
	@DisplayName("SimpleKeysExtractor - Invalid argument")
	public void getKeyInvalidTest() {
		SimpleKeysExtractor extractor = new SimpleKeysExtractor();
		Assertions.assertNotNull(extractor);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			extractor.getKey(null);
		});
	}
	
}

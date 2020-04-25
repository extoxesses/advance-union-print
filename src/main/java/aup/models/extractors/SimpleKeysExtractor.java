package aup.models.extractors;

import java.util.Objects;

import aup.interfaces.IKeyExtractor;

public class SimpleKeysExtractor implements IKeyExtractor {

	private static final String MODEL = "{{value}}";
	
	private static final String PLACEHOLDER = "value";
	
	public SimpleKeysExtractor() {
		super();
	}
	
	@Override
	public String getKey(String fieldName) {
		String value = Objects.nonNull(fieldName) ? fieldName : "";
		return MODEL.replace(PLACEHOLDER, value);
	}

}

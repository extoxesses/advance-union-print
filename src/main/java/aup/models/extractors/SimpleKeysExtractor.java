package aup.models.extractors;

import java.util.Objects;

import aup.interfaces.IKeyExtractor;

public class SimpleKeysExtractor implements IKeyExtractor {

	private static final String ERROR_MSG = "Invalid input argument: null";
	
	private static final String MODEL = "{{value}}";
	
	private static final String PLACEHOLDER = "value";
	
	public SimpleKeysExtractor() {
		super();
	}
	
	@Override
	public String getKey(String fieldName) throws IllegalArgumentException {
		if (Objects.isNull(fieldName)) {
			throw new IllegalArgumentException(ERROR_MSG);
		}
		
		return MODEL.replace(PLACEHOLDER, fieldName);
	}

}

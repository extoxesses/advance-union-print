package aup.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aup.constants.LibConstants;

public class TestUtils {

	private TestUtils() {
		throw new UnsupportedOperationException(LibConstants.PRIVATE_CONSTRUCT_MSG);
	}
	
	public static List<Map<String, String>> getTestMap() {
		Map<String, String> map = new HashMap<>();
		map.put("{{key0}}", "value0");
		map.put("{{key1}}", "value1");
		map.put("{{key2}}", "value2");
		
		List<Map<String, String>> queue = new ArrayList<>();
		queue.add(map);
		
		return queue;
	}
	
}

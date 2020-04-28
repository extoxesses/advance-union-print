package aup.test.models.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Test;

import aup.models.messages.Email;

public class EmailTest {

	@Test
	public void createTest() {
		Email model = getModel();
		Map<String,String> map = getMap();
		Email test = (Email) model.create(map);
		
		assertEquals("Replaced subjet: SUBJECT", test.getSubject());
		assertEquals("Replaced text: TEXT", test.getMessage());
	}

	private Email getModel() {
		return null;
	}
	
	private Map<String,String> getMap() {
		Map<String,String> map = new HashedMap<>();
		return map;
	}
	
}

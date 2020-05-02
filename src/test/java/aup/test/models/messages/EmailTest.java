package aup.test.models.messages;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.interfaces.IAttachmentModel;
import aup.interfaces.IMessage;
import aup.models.messages.Email;
import aup.test.utils.TestUtils;

public class EmailTest {

	@Test
	@DisplayName("Email - Builder test")
	public void emailBuilderTest() {
		Email email = new Email();
		Assertions.assertNotNull(email);

		Assertions.assertEquals("", email.getSubject());
		Assertions.assertEquals("", email.getMessage());
		Assertions.assertEquals("", email.getReceiver());
		Assertions.assertTrue(email.getCarbonCopy().isEmpty());
		Assertions.assertTrue(email.getHiddenReceiver().isEmpty());
		Assertions.assertTrue(email.getDynamicAttachment().isEmpty());
		Assertions.assertTrue(email.getStaticAttachment().isEmpty());
	}

	@Test
	@DisplayName("Email - Create test")
	public void createTest() {
		IAttachmentModel atc = getAttachment();

		Email model = getModel(atc);

		Map<String, String> map = TestUtils.getTestMap().get(0);
		map.put("{{receiver}}", "receiver@email.com");
		map.put("{{cc_receiver}}", "cc.receiver0@email.com;cc.receiver1@email.com");
		map.put("{{hidden_receiver}}", "hidden.receiver0@email.com;hidden.receiver1@email.com");

		IMessage message = model.create(map);
		Assertions.assertNotNull(message);
		Assertions.assertEquals(Email.class, message.getClass());

		Email email = (Email) message;
		Assertions.assertEquals(getReference(atc), email);
	}

	@Test
	@DisplayName("Email - Equals test")
	public void equalsTest() {
		IAttachmentModel atc = getAttachment();
		
		Email test = new Email();
		Assertions.assertEquals(test, test);
		Assertions.assertNotEquals(test, null);
		Assertions.assertNotEquals(test, atc);
		
		Email model = getModel(atc);
		Assertions.assertNotEquals(test, model);
		
		test.setSubject("Subject: {{key0}}");
		Assertions.assertNotEquals(test, model);
		
		test.setMessage("Example message: {{key1}}");
		Assertions.assertNotEquals(test, model);
		
		test.setReceiver("{{receiver}}");
		Assertions.assertNotEquals(test, model);
		
		String[] cc = { "cc0", "cc1" };
		test.setCarbonCopy(Arrays.asList(cc));
		Assertions.assertNotEquals(test, model);
		
		String[] hh = { "hh0", "hh1", "hh2" };
		test.setHiddenReceiver(Arrays.asList(hh));
		Assertions.assertNotEquals(test, model);

		String[] statics = { "staticAtc0", "staticAtc1" };
		test.setStaticAttachment(Arrays.asList(statics));
		Assertions.assertNotEquals(test, model);
		
		IAttachmentModel[] dynamics = { atc };
		test.setDynamicAttachment(Arrays.asList(dynamics));
		Assertions.assertEquals(test, model);
	}
	
	/// --- Private methods ---------

	private static Email getModel(IAttachmentModel atc) {
		Email email = new Email();

		email.setSubject("Subject: {{key0}}");
		email.setMessage("Example message: {{key1}}");
		email.setReceiver("{{receiver}}");

		String[] cc = { "cc0", "cc1" };
		email.setCarbonCopy(Arrays.asList(cc));

		String[] hh = { "hh0", "hh1", "hh2" };
		email.setHiddenReceiver(Arrays.asList(hh));

		IAttachmentModel[] dynamics = { atc };
		email.setDynamicAttachment(Arrays.asList(dynamics));

		String[] statics = { "staticAtc0", "staticAtc1" };
		email.setStaticAttachment(Arrays.asList(statics));

		return email;
	}

	private static Email getReference(IAttachmentModel atc) {
		Email email = new Email();
		email.setSubject("Subject: value0");
		email.setMessage("Example message: value1");
		email.setReceiver("receiver@email.com");

		String[] cc = { "cc0", "cc1", "cc.receiver0@email.com", "cc.receiver1@email.com" };
		email.setCarbonCopy(Arrays.asList(cc));

		String[] hh = { "hh0", "hh1", "hh2", "hidden.receiver0@email.com", "hidden.receiver1@email.com" };
		email.setHiddenReceiver(Arrays.asList(hh));

		String[] statics = { "staticAtc0", "staticAtc1" };
		email.setStaticAttachment(Arrays.asList(statics));

		IAttachmentModel[] dynamics = { atc };
		email.setDynamicAttachment(Arrays.asList(dynamics));

		return email;
	}

	private IAttachmentModel getAttachment() {
		return new IAttachmentModel() {

			@Override
			public String write(String filePath) throws Exception {
				// Empty method
				return "";
			}

			@Override
			public IAttachmentModel replace(Map<String, String> replacementMap) {
				return this;
			}

			@Override
			public void read(String filePath) throws Exception {
				// Empty method
			}
		};
	}

}

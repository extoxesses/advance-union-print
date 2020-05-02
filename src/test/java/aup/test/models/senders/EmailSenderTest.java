package aup.test.models.senders;

import java.util.Properties;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.constants.EmailSenderConstants;
import aup.models.messages.Email;
import aup.models.senders.EmailSender;

public class EmailSenderTest {

	@Test
	@DisplayName("EmailSernder - Properties Test")
	public void propertiesTest() {
		EmailSender sender = new EmailSender("url", 1234);
		sender.setAnonymous(true);
		sender.setDebugger(true);
		sender.setProxy("host", "port", "version");
		sender.setSsl("fallback", "port");

		Properties properties = System.getProperties();
		Assertions.assertEquals("smpt", properties.getProperty("mail.transport.protocol"));
		Assertions.assertEquals("url", properties.getProperty("mail.smtp.host"));
		Assertions.assertEquals("1234", properties.getProperty("mail.smtp.port"));

		Assertions.assertEquals("false", properties.getProperty("mail.smtp.starttls.enable"));
		Assertions.assertEquals("false", properties.getProperty("mail.smtp.auth"));

		Assertions.assertEquals("true", properties.getProperty("mail.debug"));

		Assertions.assertEquals("true", properties.getProperty("proxySet"));
		Assertions.assertEquals("host", properties.getProperty("socksProxyHost"));
		Assertions.assertEquals("port", properties.getProperty("socksProxyPort"));
		Assertions.assertEquals("version", properties.getProperty("socksProxyVersion"));

		Assertions.assertEquals(EmailSenderConstants.SSL_FACTORY,
				properties.getProperty("mail.smtp.socketFactory.class"));
		Assertions.assertEquals("fallback", properties.getProperty("mail.smtp.socketFactory.fallback"));
		Assertions.assertEquals("port", properties.getProperty("mail.smtp.socketFactory.port"));

		sender.setAnonymous(false);
		Assertions.assertEquals("true", properties.getProperty("mail.smtp.starttls.enable"));
		Assertions.assertEquals("true", properties.getProperty("mail.smtp.auth"));
	}

	@Test
	@DisplayName("EmailSernder - Logout test")
	public void logoutTest() {
		EmailSender sender = new EmailSender("url", 1234);
		sender.logOut();

		Assertions.assertThrows(AddressException.class, () -> {
			sender.send(null);
		});
	}

	@Test
	@DisplayName("EmailSernder - Send test")
	public void sendTest() {
		EmailSender sender = new EmailSender("url", 1234);
		sender.authenticate("sender@email.com", "1234");
		Email email = new Email();

		Assertions.assertThrows(SendFailedException.class, () -> {
			sender.send(email);
		});
	}
}

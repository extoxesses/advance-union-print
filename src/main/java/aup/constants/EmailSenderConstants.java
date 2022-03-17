package aup.constants;

public class EmailSenderConstants {

	// --- Mail configuration

	public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	public static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";

	// --- Provides configuration

	public static final String GMAIL_SMTP = "smtp.gmail.com";

	public static final String OUTLOOK_SMTP = "smtp-mail.outlook.com";

	// --- Private methods

	private EmailSenderConstants() {
		throw new UnsupportedOperationException(LibConstants.PRIVATE_CONSTRUCT_MSG);
	}

}

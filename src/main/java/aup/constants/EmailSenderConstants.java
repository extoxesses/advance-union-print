package aup.constants;

public class EmailSenderConstants {

	public static final String SMPT_TLS_ENABLE = "mail.smtp.starttls.enable";

	public static final String SMPT_AUTH = "mail.smtp.auth";

	public static final String IMAP_TLS_ENABLE = "mail.imap.starttls.enable";

	public static final String IMAP_AUTH = "mail.imap.auth";

	public static final String POP3_TLS_ENABLE = "mail.pop3.starttls.enable";

	public static final String POP3_AUTH = "mail.pop3.auth";

	public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	private EmailSenderConstants() {
		throw new IllegalAccessError(LibConstants.PRIVATE_CONSTRUCT_MSG);
	}

}

package aup.constants;

public class EmailSenderConstants {

	public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	public static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";
	
	private EmailSenderConstants() {
		throw new UnsupportedOperationException(LibConstants.PRIVATE_CONSTRUCT_MSG);
	}

}

package aup.constants;

public class RawDataSourceConstants {

	public static final String RECEIVER_EMAIL_FIELD = "{{receiver}}";
	
	public static final String CC_EMAIL_FIELD = "{{cc_receiver}}";
	
	public static final String HIDDEN_EMAIL_FIELD = "{{hidden_receiver}}";
	
	public static final String MAIL_SPLITTER = ";";
	
	private RawDataSourceConstants () {
		throw new UnsupportedOperationException(LibConstants.PRIVATE_CONSTRUCT_MSG);
	}
}

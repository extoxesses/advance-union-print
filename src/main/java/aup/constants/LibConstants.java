package aup.constants;

public class LibConstants {

	public static final String PRIVATE_CONSTRUCT_MSG = "Unable to instantiate static class.";

	public static final String FOLDER_NOT_CREATED_MSG = "Unable to create temporary files. Check you permissions.";
	
	public static final String TMP_FOLDER = ".aup-tmp";
	
	private LibConstants() {
		throw new IllegalAccessError(PRIVATE_CONSTRUCT_MSG);
	}
	
}

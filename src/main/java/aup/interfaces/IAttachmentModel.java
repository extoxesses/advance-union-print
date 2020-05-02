package aup.interfaces;

import java.util.Map;

public interface IAttachmentModel {

	public static final String DEFAULT_VALUE = "***";
	
	public void read(String filePath) throws Exception;
	
	public String write(String filePath)  throws Exception;
		
	public IAttachmentModel replace(Map<String, String> replacementMap);

}

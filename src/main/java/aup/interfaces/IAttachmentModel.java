package aup.interfaces;

import java.util.Map;

public interface IAttachmentModel {

	public static final String DEFAULT_VALUE = "***";
	
	public void read(String filePath) throws Exception;
	
	public void write(String filePath);
		
	public IAttachmentModel replace(Map<String, String> replacementMap);
	
	public String getFileName();

}

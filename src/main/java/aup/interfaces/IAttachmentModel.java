package aup.interfaces;

import java.util.Map;

public interface IAttachmentModel {
	
	void read(String filePath) throws Exception;
	
	String write(String filePath)  throws Exception;
		
	IAttachmentModel replace(Map<String, String> replacementMap);

}

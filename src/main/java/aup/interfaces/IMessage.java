package aup.interfaces;

import java.util.List;
import java.util.Map;

public interface IMessage {
	
	List<IAttachmentModel> getDynamicAttachment();

	IMessage create(Map<String, String> replacementMap);

}

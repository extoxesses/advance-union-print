package aup.interfaces;

import java.util.List;
import java.util.Map;

public interface IMessage {
	
	public List<IAttachmentModel> getDynamicAttachment();

	public IMessage create(Map<String, String> replacementMap);

}

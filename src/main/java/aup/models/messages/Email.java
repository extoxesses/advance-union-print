package aup.models.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import aup.constants.RawDataSourceConstants;
import aup.interfaces.IAttachmentModel;
import aup.interfaces.IMessage;

public class Email implements IMessage {

	private String object;

	private String message;

	private String receiver;

	private List<String> carbonCopy;

	private List<String> hiddenReceiver;

	private List<IAttachmentModel> dynamicAttachment;

	private List<String> staticAttachment;

	/// --- Constructors ---------

	public Email() {
		this.object = ""; // TODO: gestire con gli optional
		this.message = "";
		this.receiver = "";
		this.carbonCopy = new ArrayList<>();
		this.dynamicAttachment = new ArrayList<>();
		this.staticAttachment = new ArrayList<>();
	}

	@Override
	public IMessage create(Map<String, String> replacementMap) {
		Email email = new Email();

		replacementMap.keySet().parallelStream().forEach(key -> {
			String value = replacementMap.getOrDefault(key, "");
			email.object = object.replace(key, value);
			email.message = message.replace(key, value);
		});

		String receiver = replacementMap.getOrDefault(RawDataSourceConstants.RECEIVER_EMAIL_FIELD, "");
		email.receiver = receiver;

		String[] cc = replacementMap.getOrDefault(RawDataSourceConstants.CC_EMAIL_FIELD, "")
				.split(RawDataSourceConstants.MAIL_SPLITTER);
		if (cc.length > 0 && !cc[0].equals("")) {
			email.carbonCopy.addAll(Arrays.asList(cc));
		}

		String[] hidden = replacementMap.getOrDefault(RawDataSourceConstants.HIDDEN_EMAIL_FIELD, "")
				.split(RawDataSourceConstants.MAIL_SPLITTER);
		if (hidden.length > 0 && !hidden[0].equals("")) {
			email.hiddenReceiver.addAll(Arrays.asList(hidden));
		}

		dynamicAttachment.forEach(document -> {
			IAttachmentModel parsed = document.replace(replacementMap);
			email.dynamicAttachment.add(parsed);
		});

		return email;
	}

	/// --- Getter and Setter methods ---------

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public List<String> getCarbonCopy() {
		return new ArrayList<String>(carbonCopy);
	}

	public void setCarbonCopy(List<String> carbonCopy) {
		this.carbonCopy = new ArrayList<String>(carbonCopy);
	}

	public List<String> getHiddenReceiver() {
		return new ArrayList<String>(hiddenReceiver);
	}

	public void setHiddenReceiver(List<String> hiddenReceiver) {
		this.hiddenReceiver = new ArrayList<String>(hiddenReceiver);
	}

	public List<IAttachmentModel> getDynamicAttachment() {
		return new ArrayList<IAttachmentModel>(dynamicAttachment);
	}

	public void setDynamicAttachment(List<IAttachmentModel> dynamicAttachment) {
		this.dynamicAttachment = new ArrayList<IAttachmentModel>(dynamicAttachment);
	}

	public List<String> getStaticAttachment() {
		return new ArrayList<String>(staticAttachment);
	}

	public void setStaticAttachment(List<String> staticAttachment) {
		this.staticAttachment = new ArrayList<String>(staticAttachment);
	}
}

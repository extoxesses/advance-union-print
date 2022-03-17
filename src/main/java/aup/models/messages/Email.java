package aup.models.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import aup.constants.RawDataSourceConstants;
import aup.interfaces.IAttachmentModel;
import aup.interfaces.IMessage;

public class Email implements IMessage {

	private String subject;

	private String message;

	private String receiver;

	private List<String> carbonCopy;

	private List<String> hiddenReceiver;

	private List<IAttachmentModel> dynamicAttachment;

	private List<String> staticAttachment;

	/// --- Constructors ---------

	public Email() {
		this.subject = "";
		this.message = "";
		this.receiver = "";
		this.carbonCopy = new ArrayList<>();
		this.hiddenReceiver = new ArrayList<>();
		this.dynamicAttachment = new ArrayList<>();
		this.staticAttachment = new ArrayList<>();
	}

	@Override
	public IMessage create(Map<String, String> replacementMap) {
		Email email = new Email();

		// --- Subject and text message ---------
		email.subject = this.subject;
		email.message = this.message;
		for (String key : replacementMap.keySet()) {
			String value = replacementMap.getOrDefault(key, "");
			email.subject = email.subject.replace(key, value);
			email.message = email.message.replace(key, value);
		}

		// --- Receivers ---------
		String receiver = replacementMap.getOrDefault(RawDataSourceConstants.RECEIVER_EMAIL_FIELD, "");
		email.receiver = receiver;

		email.carbonCopy.addAll(carbonCopy);
		String[] cc = replacementMap.getOrDefault(RawDataSourceConstants.CC_EMAIL_FIELD, "")
				.split(RawDataSourceConstants.MAIL_SPLITTER);
		if (cc.length > 0 && !cc[0].equals("")) {
			email.carbonCopy.addAll(Arrays.asList(cc));
		}

		email.hiddenReceiver.addAll(hiddenReceiver);
		String[] hidden = replacementMap.getOrDefault(RawDataSourceConstants.HIDDEN_EMAIL_FIELD, "")
				.split(RawDataSourceConstants.MAIL_SPLITTER);
		if (hidden.length > 0 && !hidden[0].equals("")) {
			email.hiddenReceiver.addAll(Arrays.asList(hidden));
		}

		// --- Attachments ---------
		
		email.staticAttachment.addAll(staticAttachment);
		
		dynamicAttachment.forEach(document -> {
			IAttachmentModel parsed = document.replace(replacementMap);
			email.dynamicAttachment.add(parsed);
		});

		return email;
	}

	/// --- Getter and Setter methods ---------

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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
		return new ArrayList<>(carbonCopy);
	}

	public void setCarbonCopy(List<String> carbonCopy) {
		this.carbonCopy = new ArrayList<>(carbonCopy);
	}

	public List<String> getHiddenReceiver() {
		return new ArrayList<>(hiddenReceiver);
	}

	public void setHiddenReceiver(List<String> hiddenReceiver) {
		this.hiddenReceiver = new ArrayList<>(hiddenReceiver);
	}

	public List<IAttachmentModel> getDynamicAttachment() {
		return new ArrayList<>(dynamicAttachment);
	}

	public void setDynamicAttachment(List<IAttachmentModel> dynamicAttachment) {
		this.dynamicAttachment = new ArrayList<>(dynamicAttachment);
	}

	public List<String> getStaticAttachment() {
		return new ArrayList<>(staticAttachment);
	}

	public void setStaticAttachment(List<String> staticAttachment) {
		this.staticAttachment = new ArrayList<>(staticAttachment);
	}

	/// --- Equals ---------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carbonCopy == null) ? 0 : carbonCopy.hashCode());
		result = prime * result + ((dynamicAttachment == null) ? 0 : dynamicAttachment.hashCode());
		result = prime * result + ((hiddenReceiver == null) ? 0 : hiddenReceiver.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((staticAttachment == null) ? 0 : staticAttachment.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (Objects.isNull(obj)) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Email other = (Email) obj;
		return compare(subject, other.subject)
				&& compare(message, other.message)
				&& compare(receiver, other.receiver)
				&& compare(carbonCopy, other.carbonCopy)
				&& compare(hiddenReceiver, other.hiddenReceiver)
				&& compare(staticAttachment, other.staticAttachment)
				&& compare(dynamicAttachment, other.dynamicAttachment);
	}

	/// --- Private methods ---------

	private boolean compare(Object o0, Object o1) {
		if (Objects.isNull(o0)) {
			return Objects.isNull(o1);
		} else {
			return o0.equals(o1);
		}
	}

}

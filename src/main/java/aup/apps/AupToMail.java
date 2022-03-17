package aup.apps;

import aup.interfaces.IAttachmentModel;
import aup.models.data.CsvRawData;
import aup.models.messages.Email;
import aup.models.senders.EmailSender;
import aup.modules.UnionPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AupToMail {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AupToMail.class);

	private Email emailTemplate;
	private EmailSender provider;
	private CsvRawData feeder;

	public void setDebug(boolean debug) {
		this.provider.setDebugger(debug);
	}
	
	public void setEmailTemplate(String subject, String msg, List<String> staticAttPath, List<IAttachmentModel> dynamicAtt) {
		this.emailTemplate = new Email();

		this.emailTemplate.setSubject(subject);
		this.emailTemplate.setMessage(msg);
		this.emailTemplate.setStaticAttachment(staticAttPath);
		this.emailTemplate.setDynamicAttachment(dynamicAtt);
	}

	public void setFeeder(String path) throws IOException {
		this.feeder = new CsvRawData();
		this.feeder.loadFromFile(path, StandardCharsets.UTF_8);
	}

	public void setProvider(String username, String password, String smtpUrl) {
		this.provider = new EmailSender(smtpUrl, 587);
		this.provider.authenticate(username, password);
		this.provider.setAnonymous(false);
		this.provider.setDebugger(false);
	}

	public void print() {
		new UnionPrinter(emailTemplate, provider, feeder).print();
	}

}

package aup.test.apps;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.interfaces.IAttachmentModel;
import aup.models.attachments.MsWordDoc;
import aup.models.data.CsvRawData;
import aup.models.messages.Email;
import aup.models.senders.EmailSender;
import aup.modules.UnionPrinter;

public class EmailSenderApp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderApp.class);
	
	public static final String GMAIL_SMTP = "smtp.gmail.com";
	
	public static final String OUTLOOK_SMTP = "smtp-mail.outlook.com";
	
	public static void main (String[] args) {
		try {
			EmailSender sender = createSender(args[0], args[1], true);
			Email email = createEmail();
			CsvRawData data = createSource();
			
			UnionPrinter printer = new UnionPrinter(email, sender, data);
			printer.print();
			
		} catch (Exception e) {
			LOGGER.error("{}", e.getMessage());
		}
	}
	
	/// --- Private Methods ---------
	
	private static Email createEmail() throws Exception {
		Email email = new Email();
		email.setSubject("Test object");
		email.setMessage("Ciao {{name}} questo è un esempio di replace nel testo");
		
		List<String> staticAtch = new ArrayList<>();
		staticAtch.add("src/test/resources/email/static.atc.txt");
		email.setStaticAttachment(staticAtch);
		
		List<IAttachmentModel> dynamicAtch = new ArrayList<>();
		MsWordDoc doc = new MsWordDoc();
		doc.read("src/test/resources/email/dynamic.atc.docx");
		doc.setFileName("Dynamic Attachment");
		dynamicAtch.add(doc);
		email.setDynamicAttachment(dynamicAtch);
		
		return email;
	}
	
	private static EmailSender createSender(String user, String password, boolean debug) {		
		EmailSender sender = new EmailSender(OUTLOOK_SMTP, 587);
		sender.authenticate(user, password);
		sender.setAnonymous(false);
		sender.setDebugger(debug);
		
		return sender;
	}
	
	private static CsvRawData createSource() throws Exception {
		String filePath = "src/test/resources/email/source.csv";
		CsvRawData src = new CsvRawData();
		src.loadFromFile(filePath, StandardCharsets.UTF_8);
		return src;
	}
	
}

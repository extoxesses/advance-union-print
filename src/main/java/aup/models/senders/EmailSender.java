package aup.models.senders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.constants.EmailSenderConstants;
import aup.constants.LibConstants;
import aup.interfaces.IAttachmentModel;
import aup.interfaces.IMessage;
import aup.interfaces.ISender;
import aup.models.messages.Email;

public class EmailSender implements ISender {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

	private Properties properties;

	private Session authSession;

	private String sender;

	private List<String> tmpFiles;

	public EmailSender(String serverUrl, int serverPort) {
		super();
		this.tmpFiles = new ArrayList<>();

		properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smpt");
		properties.setProperty("mail.smtp.host", serverUrl);
		properties.setProperty("mail.smtp.port", Integer.toString(serverPort));
	}

	/// --- Configuration methods ---------

	public void setAnonymous(boolean anonymous) {
		LOGGER.info("Setting anonymous properties");
		properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(!anonymous));
		properties.setProperty("mail.smtp.auth", Boolean.toString(!anonymous));
	}

	public void setDebugger(boolean enable) {
		LOGGER.info("Enabling debugger: {}", enable);
		properties.setProperty("mail.debug", Boolean.toString(enable));
	}

	public void setProxy(String host, String port, String version) {
		LOGGER.info("Enabling proxy {}:{}", host, port);
		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", host);
		properties.setProperty("socksProxyPort", port);
		properties.setProperty("socksProxyVersion", version);
	}

	public void setSsl(String fallback, String port) {
		LOGGER.info("Enabling ssl {}:{}", fallback, port);
		properties.setProperty("mail.smtp.socketFactory.class", EmailSenderConstants.SSL_FACTORY);
		properties.setProperty("mail.smtp.socketFactory.fallback", fallback);
		properties.setProperty("mail.smtp.socketFactory.port", port);
	}

	/// --- Provider method ---------

	public void authenticate(String account, String password) {
		sender = account;

		final String user = account;
		final String pass = password;
		authSession = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});
	}

	public void logOut() {
		this.sender = "";
		this.authSession = null;
	}

	@Override
	public void send(IMessage message) throws Exception {
		Email email = (Email) message;
		try {
			createTmpFolder();
			Message mailMessage = new MimeMessage(authSession);

			mailMessage.setFrom(new InternetAddress(sender));
			setRecipients(mailMessage, email);

			mailMessage.setSubject(email.getSubject());
			mailMessage.setContent(createContent(email));

			Transport.send(mailMessage);
		} catch (Exception e) {
			LOGGER.error("Error risen while trying to send email: {}", e.getMessage());
		}

		deleteTmpFolder();
	}

	/// --- Private methods ---------

	private String clearAttachmentName(String path) {
		String separator = File.separator.equals("\\") ? File.separator + File.separator : File.separator;
		List<String> components = Arrays.asList(path.split(separator));
		return components.get(components.size() - 1);
	}

	private Multipart createContent(Email email) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(email.getMessage());

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// TODO: con l'email, ho dato la doppia opzione nel nome del file docx/pdf da gestire
		for (IAttachmentModel doc : email.getDynamicAttachment()) {
			String filePath = getAttachmentPath(doc.getFileName());
			doc.write(filePath);
			tmpFiles.add(filePath);
		}

		List<String> attachments = new ArrayList<>();
		attachments.addAll(email.getStaticAttachment());
		attachments.addAll(tmpFiles);

		for (String path : attachments) {
			DataSource source = new FileDataSource(path);
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(source));

			String filename = clearAttachmentName(path);
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
		}

		return multipart;
	}

	private void createTmpFolder() throws IOException {
		boolean tmpCreated = (new File(LibConstants.TMP_FOLDER)).mkdir();
		if (!tmpCreated) {
			throw new IOException(LibConstants.FOLDER_NOT_CREATED_MSG);
		}
	}

	private void deleteTmpFolder() {
		try {
			File attachment = new File(LibConstants.TMP_FOLDER);
			FileUtils.deleteDirectory(attachment);
			tmpFiles = new ArrayList<>();
		} catch (Exception e) {
			LOGGER.error("Error risen while deliting temporary folder: {}", e.getMessage());
		}
	}

	private String getAttachmentPath(String fileName) {
		return LibConstants.TMP_FOLDER.concat("/").concat(fileName);
	}

	private void setRecipients(Message message, Email email) throws Exception {
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getReceiver()));

		if (!email.getCarbonCopy().isEmpty()) {
			for (String address : email.getCarbonCopy()) {
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(address));
			}
		}

		if (!email.getHiddenReceiver().isEmpty()) {
			for (String address : email.getHiddenReceiver()) {
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(address));
			}
		}
	}

}

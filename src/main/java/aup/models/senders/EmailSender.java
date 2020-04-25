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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.constants.EmailSenderConstants;
import aup.constants.LibConstants;
import aup.interfaces.IAttachmentModel;
import aup.interfaces.ISender;
import aup.models.messages.Email;

public class EmailSender implements ISender<Email> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

	private static final String PROTOCOL_PLACEHOLDER = "protocol";

	private boolean isParallel;

	private Properties properties;

	private Session authSession;

	private String protocol;

	private String sender;

	private List<String> tmpFiles;

	public EmailSender(String protocol, String serverUrl, int serverPort) {
		super();
		this.protocol = protocol;

		properties = System.getProperties();
		properties.put("mail.transport.protocol", protocol);
		properties.setProperty(getKey("mail.protocol.host"), serverUrl);
		properties.setProperty(getKey("mail.protocol.port"), Integer.toString(serverPort));
	}

	/// --- Configuration methods ---------

	public void isParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}

	public void setAuth(boolean anonymous, String tls, String auth) {
		LOGGER.info("Setting anonymous properties");
		properties.put(tls, Boolean.toString(!anonymous));
		properties.put(auth, Boolean.toString(!anonymous));
	}

	public void setDebugger(boolean enable) {
		properties.put("mail.debug", Boolean.toString(enable));
	}

	public void setProxy(String host, String port, String version) {
		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", host);
		properties.setProperty("socksProxyPort", port);
		properties.setProperty("socksProxyVersion", version);
	}

	public void setSsl(String fallback, String port, String socketPort) {
		properties.setProperty(getKey("mail.protocol.socketFactory.class"), EmailSenderConstants.SSL_FACTORY);
		properties.setProperty(getKey("mail.protocol.socketFactory.fallback"), fallback);
		properties.setProperty(getKey("mail.protocol.port"), port);
		properties.setProperty(getKey("mail.protocol.socketFactory.port"), socketPort);
	}

	/// --- Provider method ---------

	public void authenticate(String account, String password) {
		sender = account;

		final String user = account;
		final String pass = password;
		authSession = Session.getInstance(properties, new javax.mail.Authenticator() {
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
	public void send(Email email) throws Exception {
		createTmpFolder();
		Message message = new MimeMessage(authSession);

		message.setFrom(new InternetAddress(sender));
		setRecipients(message, email);

		message.setSubject(email.getObject());
		message.setContent(createContent(email));

		Transport.send(message);
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

		String id = isParallel ? Long.toString(System.currentTimeMillis()) : "";
		for (IAttachmentModel doc : email.getDynamicAttachment()) {
			String filePath = getAttachmentPath(doc.getFileName(), id);
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
			attachment.delete();
			tmpFiles = new ArrayList<>();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private String getAttachmentPath(String fileName, String id) {
		String path = LibConstants.TMP_FOLDER.concat(File.pathSeparator);
		if (!id.isEmpty() && !id.isBlank()) {
			path = path.concat(id).concat("_");
		}
		return path.concat(fileName);
	}

	private String getKey(String key) {
		return key.replace(PROTOCOL_PLACEHOLDER, protocol);
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

package aup.modules;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.interfaces.IMessage;
import aup.interfaces.IRawDataSource;
import aup.interfaces.ISender;

public class UnionPrinter<T extends IMessage> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionPrinter.class);

	private IMessage messageModel;

	private ISender<T> sender;

	private IRawDataSource rawDataSource;

	public UnionPrinter() {
		super();
	}

	/// --- Setter ---------

	public void setMessage(IMessage emailModel) {
		this.messageModel = emailModel;
	}

	public void setEmailSender(ISender<T> emailSender) {
		this.sender = emailSender;
	}

	public void setDataSource(IRawDataSource rawDataSource) {
		this.rawDataSource = rawDataSource;
	}

	/// --- Business logics ---------

	@SuppressWarnings("unchecked")
	public void print() throws IOException {
		LOGGER.info("Start printing with {} class", messageModel.getClass());

		List<Map<String, String>> rawData = rawDataSource.getData();
		LOGGER.info("Processing {} rows", rawData.size());

		rawData.stream().forEach(map -> {
			try {
				IMessage message = messageModel.create(map);
				sender.send((T) message);
			} catch (Exception e) {
				LOGGER.error("Error risen while trying to send an email: {}", e.getMessage());
			}
		});

		LOGGER.info("Printing process completed!");
	}
}

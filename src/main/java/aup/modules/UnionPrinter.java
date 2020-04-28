package aup.modules;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.interfaces.IMessage;
import aup.interfaces.IRawDataSource;
import aup.interfaces.ISender;

public class UnionPrinter {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionPrinter.class);

	private IMessage messageModel;

	private ISender sender;

	private IRawDataSource rawDataSource;

	public UnionPrinter() {
		super();
	}
	
	public UnionPrinter(IMessage messageModel, ISender sender, IRawDataSource rawDataSource) {
		super();
		this.messageModel = messageModel;
		this.sender = sender;
		this.rawDataSource = rawDataSource;
	}

	/// --- Setter ---------

	public void setMessage(IMessage emailModel) {
		this.messageModel = emailModel;
	}

	public void setEmailSender(ISender emailSender) {
		this.sender = emailSender;
	}

	public void setDataSource(IRawDataSource rawDataSource) {
		this.rawDataSource = rawDataSource;
	}

	/// --- Business logics ---------

	public void print() throws IOException {
		LOGGER.info("Start printing with {} class", messageModel.getClass());

		List<Map<String, String>> rawData = rawDataSource.getData();
		LOGGER.info("Processing {} rows", rawData.size());

		rawData.stream().forEach(map -> {
			try {
				IMessage message = messageModel.create(map);
				sender.send(message);
			} catch (Exception e) {
				LOGGER.error("Error risen while trying to send an email: {}", e.getMessage());
			}
		});

		LOGGER.info("Printing process completed!");
	}
}

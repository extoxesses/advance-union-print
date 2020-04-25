package aup.modules;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aup.interfaces.IMessage;
import aup.interfaces.IRawDataSource;
import aup.interfaces.ISender;

public class UnionPrinter {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionPrinter.class);

	private IMessage messageModel;

	private ISender<IMessage> sender;

	private IRawDataSource rawDataSource;

	public UnionPrinter() {
		super();
	}

	/// --- Setter ---------

	public void setMessage(IMessage emailModel) {
		this.messageModel = emailModel;
	}

	public void setEmailSender(ISender<IMessage> emailSender) {
		this.sender = emailSender;
	}

	public void setDataSource(IRawDataSource rawDataSource) {
		this.rawDataSource = rawDataSource;
	}

	/// --- Business logics ---------

	public void print(boolean isParallel) throws IOException {
		LOGGER.info("Start printing with {} class. Print parallel: {}", messageModel.getClass(), isParallel);
		
		List<Map<String, String>> rawData = rawDataSource.getData();
		LOGGER.info("Processing {} rows", rawData.size());
		
		Stream<Map<String, String>> stream = isParallel ? rawData.parallelStream() : rawData.stream();
		stream.forEach(map -> {
			try {
				IMessage message = messageModel.create(map);
				sender.send(message);
			} catch (Exception e) {
				e.getStackTrace();
			}
		});
		
		LOGGER.info("Printing process completed!");
	}
}

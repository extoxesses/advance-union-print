package aup.modules;

import java.util.ArrayList;
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

	private List<Map<String, String>> printQueue;

	public UnionPrinter() {
		super();
	}

	public UnionPrinter(IMessage messageModel, ISender sender, IRawDataSource rawDataSource) {
		super();
		this.messageModel = messageModel;
		this.sender = sender;
		this.printQueue = new ArrayList<>(rawDataSource.getData());
	}

	/// --- Setter ---------

	public void setMessage(IMessage emailModel) {
		this.messageModel = emailModel;
	}

	public void setEmailSender(ISender emailSender) {
		this.sender = emailSender;
	}

	public void setDataSource(IRawDataSource rawDataSource) {
		this.printQueue = new ArrayList<>(rawDataSource.getData());
		LOGGER.info("Loaded {} messages", this.printQueue.size());
	}

	/// --- Getter ---------
	
	public List<Map<String,String>> getPrintQueue() {
		return new ArrayList<>(this.printQueue);
	}
	
	/// --- Business logics ---------

	public void unloadQueue() {
		this.printQueue = new ArrayList<>();
	}
	
	public int print() {
		LOGGER.info("Start printing {} messages using {} class", printQueue.size(), messageModel.getClass());

		List<Map<String, String>> notPrintedElementes = new ArrayList<>();
		printQueue.stream().forEach(map -> {
			try {
				IMessage message = messageModel.create(map);
				sender.send(message);
			} catch (Exception e) {
				LOGGER.error("Error risen while trying to send an email: {}", e.getMessage());
				notPrintedElementes.add(map);
			}
		});

		int printedElements = printQueue.size() - notPrintedElementes.size();
		LOGGER.info("Printing process completed: printed {} elements over {}", printedElements, printQueue.size());
		
		printQueue = notPrintedElementes;
		LOGGER.info("Queue updated!");
		
		return printedElements;
	}
}

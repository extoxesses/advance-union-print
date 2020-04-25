package aup.modules;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import aup.constants.LibConstants;
import aup.interfaces.IMessage;
import aup.interfaces.IRawDataSource;
import aup.interfaces.ISender;

public class UnionPrinter {

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

	public void printCallback(boolean isParallel, boolean addId) throws IOException {
		boolean tmpCreated = (new File(LibConstants.TMP_FOLDER)).mkdir();
		if (!tmpCreated) {
			throw new IOException(LibConstants.FOLDER_NOT_CREATED_MSG);
		}

		List<Map<String, String>> rawData = rawDataSource.getData();
		Stream<Map<String, String>> stream = isParallel ? rawData.parallelStream() : rawData.stream();
		stream.forEach(map -> {
			IMessage message = messageModel.create(map);
			sender.send(message);
		});

		deleteTmpAttachments();

//		List<String> tmpAttachments = new ArrayList<>();
//		String id = getId(addId);
//		message.getDynamicAttachment().forEach(document -> {
//			String fileName = id.concat(document.getFileName());
//			document.write(fileName);
//			tmpAttachments.add(fileName);
//		});

	}

	/// --- Private methods ---------

	private void deleteTmpAttachments() {
		try {
			File attachment = new File(LibConstants.TMP_FOLDER);
			attachment.delete();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public String getId(boolean add) {
		long millis = System.currentTimeMillis() % 1000;
		return add ? Long.toString(millis).concat("_") : "";
	}
}

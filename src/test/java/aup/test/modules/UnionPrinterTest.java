package aup.test.modules;

import java.util.List;
import java.util.Map;

import javax.mail.MethodNotSupportedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.interfaces.IAttachmentModel;
import aup.interfaces.IKeyExtractor;
import aup.interfaces.IMessage;
import aup.interfaces.IRawDataSource;
import aup.interfaces.ISender;
import aup.modules.UnionPrinter;
import aup.test.utils.TestUtils;

public class UnionPrinterTest {

	@Test
	@DisplayName("UnionPrinter - Test with correct result")
	public void printCorrectTest() {
		UnionPrinter printer = new UnionPrinter(getVoidMessage(), getVoidSender(), getSimpleSource());
		Assertions.assertNotNull(printer);

		List<Map<String, String>> queue = printer.getPrintQueue();
		Assertions.assertEquals(TestUtils.getTestMap(), queue);

		int results = printer.print();
		Assertions.assertEquals(TestUtils.getTestMap().size(), results);

		queue = printer.getPrintQueue();
		Assertions.assertTrue(queue.isEmpty());
	}

	@Test
	@DisplayName("UnionPrinter - Test with not correct result")
	public void printUncorrectTest() {
		UnionPrinter printer = new UnionPrinter();
		Assertions.assertNotNull(printer);

		printer.setDataSource(getSimpleSource());
		printer.setEmailSender(getBrokenSender());
		printer.setMessage(getVoidMessage());

		List<Map<String, String>> queue = printer.getPrintQueue();
		Assertions.assertEquals(TestUtils.getTestMap(), queue);

		int results = printer.print();
		Assertions.assertEquals(0, results);

		queue = printer.getPrintQueue();
		Assertions.assertFalse(queue.isEmpty());
	}

	@Test
	@DisplayName("UnionPrinter - Unload test")
	public void printUnloadTest() {
		UnionPrinter printer = new UnionPrinter(null, null, getSimpleSource());
		Assertions.assertNotNull(printer);

		List<Map<String, String>> queue = printer.getPrintQueue();
		Assertions.assertEquals(TestUtils.getTestMap(), queue);

		printer.unloadQueue();
		Assertions.assertTrue(printer.getPrintQueue().isEmpty());
	}

	/// --- Private methods ---------

	private IMessage getVoidMessage() {
		return new IMessage() {

			@Override
			public List<IAttachmentModel> getDynamicAttachment() {
				// Empty method
				return null;
			}

			@Override
			public IMessage create(Map<String, String> replacementMap) {
				// Empty method
				return null;
			}
		};
	}

	private ISender getVoidSender() {
		return new ISender() {

			@Override
			public void send(IMessage message) throws Exception {
				// Empty
			}
		};
	}

	private ISender getBrokenSender() {
		return new ISender() {

			@Override
			public void send(IMessage message) throws Exception {
				throw new MethodNotSupportedException();
			}
		};
	}

	private IRawDataSource getSimpleSource() {
		return new IRawDataSource() {

			@Override
			public void setKeysExtractor(IKeyExtractor extractor) {
				// Empty method
			}

			@Override
			public List<Map<String, String>> getData() {
				return TestUtils.getTestMap();
			}
		};
	}

}

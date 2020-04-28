package aup.models.attachments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import aup.interfaces.IAttachmentModel;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class MsWordDoc implements IAttachmentModel {

	private static final String DOC_EXTENSION = ".docx";
	
	private static final String PDF_EXTENSION = ".pdf";
	
	private String defaultValue;

	private boolean saveToPdf;

	private XWPFDocument document;
	
	private String fileName;

	public MsWordDoc() {
		super();
		this.defaultValue = DEFAULT_VALUE;
	}

	@Override
	public void read(String filePath) throws Exception {
		File file = new File(filePath);
		FileInputStream instream = new FileInputStream(file.getAbsolutePath());
		document = new XWPFDocument(instream);
		instream.close();
	}

	@Override
	public void write(String filePath) throws Exception {
		FileOutputStream stream;

		if (!saveToPdf) {
			stream = new FileOutputStream(filePath.concat(DOC_EXTENSION));
			document.write(stream);
		} else {
			stream = new FileOutputStream(filePath.concat(PDF_EXTENSION));
			PdfOptions options = PdfOptions.create();
			PdfConverter.getInstance().convert(document, stream, options);
		}

		document.close();
		stream.close();
	}

	// TODO: updated con furbizie di renewal-policy
	@Override
	public IAttachmentModel replace(Map<String, String> replacementMap) {
		replacementMap.keySet().stream().forEach(key -> {
			String value = replacementMap.getOrDefault(key, defaultValue);
			replaceElement(key, value);
		});

		return this;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	/// --- Getter and setter methods ---------

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean saveToPdf() {
		return saveToPdf;
	}

	public void saveToPdf(boolean saveToPdf) {
		this.saveToPdf = saveToPdf;
	}

	/// --- Private Methods ---------

	private void replaceElement(String key, String value) {
		for (XWPFParagraph p : document.getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					String text = r.getText(0);
					if (text != null && text.contains(key)) {
						r.setText(text.replace(key, value), 0);
					}
				}
			}
		}
	}

}

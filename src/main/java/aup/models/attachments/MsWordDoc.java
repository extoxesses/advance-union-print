package aup.models.attachments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegement;
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
	public String write(String filePath) throws Exception {
		String path = Objects.isNull(filePath) ? "" : filePath;
		String ext = saveToPdf ? PDF_EXTENSION : DOC_EXTENSION;
		path = path.concat("/").concat(fileName).concat(ext);

		if (!saveToPdf) {
			FileOutputStream stream = new FileOutputStream(path);
			document.write(stream);
			stream.close();
		} else {
			PdfOptions options = PdfOptions.create();
			OutputStream stream = new FileOutputStream(new File(path));
			PdfConverter.getInstance().convert(document, stream, options);
			stream.close();
		}

		document.close();

		return path;
	}

	@Override
	public IAttachmentModel replace(Map<String, String> replacementMap) {
		replacementMap.keySet().stream().forEach(key -> {
			String value = replacementMap.getOrDefault(key, defaultValue);
			replaceInParagraphs(key, value);
		});

		return this;
	}

	/// --- Getter and setter methods ---------

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getFileName() {
		return this.fileName;
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

	// Inspired by:
	// https://stackoverflow.com/questions/24203087/replacing-a-text-in-apache-poi-xwpf-not-working
	public void replaceInParagraphs(String key, String value) {
		for (XWPFParagraph paragraph : document.getParagraphs()) {
			List<XWPFRun> runs = paragraph.getRuns();

			TextSegement found = paragraph.searchText(key, new PositionInParagraph());
			if (Objects.isNull(found)) {
				continue;
				
			} else if (found.getBeginRun() == found.getEndRun()) {
				// Whole search string is in one Run
				XWPFRun run = runs.get(found.getBeginRun());
				String runText = run.getText(run.getTextPosition());
				String replaced = runText.replace(key, value);
				run.setText(replaced, 0);

			} else {
				// The search string spans over more than one Run
				// Put the Strings together
				StringBuilder b = new StringBuilder();
				for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
					XWPFRun run = runs.get(runPos);
					b.append(run.getText(run.getTextPosition()));
				}
				String connectedRuns = b.toString();
				String replaced = connectedRuns.replace(key, value);
				// The first Run receives the replaced String of all connected Runs
				XWPFRun partOne = runs.get(found.getBeginRun());
				partOne.setText(replaced, 0);
				// Removing the text in the other Runs.
				for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
					XWPFRun partNext = runs.get(runPos);
					partNext.setText("", 0);
				}
			}
		}
	}

}

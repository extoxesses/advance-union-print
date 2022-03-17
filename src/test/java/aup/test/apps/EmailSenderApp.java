package aup.test.apps;

import aup.apps.AupToMail;
import aup.constants.EmailSenderConstants;
import aup.interfaces.IAttachmentModel;
import aup.models.attachments.MsWordDoc;
import aup.models.data.CsvRawData;
import aup.models.messages.Email;
import aup.models.senders.EmailSender;
import aup.modules.UnionPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailSenderApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderApp.class);

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Test application requires two arguments: (1) Username (2) Password");
            }
            AupToMail app = new AupToMail();

            // 1) Configure email provider
            app.setProvider(args[0], args[1], EmailSenderConstants.GMAIL_SMTP);
            app.setDebug(true);

            // 2) Create email template
            String subject = "Test object";
            // TODO: describe placeholder rules
            String mailBody = "<h3>Ciao <b>{{name}}</b>,</h3><br/>questo è un esempio di replace nel testo";
            List<String> staticAttachments = Collections.singletonList("src/test/resources/email/static.atc.txt");

            Map<String, String> files = new HashMap<>();
            files.put("File name", "src/test/resources/email/dynamic.atc.docx");
            app.setEmailTemplate(subject, mailBody, staticAttachments, getDynamicAttachments(files));

            // 3) Configure feeder
            app.setFeeder("src/test/resources/email/source.csv");

            // 4) Finalize operation
            app.print();

        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage());
        }
    }

    /// --- Private Methods ---------

    private static List<IAttachmentModel> getDynamicAttachments(Map<String, String> files) throws IOException {
        List<IAttachmentModel> attachments = new ArrayList<>();
        for (Map.Entry<String, String> entry : files.entrySet()) {
            MsWordDoc doc = new MsWordDoc();
            doc.setFileName(entry.getKey());
            doc.read(entry.getValue());
            doc.saveToPdf(true);
        }
        return attachments;
    }

}

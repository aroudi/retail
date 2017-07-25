package au.com.biztune.retail.processor;

import au.com.biztune.retail.util.Environment;
import au.com.biztune.retail.util.queuemanager.Processor;
import au.com.biztune.retail.util.queuemanager.Request;
import au.com.biztune.retail.util.queuemanager.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by arash on 3/03/2017.
 */
public class EMailProcessor implements Processor {
    private Session session;
    private String userName;
    private final Logger logger = LoggerFactory.getLogger(EMailProcessor.class);

    /**
     * Process Email requests.
     * @param request request
     * @return Response
     */
    public Response process(Request request) {
        final Response response = new Response();
        try {
            EmailRequest emailRequest;
            if (request instanceof EmailRequest) {
                emailRequest = (EmailRequest) request;
            } else {
                response.setSucceeded(false);
                response.setMessage("BAD EMAIL REQUEST");
                return response;
            }

            //init mail server.
            if (session == null) {
                init();
            }
            // creates a new e-mail message
            final Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(userName));
            final InternetAddress[] toAddresses = {new InternetAddress(emailRequest.getToAddress())};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(emailRequest.getSubject());
            msg.setSentDate(new Date());

            // creates message part
            final MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailRequest.getMessage(), "text/html");

            // creates multi-part
            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // adds attachments
            if (emailRequest.isSendAsStream()) {
                final DataSource dataSource = new ByteArrayDataSource(emailRequest.getAttachFileAsStream().toByteArray(), "application/pdf");
                final MimeBodyPart pdfBodyPart = new MimeBodyPart();
                pdfBodyPart.setDataHandler(new DataHandler(dataSource));
                pdfBodyPart.setFileName(emailRequest.getAttachFileName());
                multipart.addBodyPart(pdfBodyPart);
            } else {
                final MimeBodyPart attachPart = new MimeBodyPart();
                multipart.addBodyPart(attachPart);
            }
            // sets the multi-part as e-mail's content
            msg.setContent(multipart);

            // sends the e-mail
            logger.debug("Email : prepare for sending" + toAddresses);
            Transport.send(msg);
            logger.debug("Email sent to " + toAddresses);
            response.setSucceeded(true);
            return response;

        } catch (Exception e) {
            logger.error("Exception in processing email request: ", e);
            response.setSucceeded(false);
            response.setMessage("Exception in processing email request :" + e.getMessage());
            return response;
        }
    }

    /**
     * init the mail server.
     */
    private void init() {
        try {
            // sets SMTP server properties
            final String host = Environment.get("SMTP_HOST");
            final String port = Environment.get("SMTP_PORT");
            userName = Environment.get("SMTP_MAIL_FROM");
            //final String password = Environment.get("SMTP_PASSWORD");

            final Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            //properties.put("mail.smtp.auth", "true");
            //properties.put("mail.smtp.starttls.enable", "true");
            //properties.put("mail.user", userName);
            //properties.put("mail.password", password);

            // creates a new session with an authenticator
            /*
            //final Authenticator auth = new Authenticator() {
            //    public PasswordAuthentication getPasswordAuthentication() {
            //        return new PasswordAuthentication(userName, password);
            //    }
            };
            */
            session = Session.getDefaultInstance(properties, null);
            logger.info("EMail Sender initialised.");
        } catch (Exception e) {
            logger.error("Exception in initializing EMail", e);
        }
    }
}

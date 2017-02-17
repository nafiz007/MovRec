package cs2340.teamnasamovierecommender.pojo;

/**
 * Created by sai on 4/24/16.
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailSender extends javax.mail.Authenticator {
    static {
        Security.addProvider(new JSSEProvider());
    }

    /**
     * The String for user
     */
    private final String user;
    /**
     * The String for password
     */
    private final String password;
    /**
     * The Session
     */
    private Session session;

    /**
     * Constructor for GMailSender
     */
    public GmailSender() {
        this.user = "noreply.TeamNASA@gmail.com";
        this.password = "teamnasa123456";

        final Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        final String mailhost = "smtp.gmail.com";
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");

        props.setProperty("mail.smtp.quitwait", "false");
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply.TeamNASA@gmail.com", "teamnasa123456");
            }
        });
        session = Session.getDefaultInstance(props, this);
    }

    /**
     * Creates a new PasswordAuthentication
     *
     * @return a new PasswordAuthentication
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    /**
     * @param body       the body of the message
     * @param recipients the recipient of the message
     * @throws MessagingException an exception thrown by the methods called in this method
     */
    public synchronized void sendMail(final String body,
                                      final String recipients) throws MessagingException {
        final MimeMessage message = new MimeMessage(session);
        final DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes()));
        message.setSender(new InternetAddress("noreply.TeamNASA@mail.com"));
        message.setSubject("Password Recovery");
        message.setDataHandler(handler);

        if (recipients.indexOf(',') > 0) {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        } else {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    recipients));
        }
        Transport.send(message);
    }

    public class ByteArrayDataSource implements DataSource {
        /**
         * byte[] data
         */
        private final byte[] data;
        /**
         * the String for type
         */
        private final String type;

        /**
         * Constructor for ByteArrayDataSource
         *
         * @param data1 the data that should be turned into ByteArrayDataSource
         */
        public ByteArrayDataSource(final byte[] data1) {
            super();
            this.data = data1;
            this.type = "text/plain";
        }

        /**
         * Gets the content type
         *
         * @return the conent type of the GMailSender
         */
        public String getContentType() {
            if (type == null) {
                return "application/octet-stream";
            } else {
                return type;
            }
        }

        /**
         * @return a new ByteArrayInputStream
         * @throws IOException an exception thrown by creating a ByteArrayInputStream
         */
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        /**
         * gets the name of the ByteArrayDataSource
         *
         * @return "ByteArrayDataSource"
         */
        public String getName() {
            return "ByteArrayDataSource";
        }

        /**
         * gets the OutputStream of the ByteArrayDataSource
         *
         * @return Always throws an IOException
         * @throws IOException Always thrown
         */
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}

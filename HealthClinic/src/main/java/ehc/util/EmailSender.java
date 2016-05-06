package ehc.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import ehc.util.DateUtil;

public class EmailSender {
	static Properties properties;
	private static String host;
	private static String port;
	private static String userName;
	private static String password;
	private static String tls;
	private static String auth;
	private String testingEnviromentEmailFrom;
	private String testingEnviromentEmailTo;

	public EmailSender(String host, String port, String userName, String password, String tls, String auth) {
		super();
		EmailSender.host = host;
		EmailSender.port = port;
		EmailSender.userName = userName;
		EmailSender.password = password;
		EmailSender.tls = tls;
		EmailSender.auth = auth;
	}

	public EmailSender(String host, String port, String userName, String password, String tls, String auth,
			String testingEnviromentEmailFrom, String testingEnviromentEmailTo) {
		this(host, port, userName, password, tls, auth);
		if (null != testingEnviromentEmailFrom) {
			this.testingEnviromentEmailFrom = testingEnviromentEmailFrom;
		}
		if (null != testingEnviromentEmailTo) {
			this.testingEnviromentEmailTo = testingEnviromentEmailTo;
		}
	}

	private static void setSmtpProperties() throws MessagingException {
		// sets SMTP server properties
		if ((null != host) && (null != port) && (null != userName) && (null != password) && (null != tls)
				&& (null != auth)) {
			properties = new Properties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", port);
			properties.put("mail.user", userName);
			properties.put("mail.password", password);
			properties.put("mail.smtp.starttls.enable", tls);
			properties.put("mail.smtp.auth", auth);
		} else {
			throw new MessagingException();
		}
	}

	private static String getMachineName() throws UnknownHostException {
		String host = System.getenv("COMPUTERNAME");
		if (host != null) {
			return host;
		}

		host = System.getenv("HOSTNAME");
		if (host != null) {
			return host;
		}

		host = InetAddress.getLocalHost().getHostName();
		if (host != null) {
			return host;
		}

		return null;
	}

	public static String formatExceptionEmailSubject(String emailSubject, Throwable exception)
			throws UnknownHostException {
		if ((null != emailSubject) && (null != exception)) {
			return MessageFormat.format(emailSubject, exception.getClass().getName());
		}

		return null;
	}

	public static String formatExceptionEmailSubject(String emailSubject, String string) throws UnknownHostException {
		if ((null != emailSubject) && (null != string)) {
			return MessageFormat.format(emailSubject, string);
		}

		return null;
	}

	public static String formatExceptionEmailMessage(String emailMessage, Throwable exception)
			throws UnknownHostException {
		if ((null != emailMessage) && (null != exception)) {
			return MessageFormat.format(emailMessage, getMachineName(), DateUtil.now().toString(),
					ExceptionUtils.getFullStackTrace(exception));
		}
		return null;
	}

	public static String formatExceptionEmailMessage(String emailMessage, List<String> arguments) {
		if ((null != emailMessage) && (null != arguments) && !arguments.isEmpty()) {
			return MessageFormat.format(emailMessage, arguments);
		}
		return null;
	}

	public static String formatFatalErrorEmailMessage(String emailMessage, String s) throws UnknownHostException {
		if ((null != emailMessage) && (null != s)) {
			return MessageFormat.format(emailMessage, getMachineName(), DateUtil.now().toString(), s);
		}
		return null;
	}

	public void sendMessage(String fromAddress, String[] toAddresses, String[] toAddressesCC, String[] toAddressesBCC,
			String subject, String message, String[] attachFiles, String senderAlias) throws AddressException, MessagingException {

		setSmtpProperties();

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);
		String emailFromAddress = fromAddress;

		if (null != getTestingEnviromentEmailFrom()) {
			emailFromAddress = getTestingEnviromentEmailFrom();
		}
		if (StringUtils.isNotEmpty(senderAlias)){
			emailFromAddress = senderAlias;
		}
		msg.setFrom(new InternetAddress(emailFromAddress));

		if (null != getTestingEnviromentEmailTo()) {
			setUpEmailRecipientsIfNeeded(getTestingEnviromentEmailTo().split(";"), Message.RecipientType.TO, msg);
		} else if ((null != toAddresses) && (0 < toAddresses.length)) {
			setUpEmailRecipientsIfNeeded(toAddresses, Message.RecipientType.TO, msg);
			setUpEmailRecipientsIfNeeded(toAddressesCC, Message.RecipientType.CC, msg);
			setUpEmailRecipientsIfNeeded(toAddressesBCC, Message.RecipientType.BCC, msg);
		}

		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		// messageBodyPart.setContent(message, "text/html");
		messageBodyPart.setText(message, "UTF-8", "html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);
	}

	private void setUpEmailRecipientsIfNeeded(String[] toAddresses, RecipientType recipientType, Message msg)
			throws AddressException, MessagingException {
		if ((null != toAddresses) && (0 < toAddresses.length)) {
			InternetAddress[] toInternetAddressesCC = new InternetAddress[toAddresses.length];
			for (int i = 0; i < toAddresses.length; i++) {
				toInternetAddressesCC[i] = new InternetAddress(toAddresses[i]);
			}
			msg.setRecipients(recipientType, toInternetAddressesCC);
		}
	}

	public void sendMessage(String fromAddress, String[] toAddresses, String subject, String message,
			String[] attachFiles, String senderAlias) throws AddressException, MessagingException {
		sendMessage(fromAddress, toAddresses, null, null, subject, message, attachFiles, senderAlias);
	}

	protected String getTestingEnviromentEmailFrom() {
		return testingEnviromentEmailFrom;
	}

	protected String getTestingEnviromentEmailTo() {
		return testingEnviromentEmailTo;
	}
}
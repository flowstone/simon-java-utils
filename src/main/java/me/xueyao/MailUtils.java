package me.xueyao;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具
 */
public class MailUtils {
	/**
	 * 发送邮件
	 * @param from
	 * @param to
	 * @param subject
	 * @param msgText
	 */
	public static void sendMail(String from, String to, String subject, String msgText) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.debug", false);

		Session session = Session.getInstance(props, null);
		session.setDebug(false);

		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			// If the desired charset is known, you can use
			// setText(text, charset)
			msg.setText(msgText);

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

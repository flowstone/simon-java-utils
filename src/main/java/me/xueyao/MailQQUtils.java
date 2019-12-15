package me.xueyao;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailQQUtils {
	/**
	 * 发送邮件
	 * @param to   接收邮件的地址
	 * @param subject  邮件主题
	 * @param msgText   邮件内容
	 */
	public static void send(String to, String subject, String msgText) {
		//发邮件的地址
		String from = "931330220@qq.com"; 
		String password = "授权码";
		//邮件发送服务器地址
		String host = "smtp.qq.com"; 
		//是否开启debug模式
		boolean debug = true; 

		// 设置发送邮件的配置信息
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		
		if (debug) {
			props.put("mail.debug", debug);
		}
		//添加auth认证
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		
		//邮件会话
		Session session = Session.getInstance(props, null);
		session.setDebug(debug);

		try {
			//创建邮件
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			//设置主题
			msg.setSubject(subject);
			//设置发送时间
			msg.setSentDate(new Date());
			//设置邮件的内容
			msg.setText(msgText);
			//发送邮件
			Transport.send(msg,from,password);
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String to = "18896544516@163.com";
		String subject = "如何学习?";
		String msgText = "解决学习困扰,就是天天晚上熬夜学习";
		MailQQUtils.send(to, subject, msgText);

		
	}

	
}

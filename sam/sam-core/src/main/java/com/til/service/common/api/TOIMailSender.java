/**
 * 
 */
package com.til.service.common.api;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.hibernate.entity.Emails;
import com.til.service.utils.Utilities;

/**
 * @author girish.gaurav
 * 
 */
@Service
public class TOIMailSender {

	private static final Logger log = LoggerFactory.getLogger(TOIMailSender.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailsDao emailsDao;

	/**
	 * 
	 */
	public TOIMailSender() {
	}

	public Emails sendEmail(final int id) {
		return sendEmail(id, false);
	}

	public Emails sendEmail(final int id, final boolean retryOnFailure) {
		Emails emails = emailsDao.get(id);
		if (emails != null) {
			log.info("sending email with id {}", id);
			sendEmail(emails, retryOnFailure);
			emailsDao.saveOrUpdate(emails);
		}
		return emails;
	}

	public Emails sendEmail(final Emails email) {
		return sendEmail(email, false);
	}

	public Emails sendEmail(final Emails email, final boolean retryOnFailure) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "UTF-8");
				message.setTo(email.getToEmail().split("[,;]"));
				if (email.getCcemail() != null && !"".equals(email.getCcemail()))
					message.setCc(email.getCcemail().split("[,;]"));
				if (email.getBccemail() != null && !"".equals(email.getBccemail()))
					message.setBcc(email.getBccemail().split("[,;]"));
				// could be parameterized...
				message.setFrom(email.getFromEmail(), email.getfromName());
				message.setSubject(email.getSubject());
				// 2nd parameter decides whether to send an html email or not
				message.setText(Utilities.decode(email.getData()), true);
				if (null != email.getAttachmentname()
						&& !"".equals(email.getAttachmentname())) {
					ByteArrayResource byteFile = new ByteArrayResource(
							email.getAttachment());
					message.addAttachment(email.getAttachmentname(), byteFile);
				}
			}
		};

		if (retryOnFailure) {
			for (int i = 0; i < Emails.RETRY_COUNT; i++) {
				sendEmail(email, preparator);
				if (Emails.STATUS_PROCESSED.equals(email.getStatus())) {
					break;
				}
			}
		} else {
			sendEmail(email, preparator);
		}

		return email;
	}

	public Emails sendEmail(final Emails email, MimeMessagePreparator preparator) {
		Date nowTime = new Date();

		try {
			// increment retry count
			email.setRetryCount(email.getRetryCount() + 1);
			email.setUpdateDate(nowTime);

			this.mailSender.send(preparator);
			email.setStatus(Emails.STATUS_PROCESSED);
			log.info("Mail successfully sent to {}", email.getToEmail());
		} catch (Exception e) {
			// simply log it and go on...
			log.error("Exception while sending email", e);

			if (email.getRetryCount() >= Emails.RETRY_COUNT) {
				email.setStatus(Emails.STATUS_IGNORED);
			} else {
				// let it be in pending
			}
			email.setRemarks("EXCEPTION: " + e.getMessage());
		}
		return email;
	}
}

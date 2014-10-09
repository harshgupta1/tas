package com.til.service.job;


import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.hibernate.entity.Emails;
import com.til.service.utils.Utilities;

/**
 * @author Harsh.Gupta
 *
 */
@Service
public class EmailNotificationJob {
	
	private static final Logger 	log = LoggerFactory.getLogger(EmailNotificationJob.class);
	
	@Autowired
	private EmailsDao 			emailsDao = null ;
	@Autowired
	private JavaMailSender 		mailSender = null ;
	
	@Value("${emailNotificationJob.enabled}")
	private String 				enabled;
	
	private String 				emailReplyUrl = "http://tlabs.in/mailreply.html";
	/**
	 * Notify the consumers
	 */
	@Profiled(tag = "emailNotificationJob")
	@Scheduled(fixedRate=60000)
	public void execute() {
		
		if("true".equalsIgnoreCase(enabled))
		{
			try
			{
				// list of pending email
				// for each event prepare text and send it out
				
				// query determines "unprocessed events"
				List<Emails> emailList = emailsDao.findAllUnprocessedEmails();
				
				for (Emails emails : emailList) {
					// prepare the mail text and send it out
					sendEmail(emails) ;
				}
			}
			catch(Exception e)
			{
				log.error("Exception catched while sending email. Please debug. Error Message: " , e);
			}
		}
	}
	
	private void sendEmail(final Emails emails) {
		Date nowTime = new Date() ;
		
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo( emails.getToEmail().split("[,;]") );
                if(emails.getCcemail() != null && !"".equals(emails.getCcemail()))
                	message.setCc( emails.getCcemail().split("[,;]") );
                if(emails.getBccemail() != null && !"".equals(emails.getBccemail()))
                message.setBcc( emails.getBccemail().split("[,;]") );
                message.setFrom(emails.getFromEmail(), emails.getfromName()); // could be parameterized...
                message.setSubject(emails.getSubject());
                // 2nd parameter decides whether to send an html email or not
                message.setText(Utilities.decode(emails.getData()), true);
                if(null != emails.getAttachmentname() && !"".equals(emails.getAttachmentname()))
                {
                	ByteArrayResource byteFile = new ByteArrayResource(emails.getAttachment()); 
                	message.addAttachment(emails.getAttachmentname(), byteFile);
                }
            }
        };
        
        MimeMessagePreparator preparatorReply = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo(emails.getUserEmail() );
                message.setFrom(mailFrom); // could be parameterized...
                message.setSubject(mailSubject);
                // 2nd parameter decides whether to send an html email or not
                mailReply = Utilities.executeGet(emailReplyUrl);
                message.setText(mailReply.replace("{0}", emails.getUserName()), true);
            }
        };
        
        MimeMessagePreparator basecampqReply = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo("lbansal@mailmanagr.com");
                message.setFrom(mailFrom); // could be parameterized...
                message.setSubject(emails.getUserName() + "," +emails.getLocation()+","+emails.getPhoneno()+","+emails.getUserEmail());
                // 2nd parameter decides whether to send an html email or not
                message.setText(emails.getData(), false);
            }
        };
        try {
            this.mailSender.send(preparator);
            emails.setStatus(Emails.STATUS_PROCESSED) ;
            // Send Confirmation mail to user
            this.mailSender.send(preparatorReply);
            this.mailSender.send(basecampqReply);
        }
        catch(MailSendException ex)
        {
        	// TODO message file too big
        	log.error("MailSendException while sending email", ex);
            
            if(emails.getRetryCount() > Emails.RETRY_COUNT) {
            	emails.setStatus(Emails.STATUS_IGNORED) ;
            }
            else {
            	// let it be in pending
            }
            emails.setRemarks(ex.getMessage()) ;
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error("MailException while sending email", ex);
            
            if(emails.getRetryCount() > Emails.RETRY_COUNT) {
            	emails.setStatus(Emails.STATUS_IGNORED) ;
            }
            else {
            	// let it be in pending
            }
            emails.setRemarks(ex.getMessage()) ;
        }
        catch(Exception e)
        {
        	// simply log it and go on...
            log.error("Exception while sending email", e);
            
            if(emails.getRetryCount() > Emails.RETRY_COUNT) {
            	emails.setStatus(Emails.STATUS_IGNORED) ;
            }
            else {
            	// let it be in pending
            }
            emails.setRemarks("EXCEPTION: " + e.getMessage()) ;
        }
        emails.setRetryCount( emails.getRetryCount() + 1 ) ; // increment retry count
        emails.setUpdateDate(nowTime) ;
        emailsDao.saveOrUpdate(emails);
	}
	
	private String mailFrom = "tlabs@indiatimes.co.in";
	private String mailSubject = "Thank you for applying to TLabs program, we have received your application.";
	private String mailReply="Dear {0}," +
    "<p>Thanks for applying to the TLabs program, we have received your application and currently in the process of reviewing it. Someone from " + 
    "our team will get in touch with you if we have any further questions.</p>" +

    "<p>We will publish a list of finalists by 15th June 2012. Individual confirmations will be sent to all finalists as well.<p/>" +

    "<p>If you have would like to know more about the program, please feel free to contact us at tlabs@indiatimes.co.in <p/>" +

    "Thanks,<br/>" +
    "TLabs Team<br/>" +
    "<a href=\"http://tlabs.indiatimes.com\">http://tlabs.indiatimes.com</a>";
	
}  

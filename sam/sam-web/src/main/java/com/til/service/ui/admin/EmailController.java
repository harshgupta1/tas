package com.til.service.ui.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.hibernate.entity.Emails;

@Controller
public class EmailController implements org.springframework.web.servlet.HandlerExceptionResolver{
	
	/** Logger for this class and subclasses */
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private EmailsDao emailsDao;
	
	@Value("${tlabs.success_redirect_uri}")
    private String tlabs_success_redirect_uri;
	
	@Value("${tlabs.error_redirect_uri}")
    private String tlabs_error_redirect_uri;
	
	@Override
	public @ResponseBody ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception ex) {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		if ( ex instanceof MaxUploadSizeExceededException ) {
			log.error(" -- File Size Exceeds --" , ex);
			model.put("errors", ex.getCause().getMessage());
		}
		return new ModelAndView("/sendemail", (Map) model);
	}


	@RequestMapping(value="/sendemail", method=RequestMethod.GET)
	public @ResponseBody String handleGetRequest(HttpServletRequest request, HttpServletResponse response){
		
		return "<script>window.parent.('failure:" + request.getParameter("errors") + "')</script>";
		
	}
	/**
	 * The @ResponseBody annotation instructs Spring MVC to serialize the object to the client. 
	 * Spring MVC automatically serializes to XML/JSON/HTTP because the client accepts that content type.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sendemailnew", method=RequestMethod.POST)
	public String sendemailnew(HttpServletRequest request, HttpServletResponse response){
		
		String error = "";
		
		String fromName = request.getParameter("fromname");
		String fromEmail = request.getParameter("fromemail");
		String userName = request.getParameter("txtUserName");
		String userEmail = request.getParameter("txtEmail");
		String toEmail = request.getParameter("toemail");
		String toName = request.getParameter("toname");
		String subject = request.getParameter("subject");
		String data = request.getParameter("data");
		String site = request.getParameter("site");
		String script = request.getParameter("script");
		String companyname = request.getParameter("companyname");
		String websiteurl = request.getParameter("websiteurl");
		String companyprofit = request.getParameter("companyprofit");
		String founder = request.getParameter("founder");
		String location = request.getParameter("location");
		String phoneno = request.getParameter("phoneno");
		String businessidea = request.getParameter("businessidea");
		String companybusiness = request.getParameter("companybusiness");
		String competitors = request.getParameter("competitors");
		String team = request.getParameter("team");
		String foundersobligations = request.getParameter("foundersobligations");
		String companyselection = request.getParameter("companyselection");
		String ccemail = request.getParameter("ccemail");
		String bccemail = request.getParameter("bccemail");
		String mailer = request.getParameter("mailer");
		String hearus = request.getParameter("hearus");
		String archive1 = request.getParameter("archive1");
		String archive2 = request.getParameter("archive2");
		String archive3 = request.getParameter("archive3");
		String archive4 = request.getParameter("archive4");
		String archive5 = request.getParameter("archive5");
		String archive6 = request.getParameter("archive6");
		String archive7 = request.getParameter("archive7");
		
		
		if(fromEmail == null || "".equals(fromEmail.trim()))
		{
			fromEmail = "mailservice@indiatimes.com";
		}
		else if(toEmail == null || "".equals(toEmail.trim()))
		{
			error = "'To Email' Address is missing.";
		}
		else if(subject == null || "".equals(subject.trim()))
		{
			error = "Subject is missing.";
		}
		else if(data == null || "".equals(data.trim()))
		{
			error = "Data is missing.";
		}
		else if(site == null || "".equals(site.trim()))
		{
			error = "Site is missing.";
		}
		if(!"".equals(error))
		{
			return error;
		}
		else
		{
			Emails emails = new Emails();
			emails.setUserName(userName);
			emails.setUserEmail(userEmail);
			emails.setfromName(fromName);
			emails.setFromEmail(fromEmail);
			emails.setToEmail(toEmail);
			emails.setToName(toName);
			emails.setSubject(subject);
			emails.setData(data);
			emails.setSite(site);
			emails.setStatus(Emails.STATUS_PENDING);
			emails.setCompanyname((companyname != null) ? companyname : null);
			emails.setWebsiteurl((websiteurl != null) ? websiteurl : null);
			emails.setCompanyprofit((companyprofit != null) ? companyprofit : null);
			emails.setFounder((founder != null) ? founder : null);
			emails.setLocation((location != null) ? location : null);
			emails.setPhoneno((phoneno != null) ? phoneno : null);
			emails.setBusinessidea((businessidea != null) ? businessidea : null);
			emails.setCompanybusiness((companybusiness != null) ? companybusiness : null);
			emails.setCompetitors((competitors != null) ? competitors : null);
			emails.setTeam((team != null) ? team : null);
			emails.setFoundersobligations((foundersobligations != null) ? foundersobligations : null);
			emails.setCompanyselection((companyselection != null) ? companyselection : null);
			emails.setCcemail((ccemail != null) ? ccemail : null);
			emails.setBccemail((bccemail != null) ? bccemail : null);
			emails.setMailer((mailer != null && "1".equals(mailer)) ? 1 : 0);
			emails.setHearus(hearus);
			emails.setArchive1(archive1);
			emails.setArchive2(archive2);
			emails.setArchive3(archive3);
			emails.setArchive4(archive4);
			emails.setArchive5(archive5);
			emails.setArchive6(archive6);
			emails.setArchive7(archive7);
			
			if (request instanceof MultipartHttpServletRequest) {
		        // process the uploaded file
				MultipartFile multipartFile = ((MultipartHttpServletRequest) request).getFile("file");
				String attachmentname = multipartFile.getOriginalFilename();
				emails.setAttachmentname(attachmentname);
				try {
					byte[] attachment = multipartFile.getBytes();
					emails.setAttachment(attachment);
				} catch (IOException e) {
					log.error("IOException getting byte for file", e);
				}
		    }
			try
			{
				int emailid = emailsDao.save(emails);
				log.debug("Email successfully sent. ID {}" , emailid);
				if("1".equals(script))
				{
					return "<script>window.parent.result('success')</script>";
				}
				else if("-1".equals(script))
				{
					return "redirect:" + tlabs_success_redirect_uri;
				}
				else
				{
					return "Email successfully sent.";
				}
			}
			catch(Exception e)
			{
				log.error("Unable to save email." , e);
				if("1".equals(script))
				{
					return "<script>window.parent.('failure:" + e.getMessage() + "')</script>";
				}
				else if("-1".equals(script))
				{
					return "redirect:" + tlabs_error_redirect_uri;
				}
				else
				{
					return "Unable to save email.";
				}
			}
		}
	}
	
	@RequestMapping(value="/sendemail", method=RequestMethod.POST)
	public @ResponseBody String sendemail(HttpServletRequest request, HttpServletResponse response){
		
		String error = "";
		
		String fromName = request.getParameter("fromname");
		String fromEmail = request.getParameter("fromemail");
		String userName = request.getParameter("txtUserName");
		String userEmail = request.getParameter("txtEmail");
		String toEmail = request.getParameter("toemail");
		String toName = request.getParameter("toname");
		String subject = request.getParameter("subject");
		String data = request.getParameter("data");
		String site = request.getParameter("site");
		String script = request.getParameter("script");
		String companyname = request.getParameter("companyname");
		String websiteurl = request.getParameter("websiteurl");
		String companyprofit = request.getParameter("companyprofit");
		String founder = request.getParameter("founder");
		String location = request.getParameter("location");
		String phoneno = request.getParameter("phoneno");
		String businessidea = request.getParameter("businessidea");
		String companybusiness = request.getParameter("companybusiness");
		String competitors = request.getParameter("competitors");
		String team = request.getParameter("team");
		String foundersobligations = request.getParameter("foundersobligations");
		String companyselection = request.getParameter("companyselection");
		String ccemail = request.getParameter("ccemail");
		String bccemail = request.getParameter("bccemail");
		String hearus = request.getParameter("hearus");
		String mailer = request.getParameter("mailer");
		String archive1 = request.getParameter("archive1");
		String archive2 = request.getParameter("archive2");
		String archive3 = request.getParameter("archive3");
		String archive4 = request.getParameter("archive4");
		String archive5 = request.getParameter("archive5");
		String archive6 = request.getParameter("archive6");
		String archive7 = request.getParameter("archive7");
		
		if(fromEmail == null || "".equals(fromEmail.trim()))
		{
			fromEmail = "mailservice@indiatimes.com";
		}
		else if(toEmail == null || "".equals(toEmail.trim()))
		{
			error = "'To Email' Address is missing.";
		}
		else if(subject == null || "".equals(subject.trim()))
		{
			error = "Subject is missing.";
		}
		else if(data == null || "".equals(data.trim()))
		{
			error = "Data is missing.";
		}
		else if(site == null || "".equals(site.trim()))
		{
			error = "Site is missing.";
		}
		if(!"".equals(error))
		{
			return error;
		}
		else
		{
			Emails emails = new Emails();
			emails.setUserName(userName);
			emails.setUserEmail(userEmail);
			emails.setfromName(fromName);
			emails.setFromEmail(fromEmail);
			emails.setToEmail(toEmail);
			emails.setToName(toName);
			emails.setSubject(subject);
			emails.setData(data);
			emails.setSite(site);
			emails.setStatus(Emails.STATUS_PENDING);
			emails.setCompanyname((companyname != null) ? companyname : null);
			emails.setWebsiteurl((websiteurl != null) ? websiteurl : null);
			emails.setCompanyprofit((companyprofit != null) ? companyprofit : null);
			emails.setFounder((founder != null) ? founder : null);
			emails.setLocation((location != null) ? location : null);
			emails.setPhoneno((phoneno != null) ? phoneno : null);
			emails.setBusinessidea((businessidea != null) ? businessidea : null);
			emails.setCompanybusiness((companybusiness != null) ? companybusiness : null);
			emails.setCompetitors((competitors != null) ? competitors : null);
			emails.setTeam((team != null) ? team : null);
			emails.setFoundersobligations((foundersobligations != null) ? foundersobligations : null);
			emails.setCompanyselection((companyselection != null) ? companyselection : null);
			emails.setCcemail((ccemail != null) ? ccemail : null);
			emails.setBccemail((bccemail != null) ? bccemail : null);
			emails.setHearus(hearus);
			emails.setMailer((mailer != null && "1".equals(mailer)) ? 1 : 0);
			emails.setArchive1(archive1);
			emails.setArchive2(archive2);
			emails.setArchive3(archive3);
			emails.setArchive4(archive4);
			emails.setArchive5(archive5);
			emails.setArchive6(archive6);
			emails.setArchive7(archive7);
			if (request instanceof MultipartHttpServletRequest) {
		        // process the uploaded file
				MultipartFile multipartFile = ((MultipartHttpServletRequest) request).getFile("file");
				String attachmentname = multipartFile.getOriginalFilename();
				emails.setAttachmentname(attachmentname);
				try {
					byte[] attachment = multipartFile.getBytes();
					emails.setAttachment(attachment);
				} catch (IOException e) {
					log.error("IOException getting byte for file", e);
				}
		    }
			try
			{
				int emailid = emailsDao.save(emails);
				log.debug("Email successfully sent. ID {}" , emailid);
				if("1".equals(script))
				{
					return "<script>window.parent.result('success')</script>";
				}
				else
				{
					return "Email successfully sent.";
				}
			}
			catch(Exception e)
			{
				log.error("Unable to save email." , e);
				if("1".equals(script))
				{
					return "<script>window.parent.('failure:" + e.getMessage() + "')</script>";
				}
				else
				{
					return "Unable to save email.";
				}
			}
		}
	}
}

/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Harsh.Gupta
 *
 */
@NamedQueries({
	@NamedQuery(name="emails.findAllByStatus",
			query="from Emails e where e.status = ?"
	),
	@NamedQuery(name="emails.findAllUnprocessedEmailsSinceWhen",
			query="from Emails e where e.status = 'PENDING' and e.retryCount < 3 and e.createDate >= ?"
	)
})
@Entity
@Table(name="emails")
public class Emails implements Serializable{
	
	private static final long serialVersionUID = -4029126593885053198L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="username",updatable=false)
	private String 	userName;
	
	@Column(name="useremail",updatable=false)
	private String 	userEmail;
	
	@Column(name="toname",updatable=false)
	private String 	toName;
	
	@Column(name="toemail",updatable=false,nullable=false)
	private String 	toEmail ;
	
	private String	ccemail;
	private String	bccemail;
	
	@Column(name="fromemail",updatable=false,nullable=false)
	private String 	fromEmail ;
	
	@Column(name="returnemail",updatable=false,nullable=true)
	private String 	returnEmail ;
	
	@Column(updatable=false,nullable=false)
	private String 	subject ;
	
	@Column(updatable=false,nullable=false)
	private String	data ;
	
	@Column(updatable=true,nullable=false)
	private String	status ;
	
	@Column(name="retrycount")
	private int		retryCount ;
	
	private String	remarks ;
	
	@Column(name="fromname",nullable=true)
	private String	fromName ;
	
	@Column(name="site",nullable=false)
	private String	site ;
	
	private String archive1 ;
	private String archive2 ;
	private String archive3 ;
	private String archive4 ;
	private String archive5 ;
	private String archive6 ;
	private String archive7 ;
	
	private byte[]	attachment;
	private String	attachmentname;
	private String	companyname;
	private String	websiteurl;
	private String	companyprofit;
	private String	founder;
	private String	location;
	private String	phoneno;
	private String	businessidea;
	private String	companybusiness;
	private String	competitors;
	private String	team;
	private String	foundersobligations;
	private String	companyselection;
	private String	hearus;
	private	int		mailer;
	
	@Column(name="createdate",updatable=false,nullable=false)
	private Date	createDate = new Date();
	
	@Column(name="updatedate",nullable=false)
	private Date	updateDate;
	
	@Transient public static final int    RETRY_COUNT = 3 ;
	// Status CONSTANTS
	@Transient public static final String STATUS_PENDING 	= "PENDING" ;
	@Transient public static final String STATUS_PROCESSED = "PROCESSED" ;
	@Transient public static final String STATUS_IGNORED 	= "IGNORED" ;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getReturnEmail() {
		return returnEmail;
	}
	public void setReturnEmail(String returnEmail) {
		this.returnEmail = returnEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the retryCount
	 */
	public int getRetryCount() {
		return retryCount;
	}
	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getfromName() {
		return fromName;
	}
	/**
	 * @param data the data to set
	 */
	public void setfromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentname() {
		return attachmentname;
	}

	public void setAttachmentname(String attachmentname) {
		this.attachmentname = attachmentname;
	}

	public String getCcemail() {
		return ccemail;
	}

	public void setCcemail(String ccemail) {
		this.ccemail = ccemail;
	}

	public String getBccemail() {
		return bccemail;
	}

	public void setBccemail(String bccemail) {
		this.bccemail = bccemail;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getWebsiteurl() {
		return websiteurl;
	}

	public void setWebsiteurl(String websiteurl) {
		this.websiteurl = websiteurl;
	}

	public String getCompanyprofit() {
		return companyprofit;
	}

	public void setCompanyprofit(String companyprofit) {
		this.companyprofit = companyprofit;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getBusinessidea() {
		return businessidea;
	}

	public void setBusinessidea(String businessidea) {
		this.businessidea = businessidea;
	}

	public String getCompanybusiness() {
		return companybusiness;
	}

	public void setCompanybusiness(String companybusiness) {
		this.companybusiness = companybusiness;
	}

	public String getCompetitors() {
		return competitors;
	}

	public void setCompetitors(String competitors) {
		this.competitors = competitors;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getFoundersobligations() {
		return foundersobligations;
	}

	public void setFoundersobligations(String foundersobligations) {
		this.foundersobligations = foundersobligations;
	}

	public String getCompanyselection() {
		return companyselection;
	}

	public void setCompanyselection(String companyselection) {
		this.companyselection = companyselection;
	}

	public String getHearus() {
		return hearus;
	}

	public void setHearus(String hearus) {
		this.hearus = hearus;
	}

	public int getMailer() {
		return mailer;
	}

	public void setMailer(int mailer) {
		this.mailer = mailer;
	}
	
	
	/**
	 * @return the archive1
	 */
	public String getArchive1() {
		return archive1;
	}

	/**
	 * @param archive1 the archive1 to set
	 */
	public void setArchive1(String archive1) {
		this.archive1 = archive1;
	}

	/**
	 * @return the archive2
	 */
	public String getArchive2() {
		return archive2;
	}

	/**
	 * @param archive2 the archive2 to set
	 */
	public void setArchive2(String archive2) {
		this.archive2 = archive2;
	}

	/**
	 * @return the archive3
	 */
	public String getArchive3() {
		return archive3;
	}

	/**
	 * @param archive3 the archive3 to set
	 */
	public void setArchive3(String archive3) {
		this.archive3 = archive3;
	}

	/**
	 * @return the archive4
	 */
	public String getArchive4() {
		return archive4;
	}

	/**
	 * @param archive4 the archive4 to set
	 */
	public void setArchive4(String archive4) {
		this.archive4 = archive4;
	}

	/**
	 * @return the archive5
	 */
	public String getArchive5() {
		return archive5;
	}

	/**
	 * @param archive5 the archive5 to set
	 */
	public void setArchive5(String archive5) {
		this.archive5 = archive5;
	}

	/**
	 * @return the archive6
	 */
	public String getArchive6() {
		return archive6;
	}

	/**
	 * @param archive6 the archive6 to set
	 */
	public void setArchive6(String archive6) {
		this.archive6 = archive6;
	}

	/**
	 * @return the archive7
	 */
	public String getArchive7() {
		return archive7;
	}

	/**
	 * @param archive7 the archive7 to set
	 */
	public void setArchive7(String archive7) {
		this.archive7 = archive7;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("UserName :");
		sb.append(getUserName());
		sb.append("UserEmail :");
		sb.append(getUserEmail());
		sb.append("DATA");
		sb.append(getData());
		sb.append("Attachment Name");
		sb.append(getAttachmentname());
		sb.append("Phone No");
		sb.append(getPhoneno());
		return sb.toString();
	}
	
	
}

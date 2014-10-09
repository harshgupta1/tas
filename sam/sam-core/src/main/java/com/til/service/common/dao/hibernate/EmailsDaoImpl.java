package com.til.service.common.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.hibernate.entity.Emails;

public class EmailsDaoImpl extends BaseDaoImpl<Emails, Integer> implements EmailsDao {

	private static final Log log = LogFactory.getLog(EmailsDaoImpl.class);
	private static final int sinceDays = 3;

	public List<Emails> findAllUnprocessedEmails() {
		
		if (log.isDebugEnabled())
			log.debug("findAllUnprocessedEmails");
		try {
			Query query = null;
			query = getSession().getNamedQuery("emails.findAllUnprocessedEmailsSinceWhen");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1 * sinceDays);
			Date startDate = cal.getTime();
			query.setParameter(0, startDate);

			return query.list();

		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findAllUnprocessedEmails", re);
			throw re;
		}
	}

	@Override
	protected Class<Emails> getEntityClass() {
		return Emails.class;
	}
}

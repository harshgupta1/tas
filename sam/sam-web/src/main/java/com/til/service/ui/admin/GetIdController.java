/**
 * 
 */
package com.til.service.ui.admin;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.til.service.common.api.TwitterUser;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.utils.Utilities;

/**
 * @author girish.gaurav
 * 
 */
@Controller
@RequestMapping(value = "/getid")
public class GetIdController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WebsiteDao websiteDao;

	@Autowired
	private Jaxb2Marshaller jaxb2Marshaller;

	/**
	 * 
	 */
	public GetIdController() {
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	ModelMap getIdFromUsername(@RequestParam("username") String username,
			@RequestParam("wid") short websiteId, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		logger.info("fetching id for username {} and websiteId {}", username,
				websiteId);
		ModelMap result = new ModelMap();
		long id = 0;
		String socialAppName = "";
		try {
			Website website = websiteDao.get(websiteId);
			socialAppName = website.getSocialAppName();
			if (website != null && "twitter".equalsIgnoreCase(socialAppName)) {
				logger.info("trying from www.idfromuser.com");
				String resp = Utilities
						.executeGet("http://www.idfromuser.com/getID.php?service=twitter&username="
								+ username);
				if (resp != null && !resp.isEmpty()) {
					id = Long.parseLong(resp);
				}
				if (id == 0) {
					logger.info("trying from api.twitter.com");
					resp = Utilities
							.executeGet("https://api.twitter.com/1/users/show.xml?screen_name="
									+ username);
					Source source = new StreamSource(new StringReader(resp));
					TwitterUser twitterUser = (TwitterUser) jaxb2Marshaller
							.unmarshal(source);
					id = twitterUser.getId();
				}
			}
		} catch (Exception e) {
			// do nothing.
		}
		result.addAttribute("username", username);
		result.addAttribute("socialAppName", socialAppName);
		result.addAttribute("id", String.valueOf(id));
		logger.info("sending response {}", result.toString());
		return result;
	}
}

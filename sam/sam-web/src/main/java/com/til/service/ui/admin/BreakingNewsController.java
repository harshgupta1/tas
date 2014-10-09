/**
 * 
 */
package com.til.service.ui.admin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.til.service.breakingnews.BreakingNewsServiceImpl;
import com.til.service.breakingnews.IBreakingNewsService;

/**
 * @author shahan.shah
 *
 */
@Controller
public class BreakingNewsController {

	private static Logger logger = LoggerFactory.getLogger(BreakingNewsController.class);

	ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
	@Autowired
	private ApplicationContext	context; 

	@RequestMapping(value = "/publish/{siteName}", method = RequestMethod.GET)
	public void sendBreakingNews(@PathVariable("siteName" ) String siteName,
			@RequestParam(required=false) String news,HttpServletRequest request, HttpServletResponse response){
		
		IBreakingNewsService breakingNewsService = (BreakingNewsServiceImpl)context.getBean("breakingNewsServiceImpl",siteName,news);
		threadExecutor.execute(breakingNewsService); 

		response.setStatus(response.SC_NO_CONTENT);
	}


}

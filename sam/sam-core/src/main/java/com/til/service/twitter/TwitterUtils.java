/**
 * 
 */
package com.til.service.twitter;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.til.service.common.api.Accounts;
import com.til.service.common.api.Paging;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.api.PageDetail;
import com.til.service.facebook.api.Error;


/**
 * @author Shahan.Shah
 * @author Harsh.Gupta
 *
 * Ref https://dev.twitter.com/docs/error-codes-responses
 */
public class TwitterUtils {

	private static final Logger log = LoggerFactory.getLogger(TwitterUtils.class);

	public static Status updateStatus(Twitter twitter, String minifiedUrl, String url, final String message) throws TwitterException{
		String oUrl = new String(url);
		Status status=null;
		for(int i=0;i<5;i++)
		{
			try 
			{
				if(minifiedUrl != null && !"".equalsIgnoreCase(minifiedUrl))
				{
					url = minifiedUrl;
				}
				//message = message +" "+url;
				// Extra check to send breaking news messages in 2 or multiple feeds
				int msgLength = message.length();
				if(msgLength > 120)
				{
					int start = 0, end=140;
					do
					{
						// Add messages
						end = start + 140;
						if(end > msgLength)
						{
							end = msgLength;
						}
						String msg1 = message.substring(start,end);
						// Breaking News feed has | sign between messages
						if(end == msgLength)
						{
							status = twitter.updateStatus(msg1);
							log.debug("Successfully updated the status {} to twitter url {} message {}", new Object[]{status, url, msg1});
							break;
						}
						else
						{
							int index = msg1.lastIndexOf("|");
							if(index != -1)
							{
								status = twitter.updateStatus(msg1.substring(0, index));
								log.debug("Successfully updated the status {} to twitter url {} message {}", new Object[]{status, url, msg1.substring(0, index)});
							}
							else
							{
								// Only Breaking News feed has | sign between messages, others don't so just strip 120 characters from the message
								String _message = message.substring(0,120) + " " + url;
								status = twitter.updateStatus(_message);
								log.debug("Successfully updated the status {} to twitter url {} message {}", new Object[]{status, url, _message});
								break;
							}
							start += index;
						}
					}while(start <= msgLength);
				}
				else
				{
					String _message = message;
					if(url != null)
					{
						_message = message + " " + url;
					}
					status = twitter.updateStatus(_message);
					log.debug("Successfully updated the status {} to twitter url {} message {}", new Object[]{status, url, _message});
				}
			} catch (TwitterException te) {
				if(te.isCausedByNetworkIssue())
				{
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(i==4)
					{
						// Simply log and throw
						log.error("Exception in sending message {}, minimifed url {} , Original url {}  to twitter user id {} \n {}",
								new Object[]{message, minifiedUrl,oUrl, twitter.getScreenName().toString(), te});
						throw te;
					}
					continue;
				}
				// If it is not a network issue, then simply log and throw
		    	// Duplicate tweet check
			    // Corresponds with an HTTP 503 - Twitter is temporarily over capacity.
		    	if(te.getStatusCode() != 403 && !(te.getStatusCode() == 503 && te.getErrorCode() == 130) )
		    	{
		    		log.error("Exception in sending message {}, minimifed url {} , Original url {}  to twitter user id {} \n {}",
							new Object[]{message, minifiedUrl,oUrl, twitter.getScreenName().toString(), te});
		    	}
		    	throw te;
			}
			break;
		}
		return status;
	}
	
	public static Status updateStatuswithMedia(Twitter twitter,String message,File file) throws TwitterException{
		
		Status status=null;
		StatusUpdate statusupdate=new StatusUpdate(message);
		statusupdate.setMedia(file);
		try {
			
			//message = message +" "+url;
			// Extra check to send breaking news messages in 2 or multiple feeds
			int msgLength = message.length();
			if(msgLength > 120)
			{
				int start = 0, end=140;
				do
				{
					// Add messages
					end = start + 140;
					if(end > msgLength)
					{
						end = msgLength;
					}
					String msg1 = message.substring(start,end);
					// Breaking News feed has | sign between messages
					if(end == msgLength)
					{
						status = twitter.updateStatus(statusupdate);
						break;
					}
					else
					{
						int index = msg1.lastIndexOf("|");
						if(index != -1)
						{
							status = twitter.updateStatus(statusupdate);
						}
						else
						{
							// Only Breaking News feed has | sign between messages, others don't so just strip 120 characters from the message
							message = message.substring(0,120) + " ";
							status = twitter.updateStatus(statusupdate);
							break;
						}
						start += index;
					}
				}while(start <= msgLength);
			}
			else
			{
				
				status = twitter.updateStatus(statusupdate);
			}
			log.debug("Successfully updated the status {} to twitter message {}", new Object[]{statusupdate, message});
		} catch (TwitterException te) {
		    te.printStackTrace();
	    	// Duplicate tweet check
		    // Corresponds with an HTTP 503 - Twitter is temporarily over capacity.
	    	if(te.getStatusCode() != 403 && !(te.getStatusCode() == 503 && te.getErrorCode() == 130) )
	    	{
	    		log.error("Exception in sending message {}, minimifed url {} , Original url {}  to twitter user id {} \n {}",
						new Object[]{message, twitter.getScreenName().toString(), te});
	    	}
	    	throw te;
		}
		return status;
	}

	public static Accounts getTwitterAccountDetails(Website website) throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true);
    	cb.setOAuthConsumerKey(website.getSocialAppId());
    	cb.setOAuthConsumerSecret(website.getSocialApiSecret());
    	cb.setOAuthAccessToken(website.getAccessToken());
    	cb.setOAuthAccessTokenSecret(website.getAccessTokenSecret());
    	Twitter twitter =  new TwitterFactory(cb.build()).getInstance();
    	Error error=new Error();
    	Accounts accounts = new Accounts();
		PageDetail pageDetail=new PageDetail();
    	try {
			User user=twitter.showUser(twitter.getId());
			pageDetail.setId(user.getId()+"");
			pageDetail.setAccess_token(twitter.getOAuthAccessToken().getToken());
			pageDetail.setUsername(twitter.getScreenName());
			pageDetail.setName(user.getName());
			pageDetail.setPicture(checkNullConverToString(user.getProfileImageURL()));
			pageDetail.setDescription(user.getDescription());
			pageDetail.setFounded(checkNullConverToString(user.getCreatedAt()));
			pageDetail.setLikes(user.getFollowersCount());
			pageDetail.setWebsite(checkNullConverToString(user.getURL()));
			pageDetail.setShares(0);
			pageDetail.setLink(TWITTER_URL+twitter.getScreenName());
			pageDetail.setFan_count(user.getFollowersCount());
			log.debug("Suggested twitter categories {} pagedetails {}" , twitter.getSuggestedUserCategories(),pageDetail.toString());
		}catch( TwitterException e) {
			error.setMessage(e.getErrorMessage());	
			accounts.setError(error);
		//	throw e;
		}
    	catch( Exception e) {
    		
    		error.setMessage(e.getMessage());	
			accounts.setError(error);
			log.error("Exception in getting details from Twitter",e);
		}
    	//pageDetail.setPicture(user.get)
    	PageDetail[] data=new PageDetail[1];
	//	Accounts accounts = new Accounts();
		data[0] = pageDetail;
		accounts.setData(data);
		Paging paging=new Paging();
		accounts.setPaging(paging);
		return accounts ;
	}   
	
	private static String checkNullConverToString(Object object) {
		if(object!=null){
			return object.toString(); 
		}
		return null;
	}

	private static final String TWITTER_URL="http://twitter.com/";
}

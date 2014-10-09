package com.til.service.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities
{

	private static final Logger log = LoggerFactory.getLogger(Utilities.class);
	static Random randomGenerator = new Random();
	
    public Utilities()
    {
    }

    public static String executePost(String targetURL, String urlParameters) throws IOException,Exception  {
		
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
    
    public static String executeGet(String targetURL) throws SocketTimeoutException,UnknownHostException,IOException,Exception  {
		
		URL url;
		HttpURLConnection connection = null;
		StringBuilder response = new StringBuilder();
		for(int i=0;i<5;i++)
		{
			try {
				// Create connection
				url = new URL(targetURL);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(20000);
				InputStream is = null;
				// Get Response
				if (connection.getResponseCode() != 200) {
					is = connection.getErrorStream();
				}
				else
				{
					is = connection.getInputStream();
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
			}catch (SocketTimeoutException e) {
				throw e;
			}catch (UnknownHostException e) {
				throw e;
			} catch (NoRouteToHostException e) {
				try {
					// IMP: Clear response buffer
					Thread.sleep(10000);
					if(i==4)
					{
						log.error("NoRouteToHostException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				log.error("Exception executing executeGet method for {}. \n {}",targetURL,e);
				throw e;
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
			break;
		}
		return response.toString();
	}
    
    public static String[] executeGetWithResponsecode(String targetURL) throws IOException,Exception  {
		
    	String[] responseArray=new String[2];
		URL url;
		HttpURLConnection connection = null;
		StringBuilder response = new StringBuilder();
		for(int i=0;i<5;i++)
		{
			try {
				// Create connection
				url = new URL(targetURL);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(20000);
				InputStream is = null;
				// Get Response
				if (connection.getResponseCode() != 200) {
					responseArray[0] = String.valueOf(connection.getResponseCode());
					is = connection.getErrorStream();
				}
				else
				{
					is = connection.getInputStream();
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}
				rd.close();
				responseArray[0] = connection.getResponseCode()+"";
				responseArray[1] = response.toString();
				/*
				 * 503 Service Unavailable
				 * 500 Internal Server Error
				 * 404 Not Found
				 * 400 Bad Request
				 * 502 Bad Gateway
				 * 403 Forbidden
				 * 408 Request Timeout
				 */
				if("503".equals(responseArray[0]) || "500".equals(responseArray[0]) || 
						"404".equals(responseArray[0]) || "400".equals(responseArray[0]) || "502".equals(responseArray[0]) 
						|| "403".equals(responseArray[0]) || "408".equals(responseArray[0]))
				{
					// IMP: Clear response buffer
					response.delete(0, response.length());
					try {
						if("403".equals(responseArray[0]))
						{
							// Put random sleep time for this particular specific error code
							// This is done just to not put all the thread calls to facebook at the same time.
							int randomNum = randomGenerator.nextInt(5);
							Thread.sleep(10000 * randomNum);
						}
						else
						{
							Thread.sleep(10000);
						}
						if(i==4)
						{
							// Even if there is 403 exception code thrown, do not throw any error. Its been a long time monitoring for this error code
							// and we don't have sufficient way to handle this. When we check the url in browser, it works
							// 
							if(!"403".equals(responseArray[0]))
							{
								log.error("{} Exception in getting data from url {} after {} attempts",
										new Object[]{responseArray[0], targetURL,i+1});
							}
						}
						continue;
					} catch (InterruptedException e1) {
						log.error("Thread Interrupted", e1);
					}
				}else if(!"200".equals(responseArray[0]))
				{
					log.error("{} Exception in getting data from url {}",responseArray[0], targetURL);
				}
			}catch (ConnectException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(10000);
					if(i==4)
					{
						log.error("ConnectException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
				
			}catch (NoRouteToHostException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(20000);
					if(i==4)
					{
						log.error("NoRouteToHostException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
				
			}
			catch (SocketException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(5000);
					if(i==4)
					{
						log.error("SocketException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
				
			}catch (SocketTimeoutException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(5000);
					if(i==4)
					{
						log.error("SocketTimeoutException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
				
			}catch (UnknownHostException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(10000);
					if(i==4)
					{
						log.error("UnknownHostException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			} catch (SSLException e) {
				try {
					// IMP: Clear response buffer
					response.delete(0, response.length());
					Thread.sleep(5000);
					if(i==4)
					{
						log.error("SSLException in getting data from url " + targetURL + " after " + (i+1) 
										+ " attempts. Throwing exception to the caller.", e);
						throw e;
					}
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			} 
			catch (Exception e) {
				log.error("Exception executing executeGetWithResponsecode method for "+targetURL,e);
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
			break;
		}
		return responseArray;
	}
    
    public static String[] executePostWithResponseCode(String targetURL, String urlParameters) throws Exception  {
		log.trace("Executing post request for url {} with params {}", targetURL,urlParameters);
    	String[] responseArray=new String[2];
		URL url;
		HttpURLConnection connection = null;
		StringBuilder response = new StringBuilder();
		for(int i=0;i<5;i++)
		{
			try {
				// Create connection
				url = new URL(targetURL);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	
				connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
	
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				log.trace("Sending Request..");
				// Send request
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
				log.trace("Request sent, getting response..");
				// Get Response
				InputStream is = null;
				if (connection.getResponseCode() != 200) {
					responseArray[0] = String.valueOf(connection.getResponseCode());
					is = connection.getErrorStream();
				}
				else
				{
					is = connection.getInputStream();
				}
				log.trace("Response Code {}, reading inputstream",connection.getResponseCode());
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
				responseArray[0] = connection.getResponseCode()+"";
				responseArray[1] = response.toString();
				log.trace("BufferedReader Closed, response is {}",responseArray);
				if("500".equals(responseArray[0]) || "400".equals(responseArray[0]))
				{
					// IMP: Clear response buffer
					response.delete(0, response.length());
					try {
						Thread.sleep(5000);
						if(i==4)
						{
							log.error("{} Exception in posting data to url {} with params {} after {} attempts",
									new Object[]{responseArray[0], targetURL,urlParameters,i+1});
						}
						continue;
					} catch (InterruptedException e1) {
						log.error("Thread Interrupted", e1);
					}
				}else if(!"200".equals(responseArray[0]))
				{
					log.error("{} Exception in posting data to url {} with params {}, message {}",
							new String[]{responseArray[0], targetURL,urlParameters, responseArray[1]});
				}
	
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
			break;
		}
		return responseArray;
	}
    
    public static String[] executePostMultipartWithResponseCode(String targetURL, File file, String urlParameters) throws Exception  {
		
    	int status = 0;
    	String[] responseArray=new String[2];
		for(int i=0;i<5;i++)
		{
			try {
				PostMethod filePost = new PostMethod(targetURL);
				String param[] = urlParameters.split("&");
				Part[] parts = new Part[param.length + 1];
				for (int j = 0; j < param.length; j++) {
					String kv[] = param[j].split("=");
					parts[j] = new StringPart(kv[0], URLDecoder.decode(kv[1]));
				}
				// Add last parameter as file parameter
				parts[parts.length-1]=   new FilePart(file.getName(), file);
		    	filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
		    	HttpClient client = new HttpClient();
		    	status = client.executeMethod(filePost);
		    	System.out.println(filePost.getResponseBodyAsString()); //{"id":"10201728283116040","post_id":"1449503695_10201727683981062"}
		    	responseArray[0] = status+"";
				responseArray[1] = filePost.getResponseBodyAsString();
				if("500".equals(status+"") || "400".equals(status+""))
				{
					// IMP: Clear response buffer
					try {
						Thread.sleep(5000);
						if(i==4)
						{
							log.error("{} Exception in posting data to url {} with params {} after {} attempts",
									new Object[]{status, targetURL,urlParameters,i+1});
						}
						continue;
					} catch (InterruptedException e1) {
						log.error("Thread Interrupted", e1);
					}
				}else if(!"200".equals(status+""))
				{
					log.error("{} Exception in posting data to url {} with params {}, message {}",
							new String[]{status+"", targetURL,urlParameters, status+""});
				}
	
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} 
			break;
		}
		return responseArray;
	}

    /*public static String executeDelete(String targetURL, String urlParameters) throws IOException,Exception  {
		
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.connect();
			int responseCode = connection.getResponseCode();
			String response = connection.getResponseMessage();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception executing executeDelete method for {}. \n {}",targetURL,e);
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}*/

    public static int getResponseCode(String targetURL, StringBuffer redirectURL) throws IOException,Exception  {
		
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(20000);
			connection.setInstanceFollowRedirects(false);
			String location = connection.getHeaderField("location");
	        if (location != null)
	        {
	            log.debug("Redirected to {}",location);
	            redirectURL.append(location);
	        }
			// Get Response Code
			return connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception executing getResponseCode method for {}. \n {}",targetURL,e);
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

    public static String stripJsessionId(String url)
    {
        int startPos = url.indexOf(";jsessionid=");
        if(startPos != -1)
        {
            int endPos = url.indexOf("?", startPos);
            if(endPos == -1)
                url = url.substring(0, startPos);
            else
                url = (new StringBuilder(String.valueOf(url.substring(0, startPos)))).append(url.substring(endPos, url.length())).toString();
        }
        return url;
    }
    
    public static String escapeXML(String str) {
        return StringEscapeUtils.escapeXml(str);
    }
    
    public static String unescapeXML(String str) {
        return StringEscapeUtils.unescapeXml(str);
    }
    
    public static String escapeHTML(String s)
    {
        return escapeHTML(s, true);
    }

    public static String escapeHTML(String s, boolean escapeAmpersand)
    {
        if(escapeAmpersand)
            s = StringUtils.replace(s, "&", "&amp;");
        s = StringUtils.replace(s, "&nbsp;", " ");
        s = StringUtils.replace(s, "\"", "&quot;");
        s = StringUtils.replace(s, "<", "&lt;");
        s = StringUtils.replace(s, ">", "&gt;");
        return s;
    }

    public static String unescapeHTML(String str)
    {
        return StringEscapeUtils.unescapeHtml(str);
    }

    public static String removeHTML(String str)
    {
        return removeHTML(str, true);
    }

    public static String removeHTML(String str, boolean addSpace)
    {
        if(str == null)
            return "";
        StringBuilder ret = new StringBuilder(str.length());
        int start = 0;
        int beginTag = str.indexOf("<");
        int endTag = 0;
        if(beginTag == -1)
            return str;
        while(beginTag >= start) 
        {
            if(beginTag > 0)
            {
                ret.append(str.substring(start, beginTag));
                if(addSpace)
                    ret.append(" ");
            }
            endTag = str.indexOf(">", beginTag);
            if(endTag > -1)
            {
                start = endTag + 1;
                beginTag = str.indexOf("<", start);
                continue;
            }
            ret.append(str.substring(beginTag));
            break;
        }
        if(endTag > -1 && endTag + 1 < str.length())
            ret.append(str.substring(endTag + 1));
        return ret.toString().trim();
    }

    public static String removeAndEscapeHTML(String s)
    {
        if(s == null)
            return "";
        else
            return escapeHTML(removeHTML(s));
    }

    public static String autoformat(String s)
    {
        String ret = StringUtils.replace(s, "\n", "<br />");
        return ret;
    }

    public static String addNofollow(String html)
    {
        if(html == null || html.length() == 0)
            return html;
        Matcher m = mLinkPattern.matcher(html);
        StringBuilder buf = new StringBuilder();
        for(; m.find(); m = mLinkPattern.matcher(html))
        {
            int start = m.start();
            int end = m.end();
            String link = html.substring(start, end);
            buf.append(html.substring(0, start));
            if(link.indexOf("rel=\"nofollow\"") == -1)
                buf.append((new StringBuilder(String.valueOf(link.substring(0, link.length() - 1)))).append(" rel=\"nofollow\">").toString());
            else
                buf.append(link);
            html = html.substring(end, html.length());
        }

        buf.append(html);
        return buf.toString();
    }

    public static String replaceNonAlphanumeric(String str)
    {
        return replaceNonAlphanumeric(str, '_');
    }

    public static String replaceNonAlphanumeric(String str, char subst)
    {
    	StringBuilder ret = new StringBuilder(str.length());
        char testChars[] = str.toCharArray();
        for(int i = 0; i < testChars.length; i++)
            if(Character.isLetterOrDigit(testChars[i]))
                ret.append(testChars[i]);
            else
                ret.append(subst);

        return ret.toString();
    }

    public static String removeNonAlphanumeric(String str)
    {
    	StringBuilder ret = new StringBuilder(str.length());
        char testChars[] = str.toCharArray();
        for(int i = 0; i < testChars.length; i++)
            if(Character.isLetterOrDigit(testChars[i]) || testChars[i] == '.')
                ret.append(testChars[i]);

        return ret.toString();
    }
    
    public static String getSummaryFromNS(String ns)
    {
    	int iPos = ns.indexOf("summary=");
    	if(iPos > 0)
    	{
    		return ns.substring(iPos + 8);
    	}
    	return "";
    }
    
    public static String getTitleFromNS(String ns)
    {
    	int iPos = ns.indexOf("title=");
    	if(iPos >= 0)
    	{
    		int iSepPos = ns.indexOf(";author=");
    		if(iSepPos > 0)
    			return ns.substring(iPos + 6,iSepPos);
    	}
    	return "";
    }
    
    public static String getStringFromArray(Object object[], int index)
    {
        return (String)object[index];
    }
    
    public static Date getDateFromArray(Object object[], int index)
    {
        return (Date)object[index];
    }
    
    public static String stringArrayToString(String stringArray[], String delim)
    {
        String ret = "";
        for(int i = 0; i < stringArray.length; i++)
            if(ret.length() > 0)
                ret = (new StringBuilder(String.valueOf(ret))).append(delim).append(stringArray[i]).toString();
            else
                ret = stringArray[i];

        return ret;
    }

    public static String[] stringToStringArray(String instr, String delim)
        throws NoSuchElementException, NumberFormatException
    {
        StringTokenizer toker = new StringTokenizer(instr, delim);
        String stringArray[] = new String[toker.countTokens()];
        int i = 0;
        while(toker.hasMoreTokens()) 
            stringArray[i++] = toker.nextToken();
        return stringArray;
    }

    public static int[] stringToIntArray(String instr, String delim)
        throws NoSuchElementException, NumberFormatException
    {
        StringTokenizer toker = new StringTokenizer(instr, delim);
        int intArray[] = new int[toker.countTokens()];
        int i = 0;
        while(toker.hasMoreTokens()) 
        {
            String sInt = toker.nextToken();
            int nInt = Integer.parseInt(sInt);
            intArray[i++] = (new Integer(nInt)).intValue();
        }
        return intArray;
    }

    public static String intArrayToString(int intArray[])
    {
        String ret = "";
        for(int i = 0; i < intArray.length; i++)
            if(ret.length() > 0)
                ret = (new StringBuilder(String.valueOf(ret))).append(",").append(Integer.toString(intArray[i])).toString();
            else
                ret = Integer.toString(intArray[i]);

        return ret;
    }

    public static void copyFile(File from, File to)
        throws IOException
    {
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = new FileInputStream(from);
        }
        catch(IOException ex)
        {
            throw new IOException((new StringBuilder("Utilities.copyFile: opening input stream '")).append(from.getPath()).append("', ").append(ex.getMessage()).toString());
        }
        try
        {
            out = new FileOutputStream(to);
        }
        catch(Exception ex)
        {
            try
            {
                in.close();
            }
            catch(IOException ioexception) { }
            throw new IOException((new StringBuilder("Utilities.copyFile: opening output stream '")).append(to.getPath()).append("', ").append(ex.getMessage()).toString());
        }
        copyInputToOutput(in, out, from.length());
    }

    public static void copyInputToOutput(InputStream input, OutputStream output, long byteCount)
        throws IOException
    {
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        byte buffer[] = new byte[8192];
        for(long length = byteCount; length > 0L;)
        {
            int bytes = (int)(length <= 8192L ? length : 8192L);
            try
            {
                bytes = in.read(buffer, 0, bytes);
            }
            catch(IOException ex)
            {
                try
                {
                    in.close();
                    out.close();
                }
                catch(IOException ioexception) { }
                throw new IOException((new StringBuilder("Reading input stream, ")).append(ex.getMessage()).toString());
            }
            if(bytes < 0)
                break;
            length -= bytes;
            try
            {
                out.write(buffer, 0, bytes);
            }
            catch(IOException ex)
            {
                try
                {
                    in.close();
                    out.close();
                }
                catch(IOException ioexception1) { }
                throw new IOException((new StringBuilder("Writing output stream, ")).append(ex.getMessage()).toString());
            }
        }

        try
        {
            in.close();
            out.close();
        }
        catch(IOException ex)
        {
            throw new IOException((new StringBuilder("Closing file streams, ")).append(ex.getMessage()).toString());
        }
    }

    public static void copyInputToOutput(InputStream input, OutputStream output)
        throws IOException
    {
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        byte buffer[] = new byte[8192];
        for(int count = 0; count != -1;)
        {
            count = in.read(buffer, 0, 8192);
            if(count != -1)
                out.write(buffer, 0, count);
        }

        try
        {
            in.close();
            out.close();
        }
        catch(IOException ex)
        {
            throw new IOException((new StringBuilder("Closing file streams, ")).append(ex.getMessage()).toString());
        }
    }

    public static String encodePassword(String password, String algorithm)
    {
        byte unencodedPassword[] = password.getBytes();
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance(algorithm);
        }
        catch(Exception e)
        {
            log.error("Excepting getting MessageDigest instance using algorithm {}", algorithm , e);
            return password;
        }
        md.reset();
        md.update(unencodedPassword);
        byte encodedPassword[] = md.digest();
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < encodedPassword.length; i++)
        {
            if((encodedPassword[i] & 0xff) < 16)
                buf.append("0");
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }

    public static String truncate(String str, int lower, int upper, String appendToEnd)
    {
        String str2 = removeHTML(str, false);
        if(upper < lower)
            upper = lower;
        if(str2.length() > upper)
        {
            int loc = str2.lastIndexOf(' ', upper);
            if(loc >= lower)
            {
                str2 = str2.substring(0, loc);
            } else
            {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            str2 = (new StringBuilder(String.valueOf(str2))).append(appendToEnd).toString();
        }
        return str2;
    }

    public static String truncateNicely(String str, int lower, int upper, String appendToEnd)
    {
        String str2 = removeHTML(str, false);
        boolean diff = str2.length() < str.length();
        if(upper < lower)
            upper = lower;
        if(str2.length() > upper)
        {
            int loc = str2.lastIndexOf(' ', upper);
            if(loc >= lower)
            {
                str2 = str2.substring(0, loc);
            } else
            {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            if(diff)
            {
                loc = str2.lastIndexOf(' ', loc);
                String str3 = str2.substring(loc + 1);
                loc = str.indexOf(str3, loc) + str3.length();
                str2 = str.substring(0, loc);
                str3 = extractHTML(str.substring(loc));
                str = (new StringBuilder(String.valueOf(str2))).append(appendToEnd).append(str3).toString();
            } else
            {
                str = (new StringBuilder(String.valueOf(str2))).append(appendToEnd).toString();
            }
        }
        return str;
    }

    public static String truncateText(String str, int lower, int upper, String appendToEnd)
    {
        String str2 = removeHTML(str, false);
        //boolean diff = str2.length() < str.length();
        if(upper < lower)
            upper = lower;
        if(str2.length() > upper)
        {
            int loc = str2.lastIndexOf(' ', upper);
            if(loc >= lower)
            {
                str2 = str2.substring(0, loc);
            } else
            {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            str = (new StringBuilder(String.valueOf(str2))).append(appendToEnd).toString();
        }
        return str;
    }

    private static String stripLineBreaks(String str)
    {
        str = str.replaceAll("<br>", "");
        str = str.replaceAll("<br/>", "");
        str = str.replaceAll("<br />", "");
        str = str.replaceAll("<p></p>", "");
        str = str.replaceAll("<p/>", "");
        str = str.replaceAll("<p />", "");
        return str;
    }

    private static String removeVisibleHTMLTags(String str)
    {
        str = stripLineBreaks(str);
        StringBuilder result = new StringBuilder(str);
        StringBuilder lcresult = new StringBuilder(str.toLowerCase());
        String visibleTags[] = {
            "<img"
        };
        for(int j = 0; j < visibleTags.length; j++)
        {
            int stringIndex;
            while((stringIndex = lcresult.indexOf(visibleTags[j])) != -1) 
                if(visibleTags[j].endsWith(">"))
                {
                    result.delete(stringIndex, stringIndex + visibleTags[j].length());
                    lcresult.delete(stringIndex, stringIndex + visibleTags[j].length());
                } else
                {
                    int endIndex = result.indexOf(">", stringIndex);
                    if(endIndex > -1)
                    {
                        result.delete(stringIndex, endIndex + 1);
                        lcresult.delete(stringIndex, endIndex + 1);
                    }
                }
        }

        String openCloseTags[] = {
            "li", "a", "div", "h1", "h2", "h3", "h4"
        };
        for(int j = 0; j < openCloseTags.length; j++)
        {
            String closeTag = (new StringBuilder("</")).append(openCloseTags[j]).append(">").toString();
            int lastStringIndex = 0;
            int stringIndex;
            while((stringIndex = lcresult.indexOf((new StringBuilder("<")).append(openCloseTags[j]).toString(), lastStringIndex)) > -1) 
            {
                lastStringIndex = stringIndex;
                int endIndex = lcresult.indexOf(closeTag, stringIndex);
                if(endIndex > -1)
                {
                    result.delete(stringIndex, endIndex + closeTag.length());
                    lcresult.delete(stringIndex, endIndex + closeTag.length());
                } else
                {
                    endIndex = lcresult.indexOf(">", stringIndex);
                    int nextStart = lcresult.indexOf("<", stringIndex + 1);
                    if(endIndex > stringIndex && lcresult.charAt(endIndex - 1) == '/' && (endIndex < nextStart || nextStart == -1))
                    {
                        result.delete(stringIndex, endIndex + 1);
                        lcresult.delete(stringIndex, endIndex + 1);
                    }
                }
            }
        }

        return result.toString();
    }

    public static String extractHTML(String str)
    {
        if(str == null)
            return "";
        StringBuilder ret = new StringBuilder(str.length());
        int start = 0;
        int beginTag = str.indexOf("<");
        int endTag = 0;
        if(beginTag == -1)
            return str;
        for(; beginTag >= start; beginTag = str.indexOf("<", start))
        {
            endTag = str.indexOf(">", beginTag);
            if(endTag <= -1)
                break;
            ret.append(str.substring(beginTag, endTag + 1));
            start = endTag + 1;
        }

        return ret.toString();
    }

    public static final String encode(String s)
    {
        try
        {
            if(s != null)
            {
                URLEncoder.encode(s, "UTF-8");
                return s;
            }
        }
        catch(UnsupportedEncodingException e)
        {
            return s;
        }
        return s;
    }

    public static final String decode(String s)
    {
        try
        {
            if(s != null)
                return URLDecoder.decode(s, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            return s;
        }
        catch(IllegalArgumentException e)
        {
        	//log.warn("This String '" + s + "' is not URL encoded.");
        	return s;
        }
        return s;
    }

    public static int stringToInt(String string)
    {
        try
        {
            return Integer.valueOf(string).intValue();
        }
        catch(NumberFormatException e)
        {
            log.debug((new StringBuilder("Invalid Integer:")).append(string).toString());
        }
        return 0;
    }

    public static String toBase64(byte aValue[])
    {
        String m_strBase64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        int iByteLen = aValue.length;
        StringBuilder tt = new StringBuilder();
        for(int i = 0; i < iByteLen; i += 3)
        {
            boolean bByte2 = i + 1 < iByteLen;
            boolean bByte3 = i + 2 < iByteLen;
            int byte1 = aValue[i] & 0xff;
            int byte2 = bByte2 ? aValue[i + 1] & 0xff : 0;
            int byte3 = bByte3 ? aValue[i + 2] & 0xff : 0;
            tt.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(byte1 / 4));
            tt.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(byte2 / 16 + (byte1 & 3) * 16));
            tt.append(bByte2 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(byte3 / 64 + (byte2 & 0xf) * 4) : '=');
            tt.append(bByte3 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(byte3 & 0x3f) : '=');
        }

        return tt.toString();
    }

    public static String stripInvalidTagCharacters(String tag)
    {
        if(tag == null)
            throw new NullPointerException();
        StringBuilder sb = new StringBuilder();
        char charArray[] = tag.toCharArray();
        for(int i = 0; i < charArray.length;)
        {
            char c = charArray[i];
            switch(c)
            {
            default:
                if('!' <= c && c <= '~' || Character.isUnicodeIdentifierPart(c) || Character.isUnicodeIdentifierStart(c))
                    sb.append(charArray[i]);
                // fall through

            case 34: // '"'
            case 44: // ','
                i++;
                break;
            }
        }

        return sb.toString();
    }

    public static String normalizeTag(String tag, Locale locale)
    {
        tag = stripInvalidTagCharacters(tag);
        return locale != null ? tag.toLowerCase(locale) : tag.toLowerCase();
    }

    @SuppressWarnings("rawtypes")
	public static List splitStringAsTags(String tags)
    {
        String tagsarr[] = StringUtils.split(tags, " ,\n\r\f\t");
        if(tagsarr == null)
            return Collections.EMPTY_LIST;
        else
            return Arrays.asList(tagsarr);
    }

    public static String transformToHTMLSubset(String s)
    {
        if(s == null)
            return null;
        s = replace(s, OPENING_B_TAG_PATTERN, "<b>");
        s = replace(s, CLOSING_B_TAG_PATTERN, "</b>");
        s = replace(s, OPENING_I_TAG_PATTERN, "<i>");
        s = replace(s, CLOSING_I_TAG_PATTERN, "</i>");
        s = replace(s, OPENING_BLOCKQUOTE_TAG_PATTERN, "<blockquote>");
        s = replace(s, CLOSING_BLOCKQUOTE_TAG_PATTERN, "</blockquote>");
        s = replace(s, BR_TAG_PATTERN, "<br />");
        s = replace(s, OPENING_P_TAG_PATTERN, "<p>");
        s = replace(s, CLOSING_P_TAG_PATTERN, "</p>");
        s = replace(s, OPENING_PRE_TAG_PATTERN, "<pre>");
        s = replace(s, CLOSING_PRE_TAG_PATTERN, "</pre>");
        s = replace(s, OPENING_UL_TAG_PATTERN, "<ul>");
        s = replace(s, CLOSING_UL_TAG_PATTERN, "</ul>");
        s = replace(s, OPENING_OL_TAG_PATTERN, "<ol>");
        s = replace(s, CLOSING_OL_TAG_PATTERN, "</ol>");
        s = replace(s, OPENING_LI_TAG_PATTERN, "<li>");
        s = replace(s, CLOSING_LI_TAG_PATTERN, "</li>");
        s = replace(s, QUOTE_PATTERN, "\"");
        s = replace(s, CLOSING_A_TAG_PATTERN, "</a>");
        for(Matcher m = OPENING_A_TAG_PATTERN.matcher(s); m.find(); m = OPENING_A_TAG_PATTERN.matcher(s))
        {
            int start = m.start();
            int end = m.end();
            String link = s.substring(start, end);
            link = (new StringBuilder("<")).append(link.substring(4, link.length() - 4)).append(">").toString();
            s = (new StringBuilder(String.valueOf(s.substring(0, start)))).append(link).append(s.substring(end, s.length())).toString();
        }

        s = s.replaceAll("&amp;lt;", "&lt;");
        s = s.replaceAll("&amp;gt;", "&gt;");
        s = s.replaceAll("&amp;#", "&#");
        return s;
    }

    private static String replace(String string, Pattern pattern, String replacement)
    {
        Matcher m = pattern.matcher(string);
        return m.replaceAll(replacement);
    }
    
    /**
     * This method ensures that the output String has only
     * valid XML unicode characters as specified by the
     * XML 1.0 standard. For reference, please see
     * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty
     * String if the input is null or empty.
     *
     * @param in The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public String stripNonValidXMLCharacters(String in) {
    	StringBuilder out = new StringBuilder(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }    
    
    public static String byteToHex(byte b) {
	    // Returns hex String representation of byte b
	    char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        'a', 'b', 'c', 'd', 'e', 'f' };
	    char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
	    return new String(array);
	}
    
    public static String getUnicodeValue(char c)
    {
		byte hi = (byte) (c >>> 8);
	    byte lo = (byte) (c & 0xff);
	    return (byteToHex(hi) + byteToHex(lo));
    }
    
    public static String cleanArticleTitle(String title)
    {
    	title = title.trim().replaceAll("-", "");
    	title = title.replaceAll("[  ]", "-");
    	title = title.replaceAll("[ ]", "-");
		return title.replaceAll("[^a-zA-Z0-9-/]*", "");
    }
    
    /**
     * This function is used to get the expired date of an accesstoken  
     * 
     */
    public static Date expiresAt(int expires)
    {
    	int expiresIn = Math.round(expires / (60*60*24));
    	return DateUtil.getDaysDiff(expiresIn);
    }
    
    public static final String TAG_SPLIT_CHARS = " ,\n\r\f\t";
    private static Pattern mLinkPattern = Pattern.compile("<a href=.*?>", 2);
    private static final Pattern OPENING_B_TAG_PATTERN = Pattern.compile("&lt;b&gt;", 2);
    private static final Pattern CLOSING_B_TAG_PATTERN = Pattern.compile("&lt;/b&gt;", 2);
    private static final Pattern OPENING_I_TAG_PATTERN = Pattern.compile("&lt;i&gt;", 2);
    private static final Pattern CLOSING_I_TAG_PATTERN = Pattern.compile("&lt;/i&gt;", 2);
    private static final Pattern OPENING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;blockquote&gt;", 2);
    private static final Pattern CLOSING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;/blockquote&gt;", 2);
    private static final Pattern BR_TAG_PATTERN = Pattern.compile("&lt;br */*&gt;", 2);
    private static final Pattern OPENING_P_TAG_PATTERN = Pattern.compile("&lt;p&gt;", 2);
    private static final Pattern CLOSING_P_TAG_PATTERN = Pattern.compile("&lt;/p&gt;", 2);
    private static final Pattern OPENING_PRE_TAG_PATTERN = Pattern.compile("&lt;pre&gt;", 2);
    private static final Pattern CLOSING_PRE_TAG_PATTERN = Pattern.compile("&lt;/pre&gt;", 2);
    private static final Pattern OPENING_UL_TAG_PATTERN = Pattern.compile("&lt;ul&gt;", 2);
    private static final Pattern CLOSING_UL_TAG_PATTERN = Pattern.compile("&lt;/ul&gt;", 2);
    private static final Pattern OPENING_OL_TAG_PATTERN = Pattern.compile("&lt;ol&gt;", 2);
    private static final Pattern CLOSING_OL_TAG_PATTERN = Pattern.compile("&lt;/ol&gt;", 2);
    private static final Pattern OPENING_LI_TAG_PATTERN = Pattern.compile("&lt;li&gt;", 2);
    private static final Pattern CLOSING_LI_TAG_PATTERN = Pattern.compile("&lt;/li&gt;", 2);
    private static final Pattern CLOSING_A_TAG_PATTERN = Pattern.compile("&lt;/a&gt;", 2);
    private static final Pattern OPENING_A_TAG_PATTERN = Pattern.compile("&lt;a href=.*?&gt;", 2);
    private static final Pattern QUOTE_PATTERN = Pattern.compile("&quot;", 2);
    
}

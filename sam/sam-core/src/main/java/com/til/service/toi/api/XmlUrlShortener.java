package com.til.service.toi.api;

import java.io.StringReader;
import java.net.SocketTimeoutException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.til.service.common.api.TOIMinifiedURL;
import com.til.service.toi.UrlShortener;
import com.til.service.utils.Utilities;

@Service("xmlUrlShortener")
public class XmlUrlShortener implements UrlShortener {
	
	private static final Logger log = LoggerFactory.getLogger(XmlUrlShortener.class);
	
	@Autowired
	private Jaxb2Marshaller jaxb2Marshaller;
	
	@Override
	public TOIMinifiedURL getMinifiedURL(String url) {
		TOIMinifiedURL toiMinifiedURL = null;
		for(int i=0;i<5;i++)
		{
			try
			{
				String data = Utilities.executeGet(url);
				Source source = new StreamSource(new StringReader(data));
				toiMinifiedURL = (TOIMinifiedURL) jaxb2Marshaller.unmarshal(source);
			}catch (SocketTimeoutException e) {
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("SocketTimeoutException in shortening url {} after {} attempts \n Exception {}",
								new Object[]{url,i+1,e});
					}
					// If SocketTimeoutException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(UnmarshallingFailureException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("UnmarshallingFailureException in shortening url {} after {} attempts \n Exception {}",
								new Object[]{url,i+1,e});
					}
					// If UnmarshallingFailureException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(Exception e)
			{
				log.error("Error parsing data from url {} shortener api \n Error {}", url, e);
			}
			break;
		}
		return toiMinifiedURL;
	}

	public Jaxb2Marshaller getJaxb2Marshaller() {
		return jaxb2Marshaller;
	}

	public void setJaxb2Marshaller(Jaxb2Marshaller jaxb2Marshaller) {
		this.jaxb2Marshaller = jaxb2Marshaller;
	}
	
	
}

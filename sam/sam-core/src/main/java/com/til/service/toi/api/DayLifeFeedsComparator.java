package com.til.service.toi.api;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.toi.api.DayLifeFeeds.Item;


/*
User defined Java comparator.
To create custom java comparator, implement Comparator interface and
define compare method.
 
The below given comparator compares feeditems on the basis of their timestamp_epoch field.
*/
public class DayLifeFeedsComparator implements Comparator{
	
	/** Logger for this class and subclasses */
    protected final Logger log = LoggerFactory.getLogger(getClass());
	
	public int compare(Object item1, Object item2){
		   
        /*
         * parameter are of type Object, so we have to downcast it
         * to Item objects
         */
	    try
	    {
	        long item1TimeStamp = ((Item)item1).getTimestamp_epoch();        
	        long item2TimeStamp = ((Item)item2).getTimestamp_epoch();
	       
	        if(item1TimeStamp < item2TimeStamp)
	            return 1;
	        else if(item1TimeStamp > item2TimeStamp)
	            return -1;
	        else
	            return 0;    
	    }
	    catch(Exception e)
	    {
	    	log.error("Exception while comparing {} and {}",new Object[]{item1,item2,e});
	    	return 0;
	    }
    }
}

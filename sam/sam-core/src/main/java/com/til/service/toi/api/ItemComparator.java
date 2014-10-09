package com.til.service.toi.api;

import java.util.Comparator;

import net.sf.json.JSONObject;


public class ItemComparator<ChannelItem> implements Comparator<ChannelItem> {

	private JSONObject dataObject;

	
	public ItemComparator(JSONObject dataObject){
		this.dataObject=dataObject;		
	}
	
	@Override
	public int compare(ChannelItem channelItem1 , ChannelItem channelItem2 ) {
		long clic1;
		long clic2;
		com.til.service.common.api.Rss2Feeds.ChannelItem item1 = (com.til.service.common.api.Rss2Feeds.ChannelItem) channelItem1;
		com.til.service.common.api.Rss2Feeds.ChannelItem item2 = (com.til.service.common.api.Rss2Feeds.ChannelItem) channelItem2; 
		clic1 = getClicks(item1);
		clic2 = getClicks(item2);

		 return (clic1>clic2 ? -1 : (clic1==clic2 ? 0 : 1));
	}

	private long getClicks(com.til.service.common.api.Rss2Feeds.ChannelItem item) {
		long clicks = 0;
		try {
			if(item.getArticleid()!=null){
				clicks = dataObject.getJSONObject(item.getArticleid()).getLong("hourlyClicks");
			}else{
				clicks = dataObject.getJSONObject(item.getUrl().substring(item.getUrl().lastIndexOf("/")+1,item.getUrl().lastIndexOf("."))).getLong("hourlyClicks");
			}
				
		} catch (Exception e) {
			clicks = -1;  				
		}
		return clicks;
	}


}

package com.til.service.common.menu;

import java.util.ArrayList;
import java.util.List;


/**
 * A parsed xml defined menu.
 */
public class ParsedMenu {
    
    private List<ParsedTab> tabs = new ArrayList<ParsedTab>();
    
    
    public void addTab(ParsedTab tab) {
        this.tabs.add(tab);
    }
    
    
    public List<ParsedTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<ParsedTab> tabs) {
        this.tabs = tabs;
    }
    
}
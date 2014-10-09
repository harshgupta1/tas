package com.til.service.common.menu;

import java.util.ArrayList;
import java.util.List;


/**
 * A Menu of MenuTab objects.
 */
public class Menu {
    
    private List<MenuTab> tabs = new ArrayList<MenuTab>();
    
    
    public void addTab(MenuTab tab) {
        this.tabs.add(tab);
    }
    
    
    public List<MenuTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<MenuTab> menus) {
        this.tabs = menus;
    }
    
}

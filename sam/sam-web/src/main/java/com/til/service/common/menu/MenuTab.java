package com.til.service.common.menu;

import java.util.ArrayList;
import java.util.List;


/**
 * Tab in a Menu.
 */
public class MenuTab {
    
    private String key = null;
    private String action = null;
    private boolean selected = false;
    private List<MenuTabItem> items = new ArrayList<MenuTabItem>();
    
    
    public void addItem(MenuTabItem item) {
        this.items.add(item);
    }
    
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String url) {
        this.action = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public List<MenuTabItem> getItems() {
        return items;
    }

    public void setItems(List<MenuTabItem> items) {
        this.items = items;
    }
    
}

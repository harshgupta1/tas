package com.til.service.common.menu;

/**
 * Items in a Tab.
 */
public class MenuTabItem {
    
    private String key = null;
    private String action = null;
    private boolean selected = false;
    
    
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
    
}


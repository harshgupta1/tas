package com.til.service.common.menu;

import java.util.ArrayList;
import java.util.List;


/**
 * A parsed "tab" from an xml defined menu config.
 */
public class ParsedTab {
    
    private String name = null;
    private String perm = null;
    private String role = null;
    private String enabledProperty = null;
    private String disabledProperty = null;
    
    private List<ParsedTabItem> tabItems = new ArrayList<ParsedTabItem>();
    
    
    public void addItem(ParsedTabItem item) {
        this.tabItems.add(item);
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEnabledProperty() {
        return enabledProperty;
    }

    public void setEnabledProperty(String enabledProperty) {
        this.enabledProperty = enabledProperty;
    }

    public List<ParsedTabItem> getTabItems() {
        return tabItems;
    }

    public void setTabItems(List<ParsedTabItem> tabItems) {
        this.tabItems = tabItems;
    }

    public String getDisabledProperty() {
        return disabledProperty;
    }

    public void setDisabledProperty(String disabledProperty) {
        this.disabledProperty = disabledProperty;
    }
    
}

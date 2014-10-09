package com.til.service.common.menu;

/**
 * A parsed "tab-item" from an xml defined menu config.
 */
public class ParsedTabItem {
    
    private String name = null;
    private String action = null;
    private String[] subActions = null;
    private String perm = null;
    private String role = null;
    private String enabledProperty = null;
    private String disabledProperty = null;
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getSubActions() {
        return subActions;
    }

    public void setSubActions(String[] subActions) {
        this.subActions = subActions;
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

    public String getDisabledProperty() {
        return disabledProperty;
    }

    public void setDisabledProperty(String disabledProperty) {
        this.disabledProperty = disabledProperty;
    }
    
}


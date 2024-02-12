package com.demo.example.dailyincomeexpensemanager.bean;


public class TemplateBean {

    
    private String f95id;
    private String template = "";
    private String accountRef = "";

    public String getId() {
        return this.f95id;
    }

    public void setId(String str) {
        this.f95id = str;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String str) {
        this.template = str;
    }

    public String getAccountRef() {
        return this.accountRef;
    }

    public void setAccountRef(String str) {
        this.accountRef = str;
    }

    public String toString() {
        return "" + this.template;
    }
}

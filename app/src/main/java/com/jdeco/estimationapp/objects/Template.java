package com.jdeco.estimationapp.objects;

import java.util.ArrayList;

public class Template {
    String templateId;
    String templateName;
    String templateDesc;
    String phase_type;
    String meter_type;
    String appId;


    public String getPhase_type() {
        return phase_type;
    }

    public void setPhase_type(String phase_type) {
        this.phase_type = phase_type;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    ArrayList<Item> templateList = new ArrayList<>();
    int templateAmount=1;

    public int getTemplateAmount() {
        return templateAmount;
    }

    public void setTemplateAmount(int templateAmount) {
        this.templateAmount = templateAmount;
    }

    public Template(String templateId, String templateName, String templateDesc) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateDesc = templateDesc;
    }
    public Template() {
        this.templateId = "";
        this.templateName = "";
        this.templateDesc = "";
    }

    public ArrayList<Item> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(ArrayList<Item> templateList) {
        this.templateList = templateList;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public int incressAmount(){
        templateAmount+=1;
        return templateAmount;
    }

    public int decressAmount(){
        templateAmount-=1;
        if(templateAmount<1)templateAmount= 1;
        return templateAmount;
    }




}

package com.jdeco.estimationapp.objects;

public class AttchmentType {
    String text;
    String code;

    public AttchmentType(){

    }

    public AttchmentType(String text , String code){
        this.text = text;
        this.code = code;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return text;
    }
}

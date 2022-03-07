package com.jdeco.estimationapp.objects;

public class Image {

    AttchmentType attachmentType;

    String filename;
    String appRowId;
    String username;
    String file;


 

    public AttchmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttchmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAppRowId() {
        return appRowId;
    }

    public void setAppRowId(String appRowId) {
        this.appRowId = appRowId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}

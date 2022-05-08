package com.jdeco.estimationapp.objects;

public class Image {

    AttchmentType attachmentType;
    String fileName;
    String appRowId;
    String username;
    String file;
    int isSync = 0;


    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public AttchmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttchmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

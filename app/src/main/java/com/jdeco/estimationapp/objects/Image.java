package com.jdeco.estimationapp.objects;

public class Image {
int imageID ;
    String imageName;
    AttchmentType attachmentType;
    String fileName;
    String appRowId;
    String username;
    String imagePath;
    String file;
    int isSync = 0;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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

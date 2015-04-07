package com.gs.thrift.model;



public class GenThriftForm {

    private String filename;
    private String language;
    private String thriftVersion;
    private String genFrame;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getThriftVersion() {
        return thriftVersion;
    }

    public void setThriftVersion(String thriftVersion) {
        this.thriftVersion = thriftVersion;
    }

    public String getGenFrame() {
        return genFrame;
    }

    public void setGenFrame(String genFrame) {
        this.genFrame = genFrame;
    }
}

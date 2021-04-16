package com.sc.mp.model;

public class WebScEvaluate {
    private String documentId;

    private Float score;

    private String remark;

    private String fftd;

    private String phd;

    private String sqpg;

    private String szgl;

    private String shbfz;

    private String shtt;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId == null ? null : documentId.trim();
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getFftd() {
        return fftd;
    }

    public void setFftd(String fftd) {
        this.fftd = fftd == null ? null : fftd.trim();
    }

    public String getPhd() {
        return phd;
    }

    public void setPhd(String phd) {
        this.phd = phd == null ? null : phd.trim();
    }

    public String getSqpg() {
        return sqpg;
    }

    public void setSqpg(String sqpg) {
        this.sqpg = sqpg == null ? null : sqpg.trim();
    }

    public String getSzgl() {
        return szgl;
    }

    public void setSzgl(String szgl) {
        this.szgl = szgl == null ? null : szgl.trim();
    }

    public String getShbfz() {
        return shbfz;
    }

    public void setShbfz(String shbfz) {
        this.shbfz = shbfz == null ? null : shbfz.trim();
    }

    public String getShtt() {
        return shtt;
    }

    public void setShtt(String shtt) {
        this.shtt = shtt == null ? null : shtt.trim();
    }
}
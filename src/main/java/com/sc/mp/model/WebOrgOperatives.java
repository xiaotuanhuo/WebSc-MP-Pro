package com.sc.mp.model;

public class WebOrgOperatives {
    private Integer id;

    private String orgId;

    private String operativeId;

    private String operativeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getOperativeId() {
        return operativeId;
    }

    public void setOperativeId(String operativeId) {
        this.operativeId = operativeId == null ? null : operativeId.trim();
    }

    public String getOperativeName() {
        return operativeName;
    }

    public void setOperativeName(String operativeName) {
        this.operativeName = operativeName == null ? null : operativeName.trim();
    }
}
package com.sc.mp.model;

public class WebScLabel {
    private String labelId;

    private String labelName;

    private String parentId;

    private String labelLevel;

    private String leaf;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId == null ? null : labelId.trim();
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName == null ? null : labelName.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getLabelLevel() {
        return labelLevel;
    }

    public void setLabelLevel(String labelLevel) {
        this.labelLevel = labelLevel == null ? null : labelLevel.trim();
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf == null ? null : leaf.trim();
    }
}
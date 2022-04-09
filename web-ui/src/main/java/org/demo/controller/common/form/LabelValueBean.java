package org.demo.controller.common.form;

public class LabelValueBean {
    protected String label;
    protected String value;

    public LabelValueBean(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public LabelValueBean() {
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

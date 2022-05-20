package org.demo.controller.fisc.form;

import lombok.Data;

@Data
public class FiscStatusRsp {
    private String returnCode;
    private String returnDesc;
    private String fiscStatus;
    private String bankStatus;
    private String appStatus;
}

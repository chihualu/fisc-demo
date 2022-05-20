package org.demo.db.modal;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WEB_BANK_INFO")
@Data
public class WebBankInfo {

    @Id
    @Column(name = "BANKCODE", length = 30)
    private String bankCode;

    @Column(name = "BANKNAME", length = 300)
    private String bankName;

    @Column(name = "TELZONE", length = 30)
    private String telZone;

    @Column(name = "TELNO", length = 100)
    private String telNo;

    @Column(name = "UPDATEDATE", length = 80)
    private String upDateDate;

}

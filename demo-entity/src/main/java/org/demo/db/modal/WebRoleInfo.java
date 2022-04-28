package org.demo.db.modal;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WEB_ROLE_INFO")
@Data
public class WebRoleInfo {

    @Id
    @Column(name = "ROLE_ID", length = 30)
    private String roleId;

    @Column(name = "ROLE_NAME", length = 100)
    private String roleName;

    @Column(name = "funcList", length = 9999)
    private String funcList;

}

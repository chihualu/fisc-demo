package org.demo.db.modal;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WEB_USER_INFO")
@Data
public class WebUserInfo {

    @Id
    @Column(name = "USER_ID", length = 30)
    private String userId;

    @Column(name = "PASSWORD", length = 300)
    private String passwd;

    @Column(name = "USER_NAME", length = 300)
    private String userName;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "ROLE_ID", length = 30)
    private String roleId;

    @Column(name = "API_TOKEN", length = 300)
    private String apiToken;

    @Column(name = "LAST_LOGIN_IP", length = 50)
    private String lastLoginIp;

}

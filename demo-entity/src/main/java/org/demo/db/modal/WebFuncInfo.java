package org.demo.db.modal;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WEB_FUNC_INFO")
@Data
public class WebFuncInfo {

    @Id
    @Column(name = "FUNC_ID", length = 30)
    private String funcId;

    @Column(name = "FUNC_NAME", length = 100)
    private String funcName;

    @Column(name = "PARENT_ID", length = 30)
    private String parentId;

    @Column(name = "FUNC_URL", length = 50)
    private String funcUrl;

    @Column(name = "HIDDEN")
    private boolean hidden;

    @Column(name = "ORDER_NUM")
    private long orderNum;

    @Column(name = "ACTIONS")
    private String actions;

}

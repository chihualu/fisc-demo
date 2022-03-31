package org.demo.db.custom;

import org.demo.db.entity.WebUserInfo;

import java.util.List;

public interface WebUserInfoRepositoryCustom {
    List<WebUserInfo> findAllByUserId(String userId);
}

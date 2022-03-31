package org.demo.db.repository;

import org.demo.db.custom.WebUserInfoRepositoryCustom;
import org.demo.db.entity.WebFuncInfo;
import org.demo.db.entity.WebUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WebUserInfoRepository extends JpaRepository<WebUserInfo, String>, JpaSpecificationExecutor<WebUserInfo>, WebUserInfoRepositoryCustom {
}

package org.demo.db.repository;

import org.demo.db.custom.WebRoleInfoRepositoryCustom;
import org.demo.db.entity.WebRoleInfo;
import org.demo.db.entity.WebUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WebRoleInfoRepository extends JpaRepository<WebRoleInfo, String>, JpaSpecificationExecutor<WebRoleInfo>, WebRoleInfoRepositoryCustom {
}

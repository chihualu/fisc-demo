package org.demo.db.repository;

import org.demo.db.custom.WebRoleInfoRepositoryCustom;
import org.demo.db.modal.WebRoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebRoleInfoRepository extends JpaRepository<WebRoleInfo, String>, JpaSpecificationExecutor<WebRoleInfo>, WebRoleInfoRepositoryCustom {
    List<WebRoleInfo> findAllByOrderByRoleIdAsc();
}

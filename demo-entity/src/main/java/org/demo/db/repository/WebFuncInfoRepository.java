package org.demo.db.repository;

import org.demo.db.custom.WebFuncInfoRepositoryCustom;
import org.demo.db.modal.WebFuncInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WebFuncInfoRepository extends JpaRepository<WebFuncInfo, String>, JpaSpecificationExecutor<WebFuncInfo>, WebFuncInfoRepositoryCustom {
}

package org.demo.db.repository;

import org.demo.db.custom.WebBankInfoRepositoryCustom;
import org.demo.db.modal.WebBankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WebBankInfoRepository extends JpaRepository<WebBankInfo, String>, JpaSpecificationExecutor<WebBankInfo>, WebBankInfoRepositoryCustom {
}

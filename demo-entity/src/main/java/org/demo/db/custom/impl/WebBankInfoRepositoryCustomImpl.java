package org.demo.db.custom.impl;

import org.apache.commons.lang3.StringUtils;
import org.demo.db.custom.WebBankInfoRepositoryCustom;
import org.demo.db.modal.WebBankInfo;
import org.demo.db.repository.WebBankInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class WebBankInfoRepositoryCustomImpl implements WebBankInfoRepositoryCustom {

    @Autowired
    @Lazy
    private WebBankInfoRepository webBankInfoRepository;

    @Override
    public List<WebBankInfo> findAllByBankCodeAndTelZone(String bankCode, String telZone) {

        return webBankInfoRepository.findAll((Specification<WebBankInfo>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(bankCode)) {
                predicates.add(builder.like(root.get("bankCode"), "%"+bankCode+"%"));
            }

            if (StringUtils.isNotBlank(telZone)) {
                predicates.add(builder.like(root.get("telZone"), "%"+telZone+"%"));
            }

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }

}

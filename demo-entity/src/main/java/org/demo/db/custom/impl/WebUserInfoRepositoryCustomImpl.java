package org.demo.db.custom.impl;

import org.apache.commons.lang3.StringUtils;
import org.demo.db.custom.WebUserInfoRepositoryCustom;
import org.demo.db.modal.WebUserInfo;
import org.demo.db.repository.WebUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class WebUserInfoRepositoryCustomImpl implements WebUserInfoRepositoryCustom {

    @Autowired
    @Lazy
    private WebUserInfoRepository webUserInfoRepository;

    @Override
    public List<WebUserInfo> findAllByUserId(String userId) {

        return webUserInfoRepository.findAll((Specification<WebUserInfo>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(userId)) {
                predicates.add(builder.like(root.get("userId"), "%"+userId+"%"));
            }

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}

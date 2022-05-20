package org.demo.db.custom;

import org.demo.db.modal.WebBankInfo;

import java.util.List;

public interface WebBankInfoRepositoryCustom {
    List<WebBankInfo> findAllByBankCodeAndTelZone(String bankCode, String telZone);
}

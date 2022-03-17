package org.demo.entity.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WebMessage {
    private String txnType;
    private String txnCode;
    private String txnDateTime;
    private String txnStan;
    private String returnCode;
}

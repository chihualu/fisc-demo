package org.demo.entity.web.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.demo.entity.web.WebMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class P3109Req extends WebMessage {
    private String bankCode;
}

package org.demo.entity.web.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.demo.entity.web.WebMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class P3100Req extends WebMessage {
    private String message;
}

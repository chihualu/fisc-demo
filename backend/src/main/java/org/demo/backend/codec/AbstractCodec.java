package org.demo.backend.codec;

import lombok.Data;

@Data
public abstract class AbstractCodec implements BaseCodec{

    private String encoding = "UTF-8";
}

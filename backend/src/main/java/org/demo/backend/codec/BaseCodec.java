package org.demo.backend.codec;

import org.demo.backend.entity.impl.FepMsg;

public interface BaseCodec {

    public byte[] render(FepMsg fepMsg) throws Exception;
    public void parse(FepMsg fepMsg) throws Exception;

}

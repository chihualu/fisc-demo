package org.demo.backend.codec.impl;

import org.demo.backend.codec.AbstractCodec;
import org.demo.backend.entity.impl.FepMsg;

public class ISO8583Codec extends AbstractCodec {
    @Override
    public byte[] render(FepMsg fepMsg) throws Exception{
        return new byte[0];
    }

    @Override
    public void parse(FepMsg fepMsg) throws Exception{

    }
}

package cn.cas.ntsc.converter.other;

import cn.cas.ntsc.dao.other.Other;
import cn.cas.ntsc.dto.other.OtherDTO;

public class OtherConverter {
    public static OtherDTO convert(final Other other) {
        return OtherDTO.builder().id(Integer.parseInt(other.getId())).name(other.getName()).build();
    }
}

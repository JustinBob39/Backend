package cn.cas.ntsc.converter.difference;

import cn.cas.ntsc.dao.difference.Difference;
import cn.cas.ntsc.dto.difference.DifferenceDTO;

import java.util.ArrayList;
import java.util.List;

public class DifferenceConverter {
    public static List<DifferenceDTO> convert(final Difference difference) {
        final List<DifferenceDTO> differenceDTOS = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final DifferenceDTO.DifferenceDTOBuilder builder = DifferenceDTO.builder()
                    .time(difference.getTime())
                    .frameStatus(difference.getFrameStatus())
                    .parentId(Integer.parseInt(difference.getParentId()))
                    .parentStatus(difference.getParentStatus())
                    .parentEventTime(difference.getParentEventTime());
            if (i == 0) {
                builder.childDuration(difference.getFirstChildDuration())
                        .childEventTime(difference.getFirstChildEventTime())
                        .childId(difference.getFirstChildId())
                        .childValueFirst(difference.getFirstChildValueFirst_String())
                        .childValueSecond(difference.getFirstChildValueSecond_String());
            } else if (i == 1) {
                builder.childDuration(difference.getSecondChildDuration())
                        .childEventTime(difference.getSecondChildEventTime())
                        .childId(difference.getSecondChildId())
                        .childValueFirst(difference.getSecondChildValueFirst_String())
                        .childValueSecond(difference.getSecondChildValueSecond_String());
            } else {
                builder.childDuration(difference.getThirdChildDuration())
                        .childEventTime(difference.getThirdChildEventTime())
                        .childId(difference.getThirdChildId())
                        .childValueFirst(difference.getThirdChildValueFirst_String())
                        .childValueSecond(difference.getThirdChildValueSecond_String());
            }
            differenceDTOS.add(builder.build());
        }
        return differenceDTOS;
    }
}

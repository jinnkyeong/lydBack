package com.loveyourdog.brokingservice.model.dto.obj;

import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DwRankVo implements Comparable<DwRankVo>{
    private Application application;
    private int totPoint;

    @Override
    public int compareTo(DwRankVo vo) {
        if(vo.totPoint<totPoint){
            return 1;
        } else if (vo.totPoint>totPoint) {
            return -1;
        } else{
            return 0;

        }
    }
}

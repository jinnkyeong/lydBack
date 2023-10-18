package com.loveyourdog.brokingservice.model.dto.obj;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Image {

    private String dirName;
    private String fileName;
    private String extension;
}

package org.dromara.im.domain.dto;

import lombok.Data;

@Data
public class ImWindowsAuthCodeDto {

    private Boolean enabled = true;

    private String code;

    private String codeDate;

    private String generatedAt;
}

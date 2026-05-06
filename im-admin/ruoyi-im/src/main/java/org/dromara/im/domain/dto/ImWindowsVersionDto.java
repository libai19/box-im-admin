package org.dromara.im.domain.dto;

import lombok.Data;

@Data
public class ImWindowsVersionDto {

    private Boolean enabled = false;

    private String latestVersion = "1.0.0";

    private String minVersion = "1.0.0";

    private Boolean forceUpdate = false;

    private String exeUrl;

    private String setupUrl;

    private String sha256;

    private String releaseNotes;
}

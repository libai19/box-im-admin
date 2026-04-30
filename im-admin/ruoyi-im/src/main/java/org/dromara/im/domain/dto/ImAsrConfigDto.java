package org.dromara.im.domain.dto;

import lombok.Data;

@Data
public class ImAsrConfigDto {

    private Boolean enabled = false;

    private String provider = "tencent";

    private String secretId;

    private String secretKey;

    private String region = "ap-guangzhou";

    private String engineModelType = "16k_zh";

    private String appKey;

    private String appId;

    private String apiKey;

    private String apiSecret;

    private String accessKeyId;

    private String accessKeySecret;

    private String secretAccessKey;

    private String cluster;

    private String devPid;
}

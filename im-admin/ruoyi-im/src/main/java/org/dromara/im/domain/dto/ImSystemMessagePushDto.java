package org.dromara.im.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ImSystemMessagePushDto {

    private Long id;

    private String title;

    private String cover;

    private String summary;

    private Integer contentType;

    private String content;

    private String linkUrl;

    private Integer type;

    private Integer targetType;

    private String targetIds;

    private Date sendTime;
}

package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImPushTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AutoMapper(target = ImPushTask.class, reverseConvertGenerate = false)
public class ImPushTaskBo {

    private Long id;

    private Long messageId;

    private String title;

    private String content;

    private Integer targetType;

    private String targetIds;

    private Integer status;

    private String failReason;

    private Long creator;

    private Date pushTime;

    private Date createdTime;

    private Map<String, Object> params = new HashMap<>();
}

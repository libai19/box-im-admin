package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.im.domain.ImSystemMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AutoMapper(target = ImSystemMessage.class, reverseConvertGenerate = false)
public class ImSystemMessageBo {

    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    private String cover;

    private String summary;

    @NotNull(message = "内容类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer contentType;

    private String content;

    private String linkUrl;

    private Integer type;

    private Integer targetType;

    private String targetIds;

    private Integer status;

    private Long creator;

    private Date pushTime;

    private Date createdTime;

    private Map<String, Object> params = new HashMap<>();
}

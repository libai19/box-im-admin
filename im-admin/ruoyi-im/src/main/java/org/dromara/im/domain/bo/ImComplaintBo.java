package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.im.domain.ImComplaint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AutoMapper(target = ImComplaint.class, reverseConvertGenerate = false)
public class ImComplaintBo {

    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    private Long userId;

    private Long targetId;

    private Integer targetType;

    private Integer type;

    private String content;

    private String images;

    private Integer status;

    private String result;

    private Long handleBy;

    private Date handleTime;

    private Date createdTime;

    private Map<String, Object> params = new HashMap<>();
}

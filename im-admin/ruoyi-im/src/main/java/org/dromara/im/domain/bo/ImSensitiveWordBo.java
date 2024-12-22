package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.ImSensitiveWord;

/**
 * 敏感词业务对象 im_sensitive_word
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImSensitiveWord.class, reverseConvertGenerate = false)
public class ImSensitiveWordBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 敏感词内容
     */
    @NotBlank(message = "敏感词内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 是否启用 0:未启用 1:启用
     */
    @NotNull(message = "是否启用 0:未启用 1:启用不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long enabled;

    /**
     * 创建者
     */
    @NotNull(message = "创建者不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long creator;


}

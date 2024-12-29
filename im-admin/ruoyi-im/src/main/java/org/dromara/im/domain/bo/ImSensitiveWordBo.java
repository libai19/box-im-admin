package org.dromara.im.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.im.domain.ImSensitiveWord;

import java.util.Date;

/**
 * 敏感词业务对象 im_sensitive_word
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@AutoMapper(target = ImSensitiveWord.class, reverseConvertGenerate = false)
public class ImSensitiveWordBo {

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
     * 是否启用
     */
    @NotNull(message = "是否启用", groups = { EditGroup.class })
    private Boolean enabled;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}

package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 敏感词对象 im_sensitive_word
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_sensitive_word")
public class ImSensitiveWord {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 敏感词内容
     */
    private String content;

    /**
     * 是否启用 0:未启用 1:启用
     */
    private Long enabled;

    /**
     * 创建者
     */
    private Long creator;


}

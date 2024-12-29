package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 敏感词对象 im_sensitive_word
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_sensitive_word")
public class ImSensitiveWord {

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
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}

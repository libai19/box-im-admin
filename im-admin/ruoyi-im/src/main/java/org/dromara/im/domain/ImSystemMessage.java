package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统消息对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("im_system_message")
public class ImSystemMessage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String coverUrl;

    /**
     * 简介
     */
    private String intro;

    /**
     * 内容类型 0:富文本  1:外部链接
     */
    private Long contentType;

    /**
     * 富文本内容，base64编码
     */
    private String richText;

    /**
     * 外部链接
     */
    private String externLink;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 删除标识  0：正常   1：已删除
     */
    private Long deleted;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 更新者
     */
    private Long updater;


}

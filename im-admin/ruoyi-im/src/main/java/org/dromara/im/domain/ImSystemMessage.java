package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("im_system_message")
public class ImSystemMessage extends ImBaseEntity {

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


}

package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.util.Date;

/**
 * 系统消息对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_system_message")
public class ImSystemMessage implements TransPojo {

    /**
     * id
     */
    @TableId
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
     * 创建者
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除标记
     */
    @TableLogic
    private Boolean deleted;
}

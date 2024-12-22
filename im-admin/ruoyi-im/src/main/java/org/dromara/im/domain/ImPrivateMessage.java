package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 私聊消息对象 im_private_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_private_message")
public class ImPrivateMessage {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 发送用户id
     */
    private Long sendId;

    /**
     * 接收用户id
     */
    private Long recvId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    private Long type;

    /**
     * 状态 0:未读 1:已读 
     */
    private Long status;

    /**
     * 发送时间
     */
    private Date sendTime;


}

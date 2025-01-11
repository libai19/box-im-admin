package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 私聊消息对象 im_private_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_private_message")
public class ImPrivateMessage {


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

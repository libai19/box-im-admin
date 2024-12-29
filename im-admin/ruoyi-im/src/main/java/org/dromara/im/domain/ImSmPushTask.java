package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

/**
 * 系统消息推送任务对象 im_sm_push_task
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_sm_push_task")
public class ImSmPushTask {


    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 系统消息id
     */
    private Long messageId;

    /**
     * 发送序列号
     */
    private Long seqNo;

    /**
     * 推送时间
     */
    private Date sendTime;

    /**
     * 状态 1:待发送 2:发送中 3:已发送 4:已取消
     */
    private Long status;

    /**
     * 是否发送给全体用户
     */
    private Long sendToAll;

    /**
     * 接收用户id,逗号分隔,send_to_all为false时有效
     */
    private String recvIds;

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

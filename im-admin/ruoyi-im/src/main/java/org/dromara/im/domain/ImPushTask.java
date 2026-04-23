package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 推送任务对象 im_push_task
 */
@Data
@TableName("im_push_task")
public class ImPushTask {

    @TableId(value = "id")
    private Long id;

    private Long messageId;

    private String title;

    private String content;

    private Integer targetType;

    private String targetIds;

    /**
     * 推送状态 0:待发送 1:已发送 2:失败
     */
    private Integer status;

    private String failReason;

    private Long creator;

    private Date pushTime;

    private Date createdTime;
}

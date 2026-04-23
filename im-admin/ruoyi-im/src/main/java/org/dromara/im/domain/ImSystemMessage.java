package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统消息对象 im_system_message
 */
@Data
@TableName("im_system_message")
public class ImSystemMessage {

    @TableId(value = "id")
    private Long id;

    private String title;

    private String cover;

    private String summary;

    /**
     * 内容类型 1:富文本 2:外部链接
     */
    private Integer contentType;

    private String content;

    private String linkUrl;

    /**
     * 消息分类 1:系统公告 2:活动通知 3:版本更新
     */
    private Integer type;

    /**
     * 目标类型 0:全部用户 1:指定用户
     */
    private Integer targetType;

    private String targetIds;

    /**
     * 状态 0:草稿 1:已推送
     */
    private Integer status;

    private Long creator;

    private Date pushTime;

    private Date createdTime;

    private Date updatedTime;
}

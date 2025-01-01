package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSmPushTask;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.system.domain.SysUser;

import java.util.Date;

/**
 * 系统消息推送任务视图对象 im_sm_push_task
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSmPushTask.class)
public class ImSmPushTaskVo implements TransPojo {

    /**
     * id
     */
    private Long id;

    /**
     * 系统消息id
     */
    @Trans(type = TransType.SIMPLE, dataSource = ImConstant.DS_IM_PLATFORM, target = ImSystemMessage.class,
        fields = "title", ref = "messageTitle")
    private Long messageId;

    /**
     * 消息标题
     */
    private String messageTitle;

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
    private Integer status;

    /**
     * 是否发送给全体用户
     */
    private Boolean sendToAll;

    /**
     * 接收用户id
     */
    private String recvIds;

    /**
     * 创建者
     */
    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "userName", ref = "creatorName")
    private Long creator;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private Date createTime;

}

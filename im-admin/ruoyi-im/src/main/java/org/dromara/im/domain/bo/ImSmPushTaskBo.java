package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.ImSmPushTask;

import java.util.Date;

/**
 * 系统消息推送任务业务对象 im_sm_push_task
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImSmPushTask.class, reverseConvertGenerate = false)
public class ImSmPushTaskBo extends BaseEntity {


    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 系统消息id
     */
    @NotNull(message = "系统消息id不能为空", groups = { AddGroup.class, EditGroup.class })
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
    private Integer status;

    /**
     * 是否发送给全体用户
     */
    private Boolean sendToAll;

    /**
     * 接收用户id,逗号分隔
     */
    private String recvIds;

}

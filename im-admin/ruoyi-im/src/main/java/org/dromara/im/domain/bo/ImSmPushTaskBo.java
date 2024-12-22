package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImSmPushTask;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @NotNull(message = "发送序列号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long seqNo;

    /**
     * 推送时间
     */
    @NotNull(message = "推送时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date sendTime;

    /**
     * 状态 1:待发送 2:发送中 3:已发送 4:已取消
     */
    @NotNull(message = "状态 1:待发送 2:发送中 3:已发送 4:已取消不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 是否发送给全体用户
     */
    @NotNull(message = "是否发送给全体用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sendToAll;

    /**
     * 接收用户id,逗号分隔,send_to_all为false时有效
     */
    @NotBlank(message = "接收用户id,逗号分隔,send_to_all为false时有效不能为空", groups = { AddGroup.class, EditGroup.class })
    private String recvIds;

    /**
     * 删除标识  0：正常   1：已删除
     */
    @NotNull(message = "删除标识  0：正常   1：已删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deleted;

    /**
     * 创建者
     */
    @NotNull(message = "创建者不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long creator;

    /**
     * 更新者
     */
    @NotNull(message = "更新者不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long updater;


}

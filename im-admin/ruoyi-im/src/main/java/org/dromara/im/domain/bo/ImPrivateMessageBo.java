package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.ImPrivateMessage;

import java.util.Date;

/**
 * 私聊消息业务对象 im_private_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImPrivateMessage.class, reverseConvertGenerate = false)
public class ImPrivateMessageBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 发送用户id
     */
    @NotNull(message = "发送用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sendId;

    /**
     * 接收用户id
     */
    @NotNull(message = "接收用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long recvId;

    /**
     * 发送内容
     */
    @NotBlank(message = "发送内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    @NotNull(message = "消息类型 0:文字 1:图片 2:文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 状态 0:未读 1:已读 
     */
    @NotNull(message = "状态 0:未读 1:已读 不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 发送时间
     */
    @NotNull(message = "发送时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date sendTime;


}

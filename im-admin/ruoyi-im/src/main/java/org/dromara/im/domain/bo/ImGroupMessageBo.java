package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImGroupMessage;
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
 * 群消息业务对象 im_group_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImGroupMessage.class, reverseConvertGenerate = false)
public class ImGroupMessageBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 群id
     */
    @NotNull(message = "群id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 发送用户id
     */
    @NotNull(message = "发送用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sendId;

    /**
     * 发送用户昵称
     */
    @NotBlank(message = "发送用户昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sendNickName;

    /**
     * 被@用户id列表，逗号分隔
     */
    @NotBlank(message = "被@用户id列表，逗号分隔不能为空", groups = { AddGroup.class, EditGroup.class })
    private String atUserIds;

    /**
     * 发送内容
     */
    @NotBlank(message = "发送内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    @NotNull(message = "消息类型 0:文字 1:图片 2:文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 发送时间
     */
    @NotNull(message = "发送时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date sendTime;

    /**
     * 回执消息是否完成
     */
    @NotNull(message = "回执消息是否完成不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long receiptOk;

    /**
     * 是否回执消息
     */
    @NotNull(message = "是否回执消息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long receipt;

    /**
     * 接收用户id,逗号分隔，为空表示发给所有成员
     */
    @NotBlank(message = "接收用户id,逗号分隔，为空表示发给所有成员不能为空", groups = { AddGroup.class, EditGroup.class })
    private String recvIds;


}

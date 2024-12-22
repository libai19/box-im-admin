package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImGroupMember;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 群成员业务对象 im_group_member
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImGroupMember.class, reverseConvertGenerate = false)
public class ImGroupMemberBo extends BaseEntity {

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
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 组内显示名称
     */
    @NotBlank(message = "组内显示名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remarkNickName;

    /**
     * 用户头像
     */
    @NotBlank(message = "用户头像不能为空", groups = { AddGroup.class, EditGroup.class })
    private String headImage;

    /**
     * 群名备注
     */
    @NotBlank(message = "群名备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remarkGroupName;

    /**
     * 是否已退出
     */
    @NotNull(message = "是否已退出不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quit;

    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date createdTime;

    /**
     * 退出时间
     */
    @NotNull(message = "退出时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date quitTime;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userNickName;


}

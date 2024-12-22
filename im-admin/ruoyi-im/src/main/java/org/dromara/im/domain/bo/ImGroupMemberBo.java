package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.im.domain.ImGroupMember;

import java.util.Date;

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

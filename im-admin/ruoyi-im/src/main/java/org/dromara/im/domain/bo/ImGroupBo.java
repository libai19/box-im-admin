package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImGroup;
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
 * 群业务对象 im_group
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImGroup.class, reverseConvertGenerate = false)
public class ImGroupBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 群名字
     */
    @NotBlank(message = "群名字不能为空")
    private String name;

    /**
     * 群主id
     */
    @NotNull(message = "群主id不能为空")
    private Long ownerId;

    /**
     * 群头像
     */
    @NotBlank(message = "群头像不能为空")
    private String headImage;

    /**
     * 群头像缩略图
     */
    @NotBlank(message = "群头像缩略图不能为空")
    private String headImageThumb;

    /**
     * 群公告
     */
    @NotBlank(message = "群公告不能为空")
    private String notice;

    /**
     * 群备注
     */
    @NotBlank(message = "群备注不能为空")
    private String remark;

    /**
     * 是否已解散
     */
    @NotNull(message = "是否已解散不能为空")
    private Long dissolve;

    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空")
    private Date createdTime;

    /**
     * 是否被封禁 0:否 1:是
     */
    @NotNull(message = "是否被封禁")
    private Long isBanned;

    /**
     * 被封禁原因
     */
    @NotBlank(message = "被封禁原因不能为空")
    private String reason;


}

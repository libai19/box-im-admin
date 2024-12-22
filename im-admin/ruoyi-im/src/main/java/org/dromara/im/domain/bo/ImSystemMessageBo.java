package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImSystemMessage;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 系统消息业务对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ImSystemMessage.class, reverseConvertGenerate = false)
public class ImSystemMessageBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 封面
     */
    @NotBlank(message = "封面不能为空", groups = { AddGroup.class, EditGroup.class })
    private String coverUrl;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空", groups = { AddGroup.class, EditGroup.class })
    private String intro;

    /**
     * 内容类型 0:富文本  1:外部链接
     */
    @NotNull(message = "内容类型 0:富文本  1:外部链接不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long contentType;

    /**
     * 富文本内容，base64编码
     */
    @NotBlank(message = "富文本内容，base64编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String richText;

    /**
     * 外部链接
     */
    @NotBlank(message = "外部链接不能为空", groups = { AddGroup.class, EditGroup.class })
    private String externLink;

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

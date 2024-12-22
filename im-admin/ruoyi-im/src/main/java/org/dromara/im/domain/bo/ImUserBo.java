package org.dromara.im.domain.bo;

import org.dromara.im.domain.ImUser;
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
 * 用户业务对象 im_user
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@AutoMapper(target = ImUser.class, reverseConvertGenerate = false)
public class ImUserBo {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 用户头像缩略图
     */
    private String headImageThumb;

    /**
     * 密码(明文)
     */
    private String password;

    /**
     * 性别 0:男 1::女
     */
    private Long sex;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     *
     */
    private Long type;

    /**
     * 是否被封禁
     */
    private Boolean isBanned;

    /**
     * 被封禁原因
     */
    private String reason;

    /**
     * 客户端id,用于uni-push推送
     */
    private String cid;

    /**
     * 状态 0：正常   1:已注销
     */
    private Long status;

}

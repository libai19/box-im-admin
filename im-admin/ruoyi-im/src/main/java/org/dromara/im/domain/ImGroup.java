package org.dromara.im.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 群对象 im_group
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_group")
public class ImGroup  {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 群名字
     */
    private String name;

    /**
     * 群主id
     */
    private Long ownerId;

    /**
     * 群头像
     */
    private String headImage;

    /**
     * 群头像缩略图
     */
    private String headImageThumb;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群备注
     */
    private String remark;

    /**
     * 是否已解散
     */
    private Long dissolve;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 是否被封禁 0:否 1:是
     */
    private Long isBanned;

    /**
     * 被封禁原因
     */
    private String reason;


}

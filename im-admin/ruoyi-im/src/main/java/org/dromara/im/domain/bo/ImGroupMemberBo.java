package org.dromara.im.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImGroupMember;

import java.util.Date;

/**
 * 群成员业务对象 im_group_member
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@AutoMapper(target = ImGroupMember.class, reverseConvertGenerate = false)
public class ImGroupMemberBo {

    /**
     * id
     */
    private Long id;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 组内显示名称
     */
    private String remarkNickName;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 群名备注
     */
    private String remarkGroupName;

    /**
     * 是否已退出
     */
    private Long quit;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 退出时间
     */
    private Date quitTime;

    /**
     * 用户昵称
     */
    private String userNickName;


}

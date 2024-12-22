package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.util.Date;

/**
 * 群成员对象 im_group_member
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@TableName("im_group_member")
public class ImGroupMember implements TransPojo {

    /**
     * id
     */
    @TableId(value = "id")
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

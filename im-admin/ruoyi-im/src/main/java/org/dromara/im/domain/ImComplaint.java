package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户投诉对象 im_complaint
 */
@Data
@TableName("im_complaint")
public class ImComplaint {

    @TableId(value = "id")
    private Long id;

    private Long userId;

    private Long targetId;

    /**
     * 投诉对象类型 1:用户 2:群
     */
    private Integer targetType;

    /**
     * 投诉原因类型 1:垃圾信息 2:骚扰 3:诈骗 4:其他
     */
    private Integer type;

    private String content;

    private String images;

    /**
     * 处理状态 0:未处理 1:处理中 2:已处理
     */
    private Integer status;

    private String result;

    private Long handleBy;

    private Date handleTime;

    private Date createdTime;

    private Date updatedTime;
}

package org.dromara.im.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.im.domain.ImGroup;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 群视图对象 im_group
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImGroup.class)
public class ImGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 群名字
     */
    @ExcelProperty(value = "群名字")
    private String name;

    /**
     * 群主id
     */
    @ExcelProperty(value = "群主id")
    private Long ownerId;

    /**
     * 群头像
     */
    @ExcelProperty(value = "群头像")
    private String headImage;


    /**
     * 群头像缩略图
     */
    @ExcelProperty(value = "群头像缩略图")
    private String headImageThumb;

    /**
     * 群公告
     */
    @ExcelProperty(value = "群公告")
    private String notice;

    /**
     * 群备注
     */
    @ExcelProperty(value = "群备注")
    private String remark;

    /**
     * 是否已解散
     */
    @ExcelProperty(value = "是否已解散")
    private Long dissolve;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createdTime;

    /**
     * 是否被封禁 0:否 1:是
     */
    @ExcelProperty(value = "是否被封禁 0:否 1:是")
    private Long isBanned;

    /**
     * 被封禁原因
     */
    @ExcelProperty(value = "被封禁原因")
    private String reason;


}

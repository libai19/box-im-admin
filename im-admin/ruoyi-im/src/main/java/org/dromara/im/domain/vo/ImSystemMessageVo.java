package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.system.domain.SysUser;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSystemMessage.class)
public class ImSystemMessageVo implements TransPojo {

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "标题")
    private String title;

    @ExcelProperty(value = "封面")
    private String cover;

    @ExcelProperty(value = "简介")
    private String summary;

    @ExcelProperty(value = "内容类型")
    private Integer contentType;

    private String content;

    private String linkUrl;

    private Integer type;

    private Integer targetType;

    private String targetIds;

    @ExcelProperty(value = "状态")
    private Integer status;

    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "userName", ref = "creatorName")
    private Long creator;

    @ExcelProperty(value = "创建者")
    private String creatorName;

    private Date pushTime;

    @ExcelProperty(value = "创建时间")
    private Date createdTime;
}

package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImPushTask;
import org.dromara.system.domain.SysUser;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImPushTask.class)
public class ImPushTaskVo implements TransPojo {

    @ExcelProperty(value = "id")
    private Long id;

    private Long messageId;

    @ExcelProperty(value = "推送消息")
    private String title;

    private String content;

    private Integer targetType;

    private String targetIds;

    @ExcelProperty(value = "推送状态")
    private Integer status;

    private String failReason;

    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "userName", ref = "creatorName")
    private Long creator;

    @ExcelProperty(value = "创建者")
    private String creatorName;

    @ExcelProperty(value = "推送时间")
    private Date pushTime;

    @ExcelProperty(value = "创建时间")
    private Date createdTime;
}

package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImComplaint;
import org.dromara.im.domain.ImGroup;
import org.dromara.im.domain.ImUser;
import org.dromara.system.domain.SysUser;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImComplaint.class)
public class ImComplaintVo implements TransPojo {

    @ExcelProperty(value = "id")
    private Long id;

    @Trans(type = TransType.SIMPLE, dataSource = ImConstant.DS_IM_PLATFORM, target = ImUser.class, fields = "userName", ref = "userName")
    private Long userId;

    @ExcelProperty(value = "发起用户")
    private String userName;

    private Long targetId;

    private Integer targetType;

    @ExcelProperty(value = "投诉对象")
    private String targetName;

    @ExcelProperty(value = "投诉原因")
    private Integer type;

    @ExcelProperty(value = "投诉内容")
    private String content;

    private String images;

    @ExcelProperty(value = "处理状态")
    private Integer status;

    @ExcelProperty(value = "处理结果")
    private String result;

    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "userName", ref = "handleByName")
    private Long handleBy;

    @ExcelProperty(value = "处理人")
    private String handleByName;

    private Date handleTime;

    @ExcelProperty(value = "投诉时间")
    private Date createdTime;
}

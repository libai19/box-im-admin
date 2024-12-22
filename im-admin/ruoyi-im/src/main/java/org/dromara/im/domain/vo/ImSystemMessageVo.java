package org.dromara.im.domain.vo;

import org.dromara.im.domain.ImSystemMessage;
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
 * 系统消息视图对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSystemMessage.class)
public class ImSystemMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String coverUrl;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String intro;

    /**
     * 内容类型 0:富文本  1:外部链接
     */
    @ExcelProperty(value = "内容类型 0:富文本  1:外部链接")
    private Long contentType;

    /**
     * 富文本内容，base64编码
     */
    @ExcelProperty(value = "富文本内容，base64编码")
    private String richText;

    /**
     * 外部链接
     */
    @ExcelProperty(value = "外部链接")
    private String externLink;

    /**
     * 删除标识  0：正常   1：已删除
     */
    @ExcelProperty(value = "删除标识  0：正常   1：已删除")
    private Long deleted;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private Long creator;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private Long updater;


}

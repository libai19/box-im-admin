package org.dromara.im.domain.vo;

import org.dromara.im.domain.ImSensitiveWord;
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
 * 敏感词视图对象 im_sensitive_word
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSensitiveWord.class)
public class ImSensitiveWordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 敏感词内容
     */
    @ExcelProperty(value = "敏感词内容")
    private String content;

    /**
     * 是否启用 0:未启用 1:启用
     */
    @ExcelProperty(value = "是否启用 0:未启用 1:启用")
    private Long enabled;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private Long creator;


}

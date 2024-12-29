package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.system.domain.SysUser;



/**
 * 系统消息视图对象 im_system_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSystemMessage.class)
public class ImSystemMessageVo implements TransPojo {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String coverUrl;

    /**
     * 简介
     */
    private String intro;

    /**
     * 内容类型
     */
    private Long contentType;

    /**
     * 富文本内容，base64编码
     */
    private String richText;

    /**
     * 外部链接
     */
    private String externLink;

    /**
     * 创建者
     */
    @Trans(type = TransType.SIMPLE, target = SysUser.class, fields = "userName", ref = "creatorName")
    private Long creator;

    @Schema(description = "创建者名称")
    private String creatorName;

}

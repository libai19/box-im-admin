package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImPrivateMessage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 私聊消息视图对象 im_private_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImPrivateMessage.class)
public class ImPrivateMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 发送用户id
     */
    @ExcelProperty(value = "发送用户id")
    private Long sendId;

    /**
     * 接收用户id
     */
    @ExcelProperty(value = "接收用户id")
    private Long recvId;

    /**
     * 发送内容
     */
    @ExcelProperty(value = "发送内容")
    private String content;

    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    @ExcelProperty(value = "消息类型 0:文字 1:图片 2:文件")
    private Long type;

    /**
     * 状态 0:未读 1:已读 
     */
    @ExcelProperty(value = "状态 0:未读 1:已读 ")
    private Long status;

    /**
     * 发送时间
     */
    @ExcelProperty(value = "发送时间")
    private Date sendTime;


}

package org.dromara.im.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.im.domain.ImGroupMessage;
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
 * 群消息视图对象 im_group_message
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImGroupMessage.class)
public class ImGroupMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 群id
     */
    @ExcelProperty(value = "群id")
    private Long groupId;

    /**
     * 发送用户id
     */
    @ExcelProperty(value = "发送用户id")
    private Long sendId;

    /**
     * 发送用户昵称
     */
    @ExcelProperty(value = "发送用户昵称")
    private String sendNickName;

    /**
     * 被@用户id列表，逗号分隔
     */
    @ExcelProperty(value = "被@用户id列表，逗号分隔")
    private String atUserIds;

    /**
     * 发送内容
     */
    @ExcelProperty(value = "发送内容")
    private String content;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long status;

    /**
     * 消息类型 0:文字 1:图片 2:文件
     */
    @ExcelProperty(value = "消息类型 0:文字 1:图片 2:文件")
    private Long type;

    /**
     * 发送时间
     */
    @ExcelProperty(value = "发送时间")
    private Date sendTime;

    /**
     * 回执消息是否完成
     */
    @ExcelProperty(value = "回执消息是否完成")
    private Long receiptOk;

    /**
     * 是否回执消息
     */
    @ExcelProperty(value = "是否回执消息")
    private Long receipt;

    /**
     * 接收用户id,逗号分隔，为空表示发给所有成员
     */
    @ExcelProperty(value = "接收用户id,逗号分隔，为空表示发给所有成员")
    private String recvIds;


}

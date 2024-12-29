package org.dromara.im.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.im.domain.ImSmPushTask;

import java.util.Date;



/**
 * 系统消息推送任务视图对象 im_sm_push_task
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImSmPushTask.class)
public class ImSmPushTaskVo {

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 系统消息id
     */
    @ExcelProperty(value = "系统消息id")
    private Long messageId;

    /**
     * 发送序列号
     */
    @ExcelProperty(value = "发送序列号")
    private Long seqNo;

    /**
     * 推送时间
     */
    @ExcelProperty(value = "推送时间")
    private Date sendTime;

    /**
     * 状态 1:待发送 2:发送中 3:已发送 4:已取消
     */
    @ExcelProperty(value = "状态 1:待发送 2:发送中 3:已发送 4:已取消")
    private Long status;

    /**
     * 是否发送给全体用户
     */
    @ExcelProperty(value = "是否发送给全体用户")
    private Long sendToAll;

    /**
     * 接收用户id,逗号分隔,send_to_all为false时有效
     */
    @ExcelProperty(value = "接收用户id,逗号分隔,send_to_all为false时有效")
    private String recvIds;

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

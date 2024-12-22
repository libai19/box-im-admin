package org.dromara.im.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.im.domain.ImUser;
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
 * 用户视图对象 im_user
 *
 * @author Blue
 * @date 2024-12-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ImUser.class)
public class ImUserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String userName;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @ExcelProperty(value = "用户头像")
    private String headImage;


    /**
     * 用户头像缩略图
     */
    private String headImageThumb;

    /**
     * 密码(明文)
     */
    private String password;

    /**
     * 性别 0:男 1::女
     */
    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=男,1=女")
    private Long sex;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createdTime;

    /**
     *  类型
     */
    private Long type;

    /**
     * 是否被封禁 0:否 1:是
     */
    @ExcelProperty(value = "是否被封禁", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=否,1=是")
    private Boolean isBanned;

    /**
     * 被封禁原因
     */
    @ExcelProperty(value = "被封禁原因")
    private String reason;

    /**
     * 客户端id,用于uni-push推送
     */
    @ExcelProperty(value = "客户端id")
    private String cid;

    /**
     * 状态 0：正常   1:已注销
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=已注销")
    private Long status;


}

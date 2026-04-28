package org.dromara.im.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("im_app_config")
public class ImAppConfig {

    @TableId(value = "id")
    private Long id;

    private String configKey;

    private String configValue;

    private String remark;

    private Date createdTime;

    private Date updatedTime;
}

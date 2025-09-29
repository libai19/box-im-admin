package org.dromara.im.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.im.domain.ImGroupMessage;
import org.dromara.im.domain.vo.ImGroupMessageVo;

import java.util.List;
import java.util.Map;

/**
 * 群消息Mapper接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface ImGroupMessageMapper extends BaseMapperPlus<ImGroupMessage, ImGroupMessageVo> {

    /**
     * 按天统计群聊消息量
     * @param days 统计天数
     * @return 统计结果
     */
    @Select("SELECT DATE(send_time) as date, COUNT(*) as count " +
        "FROM im_group_message " +
        "WHERE send_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
        "GROUP BY DATE(send_time) " +
        "ORDER BY date ASC")
    List<Map<String, Object>> getDailyGroupMessageCount(@Param("days") Integer days);
}

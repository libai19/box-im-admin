package org.dromara.im.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.im.domain.ImUser;
import org.dromara.im.domain.vo.ImUserVo;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface ImUserMapper extends BaseMapperPlus<ImUser, ImUserVo> {

    /**
     * 按天统计用户注册数量
     * @param days 统计天数
     * @return 统计结果
     */
    @Select("SELECT DATE(created_time) as date, COUNT(*) as count " +
        "FROM im_user " +
        "WHERE created_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
        "GROUP BY DATE(created_time) " +
        "ORDER BY date ASC")
    List<Map<String, Object>> getDailyRegistrationCount(@Param("days") Integer days);

}

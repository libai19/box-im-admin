package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.domain.vo.ImGroupMessageVo;

/**
 * 群消息Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImGroupMessageService {

    /**
     * 查询群消息
     *
     * @param id 主键
     * @return 群消息
     */
    ImGroupMessageVo queryById(Long id);

    /**
     * 分页查询群消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群消息分页列表
     */
    TableDataInfo<ImGroupMessageVo> queryPageList(ImGroupMessageBo bo, PageQuery pageQuery);



}

package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.im.domain.vo.ImPrivateMessageVo;

/**
 * 私聊消息Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImPrivateMessageService {

    /**
     * 查询私聊消息
     *
     * @param id 主键
     * @return 私聊消息
     */
    ImPrivateMessageVo queryById(Long id);

    /**
     * 分页查询私聊消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 私聊消息分页列表
     */
    TableDataInfo<ImPrivateMessageVo> queryPageList(ImPrivateMessageBo bo, PageQuery pageQuery);




}

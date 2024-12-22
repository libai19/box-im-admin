package org.dromara.im.service;

import org.dromara.im.domain.vo.ImPrivateMessageVo;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

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

    /**
     * 查询符合条件的私聊消息列表
     *
     * @param bo 查询条件
     * @return 私聊消息列表
     */
    List<ImPrivateMessageVo> queryList(ImPrivateMessageBo bo);

    /**
     * 新增私聊消息
     *
     * @param bo 私聊消息
     * @return 是否新增成功
     */
    Boolean insertByBo(ImPrivateMessageBo bo);

    /**
     * 修改私聊消息
     *
     * @param bo 私聊消息
     * @return 是否修改成功
     */
    Boolean updateByBo(ImPrivateMessageBo bo);

    /**
     * 校验并批量删除私聊消息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

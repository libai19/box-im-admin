package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.domain.vo.ImGroupMessageVo;

import java.util.Collection;
import java.util.List;

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

    /**
     * 查询符合条件的群消息列表
     *
     * @param bo 查询条件
     * @return 群消息列表
     */
    List<ImGroupMessageVo> queryList(ImGroupMessageBo bo);

    /**
     * 新增群消息
     *
     * @param bo 群消息
     * @return 是否新增成功
     */
    Boolean insertByBo(ImGroupMessageBo bo);

    /**
     * 修改群消息
     *
     * @param bo 群消息
     * @return 是否修改成功
     */
    Boolean updateByBo(ImGroupMessageBo bo);

    /**
     * 校验并批量删除群消息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

package org.dromara.im.service;

import org.dromara.im.domain.vo.ImGroupVo;
import org.dromara.im.domain.bo.ImGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 群Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImGroupService {

    /**
     * 查询群
     *
     * @param id 主键
     * @return 群
     */
    ImGroupVo queryById(Long id);

    /**
     * 分页查询群列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群分页列表
     */
    TableDataInfo<ImGroupVo> queryPageList(ImGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的群列表
     *
     * @param bo 查询条件
     * @return 群列表
     */
    List<ImGroupVo> queryList(ImGroupBo bo);

    /**
     * 新增群
     *
     * @param bo 群
     * @return 是否新增成功
     */
    Boolean insertByBo(ImGroupBo bo);

    /**
     * 修改群
     *
     * @param bo 群
     * @return 是否修改成功
     */
    Boolean updateByBo(ImGroupBo bo);

    /**
     * 校验并批量删除群信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

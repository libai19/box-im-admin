package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;

import java.util.Collection;
import java.util.List;

/**
 * 系统消息Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImSystemMessageService {

    /**
     * 查询系统消息
     *
     * @param id 主键
     * @return 系统消息
     */
    ImSystemMessageVo queryById(Long id);

    /**
     * 分页查询系统消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系统消息分页列表
     */
    TableDataInfo<ImSystemMessageVo> queryPageList(ImSystemMessageBo bo, PageQuery pageQuery);



    /**
     * 新增系统消息
     *
     * @param bo 系统消息
     * @return 是否新增成功
     */
    Boolean insertByBo(ImSystemMessageBo bo);

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 是否修改成功
     */
    Boolean updateByBo(ImSystemMessageBo bo);

    /**
     * 校验并批量删除系统消息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    List<ImSystemMessageVo> findByTitle(String title);
}

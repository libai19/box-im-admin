package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImSmPushTaskBo;
import org.dromara.im.domain.vo.ImSmPushTaskVo;

import java.util.Collection;

/**
 * 系统消息推送任务Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImSmPushTaskService {

    /**
     * 查询系统消息推送任务
     *
     * @param id 主键
     * @return 系统消息推送任务
     */
    ImSmPushTaskVo queryById(Long id);

    /**
     * 分页查询系统消息推送任务列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系统消息推送任务分页列表
     */
    TableDataInfo<ImSmPushTaskVo> queryPageList(ImSmPushTaskBo bo, PageQuery pageQuery);


    /**
     * 新增系统消息推送任务
     *
     * @param bo 系统消息推送任务
     * @return 是否新增成功
     */
    Boolean insertByBo(ImSmPushTaskBo bo);

    /**
     * 修改系统消息推送任务
     *
     * @param bo 系统消息推送任务
     * @return 是否修改成功
     */
    Boolean updateByBo(ImSmPushTaskBo bo);

    /**
     * 校验并批量删除系统消息推送任务信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteByIds(Collection<Long> ids, Boolean isValid);


    void cancel(Long id);

    void open(Long id);

    boolean isExistTask(Collection<Long> messageIds);
}

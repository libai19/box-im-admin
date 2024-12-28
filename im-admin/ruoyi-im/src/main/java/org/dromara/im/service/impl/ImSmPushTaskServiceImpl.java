package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSmPushTask;
import org.dromara.im.domain.bo.ImSmPushTaskBo;
import org.dromara.im.domain.vo.ImSmPushTaskVo;
import org.dromara.im.mapper.ImSmPushTaskMapper;
import org.dromara.im.service.IImSmPushTaskService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统消息推送任务Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImSmPushTaskServiceImpl implements IImSmPushTaskService {

    private final ImSmPushTaskMapper baseMapper;

    /**
     * 查询系统消息推送任务
     *
     * @param id 主键
     * @return 系统消息推送任务
     */
    @Override
    public ImSmPushTaskVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询系统消息推送任务列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系统消息推送任务分页列表
     */
    @Override
    public TableDataInfo<ImSmPushTaskVo> queryPageList(ImSmPushTaskBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImSmPushTask> wrapper = buildQueryWrapper(bo);
        Page<ImSmPushTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的系统消息推送任务列表
     *
     * @param bo 查询条件
     * @return 系统消息推送任务列表
     */
    @Override
    public List<ImSmPushTaskVo> queryList(ImSmPushTaskBo bo) {
        LambdaQueryWrapper<ImSmPushTask> wrapper = buildQueryWrapper(bo);
        return baseMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<ImSmPushTask> buildQueryWrapper(ImSmPushTaskBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImSmPushTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getMessageId() != null, ImSmPushTask::getMessageId, bo.getMessageId());
        wrapper.eq(bo.getSeqNo() != null, ImSmPushTask::getSeqNo, bo.getSeqNo());
        wrapper.eq(bo.getSendTime() != null, ImSmPushTask::getSendTime, bo.getSendTime());
        wrapper.eq(bo.getStatus() != null, ImSmPushTask::getStatus, bo.getStatus());
        wrapper.eq(bo.getSendToAll() != null, ImSmPushTask::getSendToAll, bo.getSendToAll());
        wrapper.eq(StringUtils.isNotBlank(bo.getRecvIds()), ImSmPushTask::getRecvIds, bo.getRecvIds());
        wrapper.eq(bo.getDeleted() != null, ImSmPushTask::getDeleted, bo.getDeleted());
        wrapper.eq(bo.getCreator() != null, ImSmPushTask::getCreator, bo.getCreator());
        wrapper.eq(bo.getUpdater() != null, ImSmPushTask::getUpdater, bo.getUpdater());
        return wrapper;
    }

    /**
     * 新增系统消息推送任务
     *
     * @param bo 系统消息推送任务
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImSmPushTaskBo bo) {
        ImSmPushTask add = MapstructUtils.convert(bo, ImSmPushTask.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改系统消息推送任务
     *
     * @param bo 系统消息推送任务
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImSmPushTaskBo bo) {
        ImSmPushTask update = MapstructUtils.convert(bo, ImSmPushTask.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImSmPushTask entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除系统消息推送任务信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}

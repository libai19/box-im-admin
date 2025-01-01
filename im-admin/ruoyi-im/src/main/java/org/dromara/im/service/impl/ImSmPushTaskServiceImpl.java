package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSmPushTask;
import org.dromara.im.domain.bo.ImSmPushTaskBo;
import org.dromara.im.domain.vo.ImSmPushTaskVo;
import org.dromara.im.enums.ImSmPushStatus;
import org.dromara.im.mapper.ImSmPushTaskMapper;
import org.dromara.im.service.IImSmPushTaskService;
import org.dromara.im.util.CommaTextUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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



    private LambdaQueryWrapper<ImSmPushTask> buildQueryWrapper(ImSmPushTaskBo bo) {
        LambdaQueryWrapper<ImSmPushTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getMessageId() != null, ImSmPushTask::getMessageId, bo.getMessageId());
        wrapper.orderByDesc(ImSmPushTask::getId);
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
        ImSmPushTask task = valid(MapstructUtils.convert(bo, ImSmPushTask.class));
        task.setDeleted(false);
        task.setCreator(LoginHelper.getUserId());
        task.setCreateTime(new Date());
        return baseMapper.insert(task) > 0;
    }




    /**
     * 修改系统消息推送任务
     *
     * @param bo 系统消息推送任务
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImSmPushTaskBo bo) {
        ImSmPushTask task = this.baseMapper.selectById(bo.getId());
        if (!ImSmPushStatus.WAIT_SEND.getValue().equals(task.getStatus())) {
            throw new ServiceException("只允许修改未发送的任务");
        }
        task = valid(MapstructUtils.convert(bo, ImSmPushTask.class));
        return baseMapper.updateById(task) > 0;
    }


    /**
     * 校验并批量删除系统消息推送任务信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public void cancel(Long id) {
        LambdaUpdateWrapper<ImSmPushTask> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ImSmPushTask::getId, id);
        wrapper.set(ImSmPushTask::getStatus, ImSmPushStatus.CANCEL.getValue());
        this.baseMapper.update(wrapper);
    }

    @Override
    public void open(Long id) {
        LambdaUpdateWrapper<ImSmPushTask> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ImSmPushTask::getId, id);
        wrapper.set(ImSmPushTask::getStatus, ImSmPushStatus.WAIT_SEND.getValue());
        this.baseMapper.update(wrapper);
    }

    @Override
    public boolean isExistTask(Collection<Long> messageIds) {
        LambdaQueryWrapper<ImSmPushTask> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ImSmPushTask::getMessageId, messageIds);
        return this.baseMapper.exists(wrapper);
    }


    private ImSmPushTask valid(ImSmPushTask task){
        // 校验接收用户
        if(task.getSendToAll()){
            task.setRecvIds(Strings.EMPTY);
        }else {
            // 去重
            Set<String> recvIds = CommaTextUtils.asSet(task.getRecvIds());
            if(recvIds.isEmpty()){
                throw new ServiceException("请选择接收用户");
            }
            if(recvIds.size() > 20){
                throw new ServiceException("接收用户最多选择20人");
            }
            task.setRecvIds(CommaTextUtils.asText(recvIds));
        }
        // 校验时间:小于当前时间，修正为当前时间，表示立即发送
        if (Objects.isNull(task.getSendTime()) || task.getSendTime().compareTo(new Date()) < 0) {
            task.setSendTime(new Date());
        }
        return task;
    }
}

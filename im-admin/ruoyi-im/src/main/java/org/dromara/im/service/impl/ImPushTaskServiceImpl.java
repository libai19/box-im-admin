package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImPushTask;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.im.domain.bo.ImPushTaskBo;
import org.dromara.im.domain.vo.ImPushTaskVo;
import org.dromara.im.mapper.ImPushTaskMapper;
import org.dromara.im.mapper.ImSystemMessageMapper;
import org.dromara.im.service.IImPushTaskService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImPushTaskServiceImpl implements IImPushTaskService {

    private static final String IM_SYSTEM_QUEUE = "im:message:system";

    private final ImPushTaskMapper baseMapper;
    private final ImSystemMessageMapper systemMessageMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public ImPushTaskVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<ImPushTaskVo> queryPageList(ImPushTaskBo bo, PageQuery pageQuery) {
        Page<ImPushTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(result);
    }

    @Override
    public List<ImPushTaskVo> queryList(ImPushTaskBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public Boolean pushMessage(Long messageId) {
        ImSystemMessage message = systemMessageMapper.selectById(messageId);
        if (message == null) {
            return false;
        }
        ImPushTask task = new ImPushTask();
        task.setMessageId(message.getId());
        task.setTitle(message.getTitle());
        task.setContent(message.getContentType() != null && message.getContentType() == 2 ? message.getLinkUrl() : message.getContent());
        task.setTargetType(message.getTargetType());
        task.setTargetIds(message.getTargetIds());
        task.setCreator(LoginHelper.getUserId());
        task.setCreatedTime(new Date());
        send(task);
        systemMessageMapper.update(Wrappers.<ImSystemMessage>lambdaUpdate()
            .eq(ImSystemMessage::getId, messageId)
            .set(ImSystemMessage::getStatus, 1)
            .set(ImSystemMessage::getPushTime, task.getPushTime())
            .set(ImSystemMessage::getUpdatedTime, new Date()));
        return baseMapper.insert(task) > 0;
    }

    @Override
    public Boolean resend(Long id) {
        ImPushTask task = baseMapper.selectById(id);
        if (task == null) {
            return false;
        }
        send(task);
        return baseMapper.updateById(task) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    private LambdaQueryWrapper<ImPushTask> buildQueryWrapper(ImPushTaskBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImPushTask> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getTitle()), ImPushTask::getTitle, bo.getTitle());
        wrapper.eq(!Objects.isNull(bo.getStatus()), ImPushTask::getStatus, bo.getStatus());
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null, ImPushTask::getCreatedTime,
            params.get("beginTime"), params.get("endTime"));
        wrapper.orderByDesc(ImPushTask::getId);
        return wrapper;
    }

    private void send(ImPushTask task) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", task.getMessageId());
            payload.put("title", task.getTitle());
            payload.put("content", task.getContent());
            payload.put("targetType", task.getTargetType());
            payload.put("targetIds", task.getTargetIds());
            redisTemplate.opsForList().rightPush(IM_SYSTEM_QUEUE, payload);
            WebSocketUtils.publishAll("[系统消息] " + task.getTitle());
            task.setStatus(1);
            task.setFailReason(null);
        } catch (Exception e) {
            task.setStatus(2);
            task.setFailReason(e.getMessage());
        }
        task.setPushTime(new Date());
    }
}

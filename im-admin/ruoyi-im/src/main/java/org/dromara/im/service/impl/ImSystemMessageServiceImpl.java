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
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;
import org.dromara.im.mapper.ImSystemMessageMapper;
import org.dromara.im.service.IImPushTaskService;
import org.dromara.im.service.IImSystemMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImSystemMessageServiceImpl implements IImSystemMessageService {

    private final ImSystemMessageMapper baseMapper;
    private final IImPushTaskService pushTaskService;

    @Override
    public ImSystemMessageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<ImSystemMessageVo> queryPageList(ImSystemMessageBo bo, PageQuery pageQuery) {
        Page<ImSystemMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(result);
    }

    @Override
    public List<ImSystemMessageVo> queryList(ImSystemMessageBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public Boolean insertByBo(ImSystemMessageBo bo) {
        ImSystemMessage message = MapstructUtils.convert(bo, ImSystemMessage.class);
        message.setCreator(LoginHelper.getUserId());
        message.setCreatedTime(new Date());
        message.setUpdatedTime(new Date());
        if (message.getType() == null) {
            message.setType(1);
        }
        if (message.getTargetType() == null) {
            message.setTargetType(0);
        }
        if (message.getStatus() == null) {
            message.setStatus(0);
        }
        return baseMapper.insert(message) > 0;
    }

    @Override
    public Boolean updateByBo(ImSystemMessageBo bo) {
        ImSystemMessage message = MapstructUtils.convert(bo, ImSystemMessage.class);
        message.setUpdatedTime(new Date());
        return baseMapper.updateById(message) > 0;
    }

    @Override
    public Boolean push(Long id) {
        return pushTaskService.pushMessage(id);
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    private LambdaQueryWrapper<ImSystemMessage> buildQueryWrapper(ImSystemMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImSystemMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getTitle()), ImSystemMessage::getTitle, bo.getTitle());
        wrapper.eq(!Objects.isNull(bo.getContentType()), ImSystemMessage::getContentType, bo.getContentType());
        wrapper.eq(!Objects.isNull(bo.getStatus()), ImSystemMessage::getStatus, bo.getStatus());
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null, ImSystemMessage::getCreatedTime,
            params.get("beginTime"), params.get("endTime"));
        wrapper.orderByDesc(ImSystemMessage::getId);
        return wrapper;
    }
}

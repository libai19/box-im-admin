package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImPrivateMessage;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.im.domain.vo.ImPrivateMessageVo;
import org.dromara.im.mapper.ImPrivateMessageMapper;
import org.dromara.im.service.IImPrivateMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 私聊消息Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImPrivateMessageServiceImpl implements IImPrivateMessageService {

    private final ImPrivateMessageMapper baseMapper;

    /**
     * 查询私聊消息
     *
     * @param id 主键
     * @return 私聊消息
     */
    @Override
    public ImPrivateMessageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询私聊消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 私聊消息分页列表
     */
    @Override
    public TableDataInfo<ImPrivateMessageVo> queryPageList(ImPrivateMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImPrivateMessage> wrapper = buildQueryWrapper(bo);
        Page<ImPrivateMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的私聊消息列表
     *
     * @param bo 查询条件
     * @return 私聊消息列表
     */
    @Override
    public List<ImPrivateMessageVo> queryList(ImPrivateMessageBo bo) {
        LambdaQueryWrapper<ImPrivateMessage> wrapper = buildQueryWrapper(bo);
        return baseMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<ImPrivateMessage> buildQueryWrapper(ImPrivateMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImPrivateMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getSendId() != null, ImPrivateMessage::getSendId, bo.getSendId());
        wrapper.eq(bo.getRecvId() != null, ImPrivateMessage::getRecvId, bo.getRecvId());
        wrapper.like(StringUtils.isNotBlank(bo.getContent()), ImPrivateMessage::getContent, bo.getContent());
        wrapper.eq(bo.getType() != null, ImPrivateMessage::getType, bo.getType());
        wrapper.eq(bo.getStatus() != null, ImPrivateMessage::getStatus, bo.getStatus());
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null, ImPrivateMessage::getSendTime,
            params.get("beginTime"), params.get("endTime"));
        wrapper.orderByDesc(ImPrivateMessage::getId);
        return wrapper;
    }

}

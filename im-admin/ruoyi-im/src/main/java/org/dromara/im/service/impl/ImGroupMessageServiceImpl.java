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
import org.dromara.im.domain.ImGroupMessage;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.domain.vo.ImGroupMessageVo;
import org.dromara.im.mapper.ImGroupMessageMapper;
import org.dromara.im.service.IImGroupMessageService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 群消息Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImGroupMessageServiceImpl implements IImGroupMessageService {

    private final ImGroupMessageMapper baseMapper;

    /**
     * 查询群消息
     *
     * @param id 主键
     * @return 群消息
     */
    @Override
    public ImGroupMessageVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询群消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群消息分页列表
     */
    @Override
    public TableDataInfo<ImGroupMessageVo> queryPageList(ImGroupMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImGroupMessage> wrapper = buildQueryWrapper(bo);
        Page<ImGroupMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<ImGroupMessage> buildQueryWrapper(ImGroupMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImGroupMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getGroupId() != null, ImGroupMessage::getGroupId, bo.getGroupId());
        wrapper.eq(bo.getSendId() != null, ImGroupMessage::getSendId, bo.getSendId());
        wrapper.like(StringUtils.isNotBlank(bo.getContent()), ImGroupMessage::getContent, bo.getContent());
        wrapper.eq(bo.getStatus() != null, ImGroupMessage::getStatus, bo.getStatus());
        wrapper.eq(bo.getType() != null, ImGroupMessage::getType, bo.getType());
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null, ImGroupMessage::getSendTime,
            params.get("beginTime"), params.get("endTime"));
        wrapper.orderByDesc(ImGroupMessage::getId);
        return wrapper;
    }

}

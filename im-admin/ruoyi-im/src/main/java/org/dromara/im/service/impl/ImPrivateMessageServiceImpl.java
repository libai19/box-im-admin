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
import org.dromara.im.domain.ImPrivateMessage;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.im.domain.vo.ImPrivateMessageVo;
import org.dromara.im.mapper.ImPrivateMessageMapper;
import org.dromara.im.service.IImPrivateMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    public ImPrivateMessageVo queryById(Long id){
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
        LambdaQueryWrapper<ImPrivateMessage> lqw = buildQueryWrapper(bo);
        Page<ImPrivateMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
        LambdaQueryWrapper<ImPrivateMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImPrivateMessage> buildQueryWrapper(ImPrivateMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImPrivateMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSendId() != null, ImPrivateMessage::getSendId, bo.getSendId());
        lqw.eq(bo.getRecvId() != null, ImPrivateMessage::getRecvId, bo.getRecvId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), ImPrivateMessage::getContent, bo.getContent());
        lqw.eq(bo.getType() != null, ImPrivateMessage::getType, bo.getType());
        lqw.eq(bo.getStatus() != null, ImPrivateMessage::getStatus, bo.getStatus());
        lqw.eq(bo.getSendTime() != null, ImPrivateMessage::getSendTime, bo.getSendTime());
        return lqw;
    }

    /**
     * 新增私聊消息
     *
     * @param bo 私聊消息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImPrivateMessageBo bo) {
        ImPrivateMessage add = MapstructUtils.convert(bo, ImPrivateMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改私聊消息
     *
     * @param bo 私聊消息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImPrivateMessageBo bo) {
        ImPrivateMessage update = MapstructUtils.convert(bo, ImPrivateMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImPrivateMessage entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除私聊消息信息
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

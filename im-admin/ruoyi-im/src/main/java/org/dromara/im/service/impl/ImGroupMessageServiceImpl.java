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
import org.dromara.im.domain.ImGroupMessage;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.domain.vo.ImGroupMessageVo;
import org.dromara.im.mapper.ImGroupMessageMapper;
import org.dromara.im.service.IImGroupMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
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
        LambdaQueryWrapper<ImGroupMessage> lqw = buildQueryWrapper(bo);
        Page<ImGroupMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的群消息列表
     *
     * @param bo 查询条件
     * @return 群消息列表
     */
    @Override
    public List<ImGroupMessageVo> queryList(ImGroupMessageBo bo) {
        LambdaQueryWrapper<ImGroupMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImGroupMessage> buildQueryWrapper(ImGroupMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImGroupMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGroupId() != null, ImGroupMessage::getGroupId, bo.getGroupId());
        lqw.eq(bo.getSendId() != null, ImGroupMessage::getSendId, bo.getSendId());
        lqw.like(StringUtils.isNotBlank(bo.getSendNickName()), ImGroupMessage::getSendNickName, bo.getSendNickName());
        lqw.eq(StringUtils.isNotBlank(bo.getAtUserIds()), ImGroupMessage::getAtUserIds, bo.getAtUserIds());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), ImGroupMessage::getContent, bo.getContent());
        lqw.eq(bo.getStatus() != null, ImGroupMessage::getStatus, bo.getStatus());
        lqw.eq(bo.getType() != null, ImGroupMessage::getType, bo.getType());
        lqw.eq(bo.getSendTime() != null, ImGroupMessage::getSendTime, bo.getSendTime());
        lqw.eq(bo.getReceiptOk() != null, ImGroupMessage::getReceiptOk, bo.getReceiptOk());
        lqw.eq(bo.getReceipt() != null, ImGroupMessage::getReceipt, bo.getReceipt());
        lqw.eq(StringUtils.isNotBlank(bo.getRecvIds()), ImGroupMessage::getRecvIds, bo.getRecvIds());
        return lqw;
    }

    /**
     * 新增群消息
     *
     * @param bo 群消息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImGroupMessageBo bo) {
        ImGroupMessage add = MapstructUtils.convert(bo, ImGroupMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改群消息
     *
     * @param bo 群消息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImGroupMessageBo bo) {
        ImGroupMessage update = MapstructUtils.convert(bo, ImGroupMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImGroupMessage entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除群消息信息
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

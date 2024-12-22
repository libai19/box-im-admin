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
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;
import org.dromara.im.mapper.ImSystemMessageMapper;
import org.dromara.im.service.IImSystemMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统消息Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImSystemMessageServiceImpl implements IImSystemMessageService {

    private final ImSystemMessageMapper baseMapper;

    /**
     * 查询系统消息
     *
     * @param id 主键
     * @return 系统消息
     */
    @Override
    public ImSystemMessageVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询系统消息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 系统消息分页列表
     */
    @Override
    public TableDataInfo<ImSystemMessageVo> queryPageList(ImSystemMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImSystemMessage> lqw = buildQueryWrapper(bo);
        Page<ImSystemMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的系统消息列表
     *
     * @param bo 查询条件
     * @return 系统消息列表
     */
    @Override
    public List<ImSystemMessageVo> queryList(ImSystemMessageBo bo) {
        LambdaQueryWrapper<ImSystemMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImSystemMessage> buildQueryWrapper(ImSystemMessageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImSystemMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), ImSystemMessage::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCoverUrl()), ImSystemMessage::getCoverUrl, bo.getCoverUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getIntro()), ImSystemMessage::getIntro, bo.getIntro());
        lqw.eq(bo.getContentType() != null, ImSystemMessage::getContentType, bo.getContentType());
        lqw.eq(StringUtils.isNotBlank(bo.getRichText()), ImSystemMessage::getRichText, bo.getRichText());
        lqw.eq(StringUtils.isNotBlank(bo.getExternLink()), ImSystemMessage::getExternLink, bo.getExternLink());
        lqw.eq(bo.getDeleted() != null, ImSystemMessage::getDeleted, bo.getDeleted());
        lqw.eq(bo.getCreator() != null, ImSystemMessage::getCreator, bo.getCreator());
        lqw.eq(bo.getUpdater() != null, ImSystemMessage::getUpdater, bo.getUpdater());
        return lqw;
    }

    /**
     * 新增系统消息
     *
     * @param bo 系统消息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImSystemMessageBo bo) {
        ImSystemMessage add = MapstructUtils.convert(bo, ImSystemMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImSystemMessageBo bo) {
        ImSystemMessage update = MapstructUtils.convert(bo, ImSystemMessage.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImSystemMessage entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除系统消息信息
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

package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.im.constant.ImConstant;
import org.springframework.stereotype.Service;
import org.dromara.im.domain.bo.ImGroupBo;
import org.dromara.im.domain.vo.ImGroupVo;
import org.dromara.im.domain.ImGroup;
import org.dromara.im.mapper.ImGroupMapper;
import org.dromara.im.service.IImGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 群Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImGroupServiceImpl implements IImGroupService {

    private final ImGroupMapper baseMapper;

    /**
     * 查询群
     *
     * @param id 主键
     * @return 群
     */
    @Override
    public ImGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询群列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群分页列表
     */
    @Override
    public TableDataInfo<ImGroupVo> queryPageList(ImGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImGroup> lqw = buildQueryWrapper(bo);
        Page<ImGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的群列表
     *
     * @param bo 查询条件
     * @return 群列表
     */
    @Override
    public List<ImGroupVo> queryList(ImGroupBo bo) {
        LambdaQueryWrapper<ImGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImGroup> buildQueryWrapper(ImGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ImGroup::getName, bo.getName());
        lqw.eq(bo.getOwnerId() != null, ImGroup::getOwnerId, bo.getOwnerId());
        lqw.eq(StringUtils.isNotBlank(bo.getHeadImage()), ImGroup::getHeadImage, bo.getHeadImage());
        lqw.eq(StringUtils.isNotBlank(bo.getHeadImageThumb()), ImGroup::getHeadImageThumb, bo.getHeadImageThumb());
        lqw.eq(StringUtils.isNotBlank(bo.getNotice()), ImGroup::getNotice, bo.getNotice());
        lqw.eq(bo.getDissolve() != null, ImGroup::getDissolve, bo.getDissolve());
        lqw.eq(bo.getCreatedTime() != null, ImGroup::getCreatedTime, bo.getCreatedTime());
        lqw.eq(bo.getIsBanned() != null, ImGroup::getIsBanned, bo.getIsBanned());
        lqw.eq(StringUtils.isNotBlank(bo.getReason()), ImGroup::getReason, bo.getReason());
        return lqw;
    }

    /**
     * 新增群
     *
     * @param bo 群
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImGroupBo bo) {
        ImGroup add = MapstructUtils.convert(bo, ImGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改群
     *
     * @param bo 群
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImGroupBo bo) {
        ImGroup update = MapstructUtils.convert(bo, ImGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除群信息
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

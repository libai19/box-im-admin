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
import org.dromara.im.domain.bo.ImGroupMemberBo;
import org.dromara.im.domain.vo.ImGroupMemberVo;
import org.dromara.im.domain.ImGroupMember;
import org.dromara.im.mapper.ImGroupMemberMapper;
import org.dromara.im.service.IImGroupMemberService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 群成员Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImGroupMemberServiceImpl implements IImGroupMemberService {

    private final ImGroupMemberMapper baseMapper;

    /**
     * 查询群成员
     *
     * @param id 主键
     * @return 群成员
     */
    @Override
    public ImGroupMemberVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询群成员列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群成员分页列表
     */
    @Override
    public TableDataInfo<ImGroupMemberVo> queryPageList(ImGroupMemberBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImGroupMember> lqw = buildQueryWrapper(bo);
        Page<ImGroupMemberVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的群成员列表
     *
     * @param bo 查询条件
     * @return 群成员列表
     */
    @Override
    public List<ImGroupMemberVo> queryList(ImGroupMemberBo bo) {
        LambdaQueryWrapper<ImGroupMember> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ImGroupMember> buildQueryWrapper(ImGroupMemberBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImGroupMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGroupId() != null, ImGroupMember::getGroupId, bo.getGroupId());
        lqw.eq(bo.getUserId() != null, ImGroupMember::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getRemarkNickName()), ImGroupMember::getRemarkNickName, bo.getRemarkNickName());
        lqw.eq(StringUtils.isNotBlank(bo.getHeadImage()), ImGroupMember::getHeadImage, bo.getHeadImage());
        lqw.like(StringUtils.isNotBlank(bo.getRemarkGroupName()), ImGroupMember::getRemarkGroupName, bo.getRemarkGroupName());
        lqw.eq(bo.getQuit() != null, ImGroupMember::getQuit, bo.getQuit());
        lqw.eq(bo.getCreatedTime() != null, ImGroupMember::getCreatedTime, bo.getCreatedTime());
        lqw.eq(bo.getQuitTime() != null, ImGroupMember::getQuitTime, bo.getQuitTime());
        lqw.like(StringUtils.isNotBlank(bo.getUserNickName()), ImGroupMember::getUserNickName, bo.getUserNickName());
        return lqw;
    }

    /**
     * 新增群成员
     *
     * @param bo 群成员
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImGroupMemberBo bo) {
        ImGroupMember add = MapstructUtils.convert(bo, ImGroupMember.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改群成员
     *
     * @param bo 群成员
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImGroupMemberBo bo) {
        ImGroupMember update = MapstructUtils.convert(bo, ImGroupMember.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ImGroupMember entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除群成员信息
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

package org.dromara.im.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImGroupMember;
import org.dromara.im.domain.bo.ImGroupMemberBo;
import org.dromara.im.domain.vo.ImGroupMemberVo;
import org.dromara.im.mapper.ImGroupMemberMapper;
import org.dromara.im.service.IImGroupMemberService;
import org.springframework.stereotype.Service;

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
        LambdaQueryWrapper<ImGroupMember> wrapper = buildQueryWrapper(bo);
        Page<ImGroupMemberVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        // 填充显示昵称
        result.getRecords().forEach(m -> {
            m.setShowNickName(StrUtil.isEmpty(m.getRemarkNickName()) ? m.getUserNickName() : m.getRemarkNickName());
        });
        return TableDataInfo.build(result);
    }


    @Override
    public Long findCountByGroupId(Long groupId) {
        LambdaQueryWrapper<ImGroupMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ImGroupMember::getGroupId, groupId);
        wrapper.eq(ImGroupMember::getQuit, false);
        return baseMapper.selectCount(wrapper);
    }

    private LambdaQueryWrapper<ImGroupMember> buildQueryWrapper(ImGroupMemberBo bo) {
        LambdaQueryWrapper<ImGroupMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(bo.getGroupId() != null, ImGroupMember::getGroupId, bo.getGroupId());
        wrapper.eq(bo.getUserId() != null, ImGroupMember::getUserId, bo.getUserId());
        wrapper.like(StringUtils.isNotBlank(bo.getRemarkNickName()), ImGroupMember::getRemarkNickName, bo.getRemarkNickName());
        wrapper.like(StringUtils.isNotBlank(bo.getRemarkGroupName()), ImGroupMember::getRemarkGroupName, bo.getRemarkGroupName());
        wrapper.like(StringUtils.isNotBlank(bo.getUserNickName()), ImGroupMember::getUserNickName, bo.getUserNickName());
        wrapper.eq(ImGroupMember::getQuit, false);
        wrapper.orderByDesc(ImGroupMember::getCreatedTime);
        return wrapper;
    }


}

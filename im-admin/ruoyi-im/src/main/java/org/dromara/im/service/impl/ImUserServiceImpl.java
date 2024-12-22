package org.dromara.im.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.constant.ImRedisKey;
import org.dromara.im.domain.ImUser;
import org.dromara.im.domain.bo.ImUserBo;
import org.dromara.im.domain.dto.ImUserBanDto;
import org.dromara.im.domain.dto.ImUserUnbanDto;
import org.dromara.im.domain.vo.ImUserVo;
import org.dromara.im.mapper.ImUserMapper;
import org.dromara.im.mq.ImRedisMQTemplate;
import org.dromara.im.service.IImUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImUserServiceImpl implements IImUserService {

    private final ImRedisMQTemplate redisMQTemplate;
    private final ImUserMapper baseMapper;

    /**
     * 查询用户
     *
     * @param id 主键
     * @return 用户
     */
    @Override
    public ImUserVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询用户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户分页列表
     */
    @Override
    public TableDataInfo<ImUserVo> queryPageList(ImUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImUser> lqw = buildQueryWrapper(bo);
        Page<ImUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的用户列表
     *
     * @param bo 查询条件
     * @return 用户列表
     */
    @Override
    public List<ImUserVo> queryList(ImUserBo bo) {
        LambdaQueryWrapper<ImUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }


    @Override
    public void ban(ImUserBanDto dto) {
        LambdaUpdateWrapper<ImUser> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ImUser::getId, dto.getId());
        wrapper.set(ImUser::getIsBanned, true);
        wrapper.set(ImUser::getReason, dto.getReason());
        baseMapper.update(wrapper);
        // 推送到处理队列，等待im-platfrom处理
        redisMQTemplate.opsForList().rightPush(ImRedisKey.IM_QUEUE_USER_BANNED, dto);
    }

    @Override
    public void unban(ImUserUnbanDto dto) {
        LambdaUpdateWrapper<ImUser> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ImUser::getId, dto.getId());
        wrapper.set(ImUser::getIsBanned, false);
        wrapper.set(ImUser::getReason, Strings.EMPTY);
        baseMapper.update(wrapper);
    }

    private LambdaQueryWrapper<ImUser> buildQueryWrapper(ImUserBo bo) {
        LambdaQueryWrapper<ImUser> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), ImUser::getUserName, bo.getUserName());
        lqw.like(StringUtils.isNotBlank(bo.getNickName()), ImUser::getNickName, bo.getNickName());
        lqw.orderByDesc(ImUser::getCreatedTime);
        return lqw;
    }

    @Override
    public List<ImUserVo> findByName(String name) {
        // 取出用户名或昵称匹配的前10条
        LambdaQueryWrapper<ImUser> queryWrapper = Wrappers.lambdaQuery();
        if(StrUtil.isNotEmpty(name)){
            queryWrapper.like(ImUser::getUserName, name);
        }
        queryWrapper.select(ImUser::getId, ImUser::getUserName);
        queryWrapper.orderByDesc(ImUser::getId);
        queryWrapper.last("limit 10");
        return baseMapper.selectVoList(queryWrapper);
    }

}

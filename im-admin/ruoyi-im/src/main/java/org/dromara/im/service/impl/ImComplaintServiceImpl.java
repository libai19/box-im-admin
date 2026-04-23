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
import org.dromara.im.domain.ImComplaint;
import org.dromara.im.domain.ImGroup;
import org.dromara.im.domain.ImUser;
import org.dromara.im.domain.bo.ImComplaintBo;
import org.dromara.im.domain.vo.ImComplaintVo;
import org.dromara.im.mapper.ImComplaintMapper;
import org.dromara.im.mapper.ImGroupMapper;
import org.dromara.im.mapper.ImUserMapper;
import org.dromara.im.service.IImComplaintService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImComplaintServiceImpl implements IImComplaintService {

    private final ImComplaintMapper baseMapper;
    private final ImUserMapper userMapper;
    private final ImGroupMapper groupMapper;

    @Override
    public ImComplaintVo queryById(Long id) {
        ImComplaintVo vo = baseMapper.selectVoById(id);
        fillTargetName(vo);
        return vo;
    }

    @Override
    public TableDataInfo<ImComplaintVo> queryPageList(ImComplaintBo bo, PageQuery pageQuery) {
        Page<ImComplaintVo> result = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        result.getRecords().forEach(this::fillTargetName);
        return TableDataInfo.build(result);
    }

    @Override
    public List<ImComplaintVo> queryList(ImComplaintBo bo) {
        List<ImComplaintVo> list = baseMapper.selectVoList(buildQueryWrapper(bo));
        list.forEach(this::fillTargetName);
        return list;
    }

    @Override
    public Boolean handle(ImComplaintBo bo) {
        ImComplaint complaint = MapstructUtils.convert(bo, ImComplaint.class);
        complaint.setHandleBy(LoginHelper.getUserId());
        complaint.setHandleTime(new Date());
        complaint.setUpdatedTime(new Date());
        if (complaint.getStatus() == null) {
            complaint.setStatus(2);
        }
        return baseMapper.updateById(complaint) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    private LambdaQueryWrapper<ImComplaint> buildQueryWrapper(ImComplaintBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ImComplaint> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(!Objects.isNull(bo.getUserId()), ImComplaint::getUserId, bo.getUserId());
        wrapper.eq(!Objects.isNull(bo.getTargetId()), ImComplaint::getTargetId, bo.getTargetId());
        wrapper.eq(!Objects.isNull(bo.getTargetType()), ImComplaint::getTargetType, bo.getTargetType());
        wrapper.eq(!Objects.isNull(bo.getType()), ImComplaint::getType, bo.getType());
        wrapper.eq(!Objects.isNull(bo.getStatus()), ImComplaint::getStatus, bo.getStatus());
        wrapper.like(StringUtils.isNotBlank(bo.getContent()), ImComplaint::getContent, bo.getContent());
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null, ImComplaint::getCreatedTime,
            params.get("beginTime"), params.get("endTime"));
        wrapper.orderByDesc(ImComplaint::getId);
        return wrapper;
    }

    private void fillTargetName(ImComplaintVo vo) {
        if (vo == null || vo.getTargetId() == null) {
            return;
        }
        if (Objects.equals(vo.getTargetType(), 2)) {
            ImGroup group = groupMapper.selectById(vo.getTargetId());
            vo.setTargetName(group == null ? String.valueOf(vo.getTargetId()) : group.getName());
        } else {
            ImUser user = userMapper.selectById(vo.getTargetId());
            vo.setTargetName(user == null ? String.valueOf(vo.getTargetId()) : user.getUserName());
        }
    }
}

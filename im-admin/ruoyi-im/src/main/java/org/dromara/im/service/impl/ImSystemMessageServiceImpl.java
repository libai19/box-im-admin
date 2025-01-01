package org.dromara.im.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSystemMessage;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;
import org.dromara.im.mapper.ImSystemMessageMapper;
import org.dromara.im.service.IImSmPushTaskService;
import org.dromara.im.service.IImSystemMessageService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    private final IImSmPushTaskService smPushTaskService;
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
        LambdaQueryWrapper<ImSystemMessage> wrapper = buildQueryWrapper(bo);
        Page<ImSystemMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<ImSystemMessage> buildQueryWrapper(ImSystemMessageBo bo) {
        LambdaQueryWrapper<ImSystemMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(bo.getTitle()), ImSystemMessage::getTitle, bo.getTitle());
        wrapper.eq(bo.getContentType() != null, ImSystemMessage::getContentType, bo.getContentType());
        wrapper.orderByDesc(ImSystemMessage::getId);
        return wrapper;
    }

    /**
     * 新增系统消息
     *
     * @param bo 系统消息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImSystemMessageBo bo) {
        ImSystemMessage message = MapstructUtils.convert(bo, ImSystemMessage.class);
        message.setDeleted(false);
        message.setCreator(LoginHelper.getUserId());
        message.setCreateTime(new Date());
        return  baseMapper.insert(message) > 0;
    }

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImSystemMessageBo bo) {
        ImSystemMessage message = MapstructUtils.convert(bo, ImSystemMessage.class);
        return baseMapper.updateById(message) > 0;
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
        if(smPushTaskService.isExistTask(ids)){
            throw new ServiceException("部分消息存在关联任务，删除失败");
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<ImSystemMessageVo> findByTitle(String title) {
        // 取出标题匹配的前10条
        LambdaQueryWrapper<ImSystemMessage> wrapper = Wrappers.lambdaQuery();
        if(StrUtil.isNotEmpty(title)){
            wrapper.like(ImSystemMessage::getTitle, title);
        }
        wrapper.select(ImSystemMessage::getId, ImSystemMessage::getTitle);
        wrapper.orderByDesc(ImSystemMessage::getId);
        wrapper.last("limit 10");
        return baseMapper.selectVoList(wrapper);

    }
}

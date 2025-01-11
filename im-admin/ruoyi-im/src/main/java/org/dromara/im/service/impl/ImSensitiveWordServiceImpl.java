package org.dromara.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImSensitiveWord;
import org.dromara.im.domain.bo.ImSensitiveWordBo;
import org.dromara.im.domain.vo.ImSensitiveWordVo;
import org.dromara.im.mapper.ImSensitiveWordMapper;
import org.dromara.im.service.IImSensitiveWordService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 敏感词Service业务层处理
 *
 * @author Blue
 * @date 2024-12-22
 */
@DS(ImConstant.DS_IM_PLATFORM)
@RequiredArgsConstructor
@Service
public class ImSensitiveWordServiceImpl implements IImSensitiveWordService {

    private final ImSensitiveWordMapper baseMapper;

    /**
     * 查询敏感词
     *
     * @param id 主键
     * @return 敏感词
     */
    @Override
    public ImSensitiveWordVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询敏感词列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 敏感词分页列表
     */
    @Override
    public TableDataInfo<ImSensitiveWordVo> queryPageList(ImSensitiveWordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ImSensitiveWord> wrapper = buildQueryWrapper(bo);
        Page<ImSensitiveWordVo> result = baseMapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的敏感词列表
     *
     * @param bo 查询条件
     * @return 敏感词列表
     */
    @Override
    public List<ImSensitiveWordVo> queryList(ImSensitiveWordBo bo) {
        LambdaQueryWrapper<ImSensitiveWord> wrapper = buildQueryWrapper(bo);
        return baseMapper.selectVoList(wrapper);
    }

    private LambdaQueryWrapper<ImSensitiveWord> buildQueryWrapper(ImSensitiveWordBo bo) {
        LambdaQueryWrapper<ImSensitiveWord> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(bo.getContent()), ImSensitiveWord::getContent, bo.getContent());
        wrapper.eq(bo.getEnabled() != null, ImSensitiveWord::getEnabled, bo.getEnabled());
        wrapper.orderByDesc(ImSensitiveWord::getId);
        return wrapper;
    }

    /**
     * 新增敏感词
     *
     * @param bo 敏感词
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(ImSensitiveWordBo bo) {
        ImSensitiveWord word = MapstructUtils.convert(bo, ImSensitiveWord.class);
        return baseMapper.insert(word) > 0;
    }

    /**
     * 修改敏感词
     *
     * @param bo 敏感词
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(ImSensitiveWordBo bo) {
        ImSensitiveWord word = MapstructUtils.convert(bo, ImSensitiveWord.class);
        return baseMapper.updateById(word) > 0;
    }


    /**
     * 校验并批量删除敏感词信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public Boolean setEnable(ImSensitiveWordBo bo) {
        LambdaUpdateWrapper<ImSensitiveWord> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ImSensitiveWord::getId,bo.getId());
        wrapper.set(ImSensitiveWord::getEnabled,bo.getEnabled());
        return this.baseMapper.update(wrapper) > 0;

    }
}

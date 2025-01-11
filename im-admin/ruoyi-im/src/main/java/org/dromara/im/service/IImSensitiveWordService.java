package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImSensitiveWordBo;
import org.dromara.im.domain.vo.ImSensitiveWordVo;

import java.util.Collection;
import java.util.List;

/**
 * 敏感词Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImSensitiveWordService {

    /**
     * 查询敏感词
     *
     * @param id 主键
     * @return 敏感词
     */
    ImSensitiveWordVo queryById(Long id);

    /**
     * 分页查询敏感词列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 敏感词分页列表
     */
    TableDataInfo<ImSensitiveWordVo> queryPageList(ImSensitiveWordBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的敏感词列表
     *
     * @param bo 查询条件
     * @return 敏感词列表
     */
    List<ImSensitiveWordVo> queryList(ImSensitiveWordBo bo);

    /**
     * 新增敏感词
     *
     * @param bo 敏感词
     * @return 是否新增成功
     */
    Boolean insertByBo(ImSensitiveWordBo bo);

    /**
     * 修改敏感词
     *
     * @param bo 敏感词
     * @return 是否修改成功
     */
    Boolean updateByBo(ImSensitiveWordBo bo);

    /**
     * 校验并批量删除敏感词信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 设置敏感词开关
     * @param bo 敏感词
     * @return
     */
    Boolean setEnable(ImSensitiveWordBo bo);
}

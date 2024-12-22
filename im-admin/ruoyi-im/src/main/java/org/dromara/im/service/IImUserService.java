package org.dromara.im.service;

import org.dromara.im.domain.dto.ImUserBanDTO;
import org.dromara.im.domain.dto.ImUserUnbanDTO;
import org.dromara.im.domain.vo.ImUserVo;
import org.dromara.im.domain.bo.ImUserBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImUserService {

    /**
     * 查询用户
     *
     * @param id 主键
     * @return 用户
     */
    ImUserVo queryById(Long id);

    /**
     * 分页查询用户列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 用户分页列表
     */
    TableDataInfo<ImUserVo> queryPageList(ImUserBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的用户列表
     *
     * @param bo 查询条件
     * @return 用户列表
     */
    List<ImUserVo> queryList(ImUserBo bo);

    /**
     *  封禁用户
     *
     * @param dto dto
     */
    void ban(ImUserBanDTO dto);

    /**
     *  解封用户
     *
     * @param dto dto
     */
    void unban(ImUserUnbanDTO dto);

}

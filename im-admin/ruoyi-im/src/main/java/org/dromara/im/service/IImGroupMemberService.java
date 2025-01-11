package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImGroupMemberBo;
import org.dromara.im.domain.vo.ImGroupMemberVo;

/**
 * 群成员Service接口
 *
 * @author Blue
 * @date 2024-12-22
 */
public interface IImGroupMemberService {

    /**
     * 查询群成员
     *
     * @param id 主键
     * @return 群成员
     */
    ImGroupMemberVo queryById(Long id);

    /**
     * 分页查询群成员列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 群成员分页列表
     */
    TableDataInfo<ImGroupMemberVo> queryPageList(ImGroupMemberBo bo, PageQuery pageQuery);



    /**
     * 查询群成员数量
     *
     * @param groupId 群id
     * @return 群成员数量
     */
    Long findCountByGroupId(Long groupId);


}

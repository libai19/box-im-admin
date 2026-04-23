package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImComplaintBo;
import org.dromara.im.domain.vo.ImComplaintVo;

import java.util.Collection;
import java.util.List;

public interface IImComplaintService {

    ImComplaintVo queryById(Long id);

    TableDataInfo<ImComplaintVo> queryPageList(ImComplaintBo bo, PageQuery pageQuery);

    List<ImComplaintVo> queryList(ImComplaintBo bo);

    Boolean handle(ImComplaintBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

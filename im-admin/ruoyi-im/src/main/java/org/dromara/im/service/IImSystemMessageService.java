package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;

import java.util.Collection;
import java.util.List;

public interface IImSystemMessageService {

    ImSystemMessageVo queryById(Long id);

    TableDataInfo<ImSystemMessageVo> queryPageList(ImSystemMessageBo bo, PageQuery pageQuery);

    List<ImSystemMessageVo> queryList(ImSystemMessageBo bo);

    Boolean insertByBo(ImSystemMessageBo bo);

    Boolean updateByBo(ImSystemMessageBo bo);

    Boolean push(Long id);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

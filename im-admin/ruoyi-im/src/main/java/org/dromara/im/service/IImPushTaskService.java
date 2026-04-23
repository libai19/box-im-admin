package org.dromara.im.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.im.domain.bo.ImPushTaskBo;
import org.dromara.im.domain.vo.ImPushTaskVo;

import java.util.Collection;
import java.util.List;

public interface IImPushTaskService {

    ImPushTaskVo queryById(Long id);

    TableDataInfo<ImPushTaskVo> queryPageList(ImPushTaskBo bo, PageQuery pageQuery);

    List<ImPushTaskVo> queryList(ImPushTaskBo bo);

    Boolean pushMessage(Long messageId);

    Boolean resend(Long id);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

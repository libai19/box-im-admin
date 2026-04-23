package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImPushTaskBo;
import org.dromara.im.domain.vo.ImPushTaskVo;
import org.dromara.im.service.IImPushTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/pushTask")
public class ImPushTaskController extends BaseController {

    private final IImPushTaskService pushTaskService;

    @SaCheckPermission("im:pushTask:list")
    @GetMapping("/list")
    public TableDataInfo<ImPushTaskVo> list(ImPushTaskBo bo, PageQuery pageQuery) {
        return pushTaskService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("im:pushTask:query")
    @GetMapping("/{id}")
    public R<ImPushTaskVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pushTaskService.queryById(id));
    }

    @SaCheckPermission("im:pushTask:resend")
    @Log(title = "推送任务", businessType = BusinessType.UPDATE)
    @PostMapping("/resend/{id}")
    public R<Void> resend(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return toAjax(pushTaskService.resend(id));
    }

    @SaCheckPermission("im:pushTask:remove")
    @Log(title = "推送任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(pushTaskService.deleteWithValidByIds(List.of(ids), true));
    }
}

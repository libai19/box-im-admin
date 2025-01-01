package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImSmPushTaskBo;
import org.dromara.im.domain.vo.ImSmPushTaskVo;
import org.dromara.im.service.IImSmPushTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息推送任务
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/smPushTask")
public class ImSmPushTaskController extends BaseController {

    private final IImSmPushTaskService imSmPushTaskService;

    /**
     * 查询系统消息推送任务列表
     */
    @SaCheckPermission("im:smPushTask:list")
    @GetMapping("/list")
    public TableDataInfo<ImSmPushTaskVo> list(ImSmPushTaskBo bo, PageQuery pageQuery) {
        return imSmPushTaskService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取系统消息推送任务详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:smPushTask:query")
    @GetMapping("/{id}")
    public R<ImSmPushTaskVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(imSmPushTaskService.queryById(id));
    }

    /**
     * 新增系统消息推送任务
     */
    @SaCheckPermission("im:smPushTask:add")
    @Log(title = "系统消息推送任务", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImSmPushTaskBo bo) {
        return toAjax(imSmPushTaskService.insertByBo(bo));
    }

    /**
     * 修改系统消息推送任务
     */
    @SaCheckPermission("im:smPushTask:edit")
    @Log(title = "系统消息推送任务", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImSmPushTaskBo bo) {
        return toAjax(imSmPushTaskService.updateByBo(bo));
    }

    /**
     * 删除系统消息推送任务
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:smPushTask:remove")
    @Log(title = "系统消息推送任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(imSmPushTaskService.deleteByIds(List.of(ids), true));
    }

    @PutMapping("cancel")
    @Log(title = "取消推送任务", businessType = BusinessType.UPDATE)
    @SaCheckPermission("im:smPushTask:edit")
    public R<Void> cancel(@RequestParam("id") Long id) {
        imSmPushTaskService.cancel(id);
        return R.ok();
    }

    @PutMapping("open")
    @Log(title = "开启推送任务", businessType = BusinessType.UPDATE)
    @SaCheckPermission("im:smPushTask:edit")
    public R<Void> open(@RequestParam("id") Long id) {
        imSmPushTaskService.open(id);
        return R.ok();
    }
}

package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImComplaintBo;
import org.dromara.im.domain.vo.ImComplaintVo;
import org.dromara.im.service.IImComplaintService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/complaint")
public class ImComplaintController extends BaseController {

    private final IImComplaintService complaintService;

    @SaCheckPermission("im:complaint:list")
    @GetMapping("/list")
    public TableDataInfo<ImComplaintVo> list(ImComplaintBo bo, PageQuery pageQuery) {
        return complaintService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("im:complaint:export")
    @Log(title = "用户投诉", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImComplaintBo bo, HttpServletResponse response) {
        List<ImComplaintVo> list = complaintService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户投诉", ImComplaintVo.class, response);
    }

    @SaCheckPermission("im:complaint:query")
    @GetMapping("/{id}")
    public R<ImComplaintVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(complaintService.queryById(id));
    }

    @SaCheckPermission("im:complaint:handle")
    @Log(title = "用户投诉", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/handle")
    public R<Void> handle(@Validated(EditGroup.class) @RequestBody ImComplaintBo bo) {
        return toAjax(complaintService.handle(bo));
    }

    @SaCheckPermission("im:complaint:remove")
    @Log(title = "用户投诉", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(complaintService.deleteWithValidByIds(List.of(ids), true));
    }
}

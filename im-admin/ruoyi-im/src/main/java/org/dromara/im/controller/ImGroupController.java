package org.dromara.im.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.im.domain.vo.ImGroupVo;
import org.dromara.im.domain.bo.ImGroupBo;
import org.dromara.im.service.IImGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 群
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/group")
public class ImGroupController extends BaseController {

    private final IImGroupService imGroupService;

    /**
     * 查询群列表
     */
    @SaCheckPermission("im:group:list")
    @GetMapping("/list")
    public TableDataInfo<ImGroupVo> list(ImGroupBo bo, PageQuery pageQuery) {
        return imGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出群列表
     */
    @SaCheckPermission("im:group:export")
    @Log(title = "群", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImGroupBo bo, HttpServletResponse response) {
        List<ImGroupVo> list = imGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "群", ImGroupVo.class, response);
    }

    /**
     * 获取群详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:group:query")
    @GetMapping("/{id}")
    public R<ImGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imGroupService.queryById(id));
    }

    /**
     * 新增群
     */
    @SaCheckPermission("im:group:add")
    @Log(title = "群", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImGroupBo bo) {
        return toAjax(imGroupService.insertByBo(bo));
    }

    /**
     * 修改群
     */
    @SaCheckPermission("im:group:edit")
    @Log(title = "群", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImGroupBo bo) {
        return toAjax(imGroupService.updateByBo(bo));
    }

    /**
     * 删除群
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:group:remove")
    @Log(title = "群", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imGroupService.deleteWithValidByIds(List.of(ids), true));
    }
}

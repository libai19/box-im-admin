package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImSystemMessageBo;
import org.dromara.im.domain.vo.ImSystemMessageVo;
import org.dromara.im.service.IImSystemMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/systemMessage")
public class ImSystemMessageController extends BaseController {

    private final IImSystemMessageService imSystemMessageService;

    /**
     * 查询系统消息列表
     */
    @SaCheckPermission("im:systemMessage:list")
    @GetMapping("/list")
    public TableDataInfo<ImSystemMessageVo> list(ImSystemMessageBo bo, PageQuery pageQuery) {
        return imSystemMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出系统消息列表
     */
    @SaCheckPermission("im:systemMessage:export")
    @Log(title = "系统消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImSystemMessageBo bo, HttpServletResponse response) {
        List<ImSystemMessageVo> list = imSystemMessageService.queryList(bo);
        ExcelUtil.exportExcel(list, "系统消息", ImSystemMessageVo.class, response);
    }

    /**
     * 获取系统消息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:systemMessage:query")
    @GetMapping("/{id}")
    public R<ImSystemMessageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imSystemMessageService.queryById(id));
    }

    /**
     * 新增系统消息
     */
    @SaCheckPermission("im:systemMessage:add")
    @Log(title = "系统消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImSystemMessageBo bo) {
        return toAjax(imSystemMessageService.insertByBo(bo));
    }

    /**
     * 修改系统消息
     */
    @SaCheckPermission("im:systemMessage:edit")
    @Log(title = "系统消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImSystemMessageBo bo) {
        return toAjax(imSystemMessageService.updateByBo(bo));
    }

    /**
     * 删除系统消息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:systemMessage:remove")
    @Log(title = "系统消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imSystemMessageService.deleteWithValidByIds(List.of(ids), true));
    }
}

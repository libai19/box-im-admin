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
import org.dromara.im.domain.vo.ImSensitiveWordVo;
import org.dromara.im.domain.bo.ImSensitiveWordBo;
import org.dromara.im.service.IImSensitiveWordService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 敏感词
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/sensitiveWord")
public class ImSensitiveWordController extends BaseController {

    private final IImSensitiveWordService imSensitiveWordService;

    /**
     * 查询敏感词列表
     */
    @SaCheckPermission("im:sensitiveWord:list")
    @GetMapping("/list")
    public TableDataInfo<ImSensitiveWordVo> list(ImSensitiveWordBo bo, PageQuery pageQuery) {
        return imSensitiveWordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出敏感词列表
     */
    @SaCheckPermission("im:sensitiveWord:export")
    @Log(title = "敏感词", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImSensitiveWordBo bo, HttpServletResponse response) {
        List<ImSensitiveWordVo> list = imSensitiveWordService.queryList(bo);
        ExcelUtil.exportExcel(list, "敏感词", ImSensitiveWordVo.class, response);
    }

    /**
     * 获取敏感词详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:sensitiveWord:query")
    @GetMapping("/{id}")
    public R<ImSensitiveWordVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imSensitiveWordService.queryById(id));
    }

    /**
     * 新增敏感词
     */
    @SaCheckPermission("im:sensitiveWord:add")
    @Log(title = "敏感词", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImSensitiveWordBo bo) {
        return toAjax(imSensitiveWordService.insertByBo(bo));
    }

    /**
     * 修改敏感词
     */
    @SaCheckPermission("im:sensitiveWord:edit")
    @Log(title = "敏感词", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImSensitiveWordBo bo) {
        return toAjax(imSensitiveWordService.updateByBo(bo));
    }

    /**
     * 删除敏感词
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:sensitiveWord:remove")
    @Log(title = "敏感词", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imSensitiveWordService.deleteWithValidByIds(List.of(ids), true));
    }
}

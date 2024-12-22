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
import org.dromara.im.domain.vo.ImGroupMessageVo;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.service.IImGroupMessageService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 群消息
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/groupMessage")
public class ImGroupMessageController extends BaseController {

    private final IImGroupMessageService imGroupMessageService;

    /**
     * 查询群消息列表
     */
    @SaCheckPermission("im:groupMessage:list")
    @GetMapping("/list")
    public TableDataInfo<ImGroupMessageVo> list(ImGroupMessageBo bo, PageQuery pageQuery) {
        return imGroupMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出群消息列表
     */
    @SaCheckPermission("im:groupMessage:export")
    @Log(title = "群消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImGroupMessageBo bo, HttpServletResponse response) {
        List<ImGroupMessageVo> list = imGroupMessageService.queryList(bo);
        ExcelUtil.exportExcel(list, "群消息", ImGroupMessageVo.class, response);
    }

    /**
     * 获取群消息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:groupMessage:query")
    @GetMapping("/{id}")
    public R<ImGroupMessageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imGroupMessageService.queryById(id));
    }

    /**
     * 新增群消息
     */
    @SaCheckPermission("im:groupMessage:add")
    @Log(title = "群消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImGroupMessageBo bo) {
        return toAjax(imGroupMessageService.insertByBo(bo));
    }

    /**
     * 修改群消息
     */
    @SaCheckPermission("im:groupMessage:edit")
    @Log(title = "群消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImGroupMessageBo bo) {
        return toAjax(imGroupMessageService.updateByBo(bo));
    }

    /**
     * 删除群消息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:groupMessage:remove")
    @Log(title = "群消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imGroupMessageService.deleteWithValidByIds(List.of(ids), true));
    }
}

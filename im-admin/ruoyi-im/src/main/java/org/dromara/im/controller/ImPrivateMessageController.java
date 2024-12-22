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
import org.dromara.im.domain.vo.ImPrivateMessageVo;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.im.service.IImPrivateMessageService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 私聊消息
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/privateMessage")
public class ImPrivateMessageController extends BaseController {

    private final IImPrivateMessageService imPrivateMessageService;

    /**
     * 查询私聊消息列表
     */
    @SaCheckPermission("im:privateMessage:list")
    @GetMapping("/list")
    public TableDataInfo<ImPrivateMessageVo> list(ImPrivateMessageBo bo, PageQuery pageQuery) {
        return imPrivateMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出私聊消息列表
     */
    @SaCheckPermission("im:privateMessage:export")
    @Log(title = "私聊消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImPrivateMessageBo bo, HttpServletResponse response) {
        List<ImPrivateMessageVo> list = imPrivateMessageService.queryList(bo);
        ExcelUtil.exportExcel(list, "私聊消息", ImPrivateMessageVo.class, response);
    }

    /**
     * 获取私聊消息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:privateMessage:query")
    @GetMapping("/{id}")
    public R<ImPrivateMessageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imPrivateMessageService.queryById(id));
    }

    /**
     * 新增私聊消息
     */
    @SaCheckPermission("im:privateMessage:add")
    @Log(title = "私聊消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImPrivateMessageBo bo) {
        return toAjax(imPrivateMessageService.insertByBo(bo));
    }

    /**
     * 修改私聊消息
     */
    @SaCheckPermission("im:privateMessage:edit")
    @Log(title = "私聊消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImPrivateMessageBo bo) {
        return toAjax(imPrivateMessageService.updateByBo(bo));
    }

    /**
     * 删除私聊消息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:privateMessage:remove")
    @Log(title = "私聊消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imPrivateMessageService.deleteWithValidByIds(List.of(ids), true));
    }
}

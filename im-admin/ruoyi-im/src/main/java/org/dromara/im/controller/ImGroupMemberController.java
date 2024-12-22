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
import org.dromara.im.domain.vo.ImGroupMemberVo;
import org.dromara.im.domain.bo.ImGroupMemberBo;
import org.dromara.im.service.IImGroupMemberService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 群成员
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/groupMember")
public class ImGroupMemberController extends BaseController {

    private final IImGroupMemberService imGroupMemberService;

    /**
     * 查询群成员列表
     */
    @SaCheckPermission("im:groupMember:list")
    @GetMapping("/list")
    public TableDataInfo<ImGroupMemberVo> list(ImGroupMemberBo bo, PageQuery pageQuery) {
        return imGroupMemberService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出群成员列表
     */
    @SaCheckPermission("im:groupMember:export")
    @Log(title = "群成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImGroupMemberBo bo, HttpServletResponse response) {
        List<ImGroupMemberVo> list = imGroupMemberService.queryList(bo);
        ExcelUtil.exportExcel(list, "群成员", ImGroupMemberVo.class, response);
    }

    /**
     * 获取群成员详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:groupMember:query")
    @GetMapping("/{id}")
    public R<ImGroupMemberVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(imGroupMemberService.queryById(id));
    }

    /**
     * 新增群成员
     */
    @SaCheckPermission("im:groupMember:add")
    @Log(title = "群成员", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ImGroupMemberBo bo) {
        return toAjax(imGroupMemberService.insertByBo(bo));
    }

    /**
     * 修改群成员
     */
    @SaCheckPermission("im:groupMember:edit")
    @Log(title = "群成员", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ImGroupMemberBo bo) {
        return toAjax(imGroupMemberService.updateByBo(bo));
    }

    /**
     * 删除群成员
     *
     * @param ids 主键串
     */
    @SaCheckPermission("im:groupMember:remove")
    @Log(title = "群成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(imGroupMemberService.deleteWithValidByIds(List.of(ids), true));
    }
}

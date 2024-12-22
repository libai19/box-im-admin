package org.dromara.im.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.im.domain.dto.ImUserBanDTO;
import org.dromara.im.domain.dto.ImUserUnbanDTO;
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
import org.dromara.im.domain.vo.ImUserVo;
import org.dromara.im.domain.bo.ImUserBo;
import org.dromara.im.service.IImUserService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 用户
 *
 * @author Blue
 * @date 2024-12-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/user")
public class ImUserController extends BaseController {

    private final IImUserService userService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("im:user:list")
    @GetMapping("/list")
    public TableDataInfo<ImUserVo> list(ImUserBo bo, PageQuery pageQuery) {
        return userService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("im:user:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ImUserBo bo, HttpServletResponse response) {
        List<ImUserVo> list = userService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", ImUserVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("im:user:query")
    @GetMapping("/{id}")
    public R<ImUserVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(userService.queryById(id));
    }

    @Operation(summary = "账号封禁")
    @PutMapping("/ban")
    @SaCheckPermission("im:user:ban")
    public void ban(@RequestBody @Valid ImUserBanDTO dto){
        userService.ban(dto);
    }


    @Operation(summary = "账号解封")
    @PutMapping("/unban")
    @SaCheckPermission("im:user:ban")
    public void unban(@RequestBody @Valid ImUserUnbanDTO dto){
        userService.unban(dto);
    }

}

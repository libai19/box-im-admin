package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImUserBo;
import org.dromara.im.domain.dto.ImUserBanDto;
import org.dromara.im.domain.dto.ImUserUnbanDto;
import org.dromara.im.domain.vo.ImUserVo;
import org.dromara.im.service.IImUserService;
import org.dromara.im.util.CommaTextUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public void ban(@RequestBody @Valid ImUserBanDto dto){
        userService.ban(dto);
    }


    @Operation(summary = "账号解封")
    @PutMapping("/unban")
    @SaCheckPermission("im:user:ban")
    public void unban(@RequestBody @Valid ImUserUnbanDto dto){
        userService.unban(dto);
    }

    @GetMapping("/findByName")
    @Operation(summary = "根据用户名查找用户",description = "用于下拉框选择用户")
    @SaCheckPermission("im:user:list")
    public R<List<ImUserVo>> findByName(@RequestParam String name){
        List<ImUserVo> vos = userService.findByName(name);
        return R.ok(vos);
    }

    @GetMapping("/findByIds")
    @Operation(summary = "根据id列表查找用户")
    @SaCheckPermission("im:user:list")
    public R<List<ImUserVo>> findByIds(@RequestParam String ids){
        List<Long> arrIds = CommaTextUtils.asList(ids).stream().map(Long::parseLong).collect(Collectors.toList());
        List<ImUserVo> vos = userService.findByIds(arrIds);
        return R.ok(vos);
    }
}

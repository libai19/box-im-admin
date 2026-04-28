package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.ImAppConfig;
import org.dromara.im.domain.bo.ImComplaintBo;
import org.dromara.im.domain.vo.ImComplaintVo;
import org.dromara.im.mapper.ImAppConfigMapper;
import org.dromara.im.service.IImComplaintService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/complaint")
public class ImComplaintController extends BaseController {

    private static final String COMPLAINT_NOTICE_TEMPLATE_KEY = "complaint.notice.template";
    private static final String DEFAULT_COMPLAINT_NOTICE_TEMPLATE = "用户{complainantName}举报了您，请注意文明用词";

    private final IImComplaintService complaintService;
    private final ImAppConfigMapper appConfigMapper;

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

    @SaCheckPermission("im:complaint:list")
    @GetMapping("/notice-template")
    public R<String> getNoticeTemplate() {
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, COMPLAINT_NOTICE_TEMPLATE_KEY)
            .last("limit 1"));
        if (Objects.isNull(config) || config.getConfigValue() == null || config.getConfigValue().trim().isEmpty()) {
            return R.ok(DEFAULT_COMPLAINT_NOTICE_TEMPLATE);
        }
        return R.ok(config.getConfigValue());
    }

    @SaCheckPermission("im:complaint:handle")
    @Log(title = "投诉提示", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/notice-template")
    public R<Void> updateNoticeTemplate(@RequestBody Map<String, String> body) {
        String value = body == null ? null : body.get("value");
        if (value == null || value.trim().isEmpty()) {
            return R.fail("投诉提示不能为空");
        }
        value = value.trim();
        if (value.length() > 20) {
            return R.fail("投诉提示不能超过20字");
        }
        Date now = new Date();
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, COMPLAINT_NOTICE_TEMPLATE_KEY)
            .last("limit 1"));
        if (Objects.isNull(config)) {
            config = new ImAppConfig();
            config.setConfigKey(COMPLAINT_NOTICE_TEMPLATE_KEY);
            config.setConfigValue(value);
            config.setRemark("用户被投诉后的实时提示词，支持 {complainantName} 和 {complainantId}");
            config.setCreatedTime(now);
            config.setUpdatedTime(now);
            appConfigMapper.insert(config);
        } else {
            config.setConfigValue(value);
            config.setUpdatedTime(now);
            appConfigMapper.updateById(config);
        }
        return R.ok();
    }

    @SaCheckPermission("im:complaint:remove")
    @Log(title = "用户投诉", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(complaintService.deleteWithValidByIds(List.of(ids), true));
    }
}

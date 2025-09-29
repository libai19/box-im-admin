package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImGroupMessageBo;
import org.dromara.im.domain.vo.ImGroupMessageVo;
import org.dromara.im.service.IImGroupMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * 按天统计群聊消息量
     *
     * @param days 统计天数，默认7天
     */
    @SaCheckPermission("im:groupMessage:list")
    @GetMapping("/dailyCount")
    public R<List<Map<String, Object>>> getDailyGroupMessageCount(@RequestParam(defaultValue = "7") Integer days) {
        return R.ok(imGroupMessageService.getDailyGroupMessageCount(days));
    }

}

package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.domain.bo.ImPrivateMessageBo;
import org.dromara.im.domain.vo.ImPrivateMessageVo;
import org.dromara.im.service.IImPrivateMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * 按天统计私聊消息量
     *
     * @param days 统计天数，默认7天
     */
    @SaCheckPermission("im:privateMessage:list")
    @GetMapping("/dailyCount")
    public R<List<Map<String, Object>>> getDailyMessageCount(@RequestParam(value = "days", defaultValue = "7") Integer days) {
        return R.ok(imPrivateMessageService.getDailyMessageCount(days));
    }

}

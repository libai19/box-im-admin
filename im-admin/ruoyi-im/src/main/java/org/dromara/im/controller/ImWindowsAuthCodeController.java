package org.dromara.im.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.im.constant.ImConstant;
import org.dromara.im.domain.ImAppConfig;
import org.dromara.im.domain.dto.ImWindowsAuthCodeDto;
import org.dromara.im.mapper.ImAppConfigMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/windows-auth-code")
public class ImWindowsAuthCodeController extends BaseController {

    public static final String WINDOWS_AUTH_CODE_CONFIG_KEY = "client.windows.auth-code";
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ImAppConfigMapper appConfigMapper;

    @SaCheckPermission("im:windowsAuthCode:view")
    @DS(ImConstant.DS_IM_PLATFORM)
    @GetMapping
    public R<ImWindowsAuthCodeDto> getConfig() {
        return R.ok(ensureTodayCode());
    }

    @SaCheckPermission("im:windowsAuthCode:edit")
    @DS(ImConstant.DS_IM_PLATFORM)
    @Log(title = "Windows今日授权码", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/refresh")
    public R<ImWindowsAuthCodeDto> refreshCode() {
        return R.ok(refreshTodayCode());
    }

    private synchronized ImWindowsAuthCodeDto ensureTodayCode() {
        ImWindowsAuthCodeDto dto = loadConfig();
        String today = today();
        if (!today.equals(dto.getCodeDate()) || StringUtils.isBlank(dto.getCode())) {
            dto = newCode(today);
            saveConfig(dto);
        }
        return dto;
    }

    private synchronized ImWindowsAuthCodeDto refreshTodayCode() {
        ImWindowsAuthCodeDto dto = newCode(today());
        saveConfig(dto);
        return dto;
    }

    private ImWindowsAuthCodeDto loadConfig() {
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, WINDOWS_AUTH_CODE_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config) || StringUtils.isBlank(config.getConfigValue())) {
            return new ImWindowsAuthCodeDto();
        }
        ImWindowsAuthCodeDto dto = JSON.parseObject(config.getConfigValue(), ImWindowsAuthCodeDto.class);
        return dto == null ? new ImWindowsAuthCodeDto() : dto;
    }

    private void saveConfig(ImWindowsAuthCodeDto dto) {
        Date now = new Date();
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, WINDOWS_AUTH_CODE_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config)) {
            config = new ImAppConfig();
            config.setConfigKey(WINDOWS_AUTH_CODE_CONFIG_KEY);
            config.setConfigValue(JSON.toJSONString(dto));
            config.setRemark("Windows客户端今日授权码");
            config.setCreatedTime(now);
            config.setUpdatedTime(now);
            appConfigMapper.insert(config);
        } else {
            config.setConfigValue(JSON.toJSONString(dto));
            config.setUpdatedTime(now);
            appConfigMapper.updateById(config);
        }
    }

    private ImWindowsAuthCodeDto newCode(String date) {
        ImWindowsAuthCodeDto dto = new ImWindowsAuthCodeDto();
        dto.setEnabled(true);
        dto.setCode(String.format("%06d", RANDOM.nextInt(1_000_000)));
        dto.setCodeDate(date);
        dto.setGeneratedAt(LocalDateTime.now(ZONE).format(DATE_TIME_FORMATTER));
        return dto;
    }

    private String today() {
        return LocalDate.now(ZONE).toString();
    }
}

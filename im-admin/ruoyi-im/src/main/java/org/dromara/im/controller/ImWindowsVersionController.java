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
import org.dromara.im.domain.dto.ImWindowsVersionDto;
import org.dromara.im.mapper.ImAppConfigMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/windows-version")
public class ImWindowsVersionController extends BaseController {

    public static final String WINDOWS_VERSION_CONFIG_KEY = "client.windows.version";

    private final ImAppConfigMapper appConfigMapper;

    @SaCheckPermission("im:windowsVersion:view")
    @DS(ImConstant.DS_IM_PLATFORM)
    @GetMapping
    public R<ImWindowsVersionDto> getConfig() {
        return R.ok(loadConfig());
    }

    @SaCheckPermission("im:windowsVersion:view")
    @DS(ImConstant.DS_IM_PLATFORM)
    @GetMapping("/generate")
    public R<ImWindowsVersionDto> generateConfig() {
        ImWindowsVersionDto dto = loadConfig();
        dto.setLatestVersion(StringUtils.defaultIfBlank(dto.getLatestVersion(), "1.0.0"));
        dto.setMinVersion(StringUtils.defaultIfBlank(dto.getMinVersion(), dto.getLatestVersion()));
        dto.setSetupUrl(firstNotBlank(dto.getSetupUrl(), releaseUrl("setup")));
        dto.setExeUrl(firstNotBlank(dto.getExeUrl(), releaseUrl("exe")));
        dto.setSha256(firstNotBlank(dto.getSha256(), sha256(firstExistingFile(setupPath(), exePath()))));
        dto.setReleaseNotes(StringUtils.defaultIfBlank(dto.getReleaseNotes(), "Windows 客户端体验优化与稳定性更新。"));
        return R.ok(dto);
    }

    @SaCheckPermission("im:windowsVersion:edit")
    @DS(ImConstant.DS_IM_PLATFORM)
    @Log(title = "Windows客户端版本", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> updateConfig(@RequestBody ImWindowsVersionDto dto) {
        if (dto == null) {
            return R.fail("配置不能为空");
        }
        normalize(dto);
        String validation = validateConfig(dto);
        if (validation != null) {
            return R.fail(validation);
        }
        Date now = new Date();
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, WINDOWS_VERSION_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config)) {
            config = new ImAppConfig();
            config.setConfigKey(WINDOWS_VERSION_CONFIG_KEY);
            config.setCreatedTime(now);
            config.setRemark("Windows客户端版本配置");
            config.setConfigValue(JSON.toJSONString(dto));
            config.setUpdatedTime(now);
            appConfigMapper.insert(config);
        } else {
            config.setConfigValue(JSON.toJSONString(dto));
            config.setUpdatedTime(now);
            appConfigMapper.updateById(config);
        }
        return R.ok();
    }

    private ImWindowsVersionDto loadConfig() {
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, WINDOWS_VERSION_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config) || StringUtils.isBlank(config.getConfigValue())) {
            return defaultConfig();
        }
        ImWindowsVersionDto dto = JSON.parseObject(config.getConfigValue(), ImWindowsVersionDto.class);
        return dto == null ? defaultConfig() : dto;
    }

    private ImWindowsVersionDto defaultConfig() {
        ImWindowsVersionDto dto = new ImWindowsVersionDto();
        dto.setEnabled(false);
        dto.setLatestVersion("1.0.0");
        dto.setMinVersion("1.0.0");
        dto.setForceUpdate(false);
        return dto;
    }

    private void normalize(ImWindowsVersionDto dto) {
        dto.setEnabled(Boolean.TRUE.equals(dto.getEnabled()));
        dto.setForceUpdate(Boolean.TRUE.equals(dto.getForceUpdate()));
        dto.setLatestVersion(StringUtils.defaultIfBlank(dto.getLatestVersion(), "1.0.0").trim());
        dto.setMinVersion(StringUtils.defaultIfBlank(dto.getMinVersion(), dto.getLatestVersion()).trim());
        dto.setExeUrl(StringUtils.trimToNull(dto.getExeUrl()));
        dto.setSetupUrl(StringUtils.trimToNull(dto.getSetupUrl()));
        dto.setSha256(StringUtils.trimToNull(dto.getSha256()));
        dto.setReleaseNotes(StringUtils.trimToEmpty(dto.getReleaseNotes()));
    }

    private String validateConfig(ImWindowsVersionDto dto) {
        if (!Boolean.TRUE.equals(dto.getEnabled())) {
            return null;
        }
        if (StringUtils.isBlank(dto.getLatestVersion())) {
            return "最新版本不能为空";
        }
        if (StringUtils.isBlank(dto.getSetupUrl()) && StringUtils.isBlank(dto.getExeUrl())) {
            return "安装包地址和EXE地址至少填写一个";
        }
        return null;
    }

    private String releaseUrl(String type) {
        String configured = System.getProperty("zhixiao.windows." + type + "Url");
        if (StringUtils.isNotBlank(configured)) {
            return configured.trim();
        }
        String fileName = "setup".equals(type) ? "晓信-Setup.exe" : "晓信.exe";
        return "https://im.zhixiaochaxun.com/download/windows/" + fileName;
    }

    private Path setupPath() {
        return Path.of(System.getProperty("zhixiao.windows.setupPath", "/opt/zhixiao/www/download/windows/晓信-Setup.exe"));
    }

    private Path exePath() {
        return Path.of(System.getProperty("zhixiao.windows.exePath", "/opt/zhixiao/www/download/windows/晓信.exe"));
    }

    private Path firstExistingFile(Path... paths) {
        for (Path path : paths) {
            if (path != null && Files.isRegularFile(path)) {
                return path;
            }
        }
        return null;
    }

    private String sha256(Path path) {
        if (path == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream input = new DigestInputStream(Files.newInputStream(path), digest)) {
                byte[] buffer = new byte[8192];
                while (input.read(buffer) != -1) {
                    // DigestInputStream updates the digest while bytes are consumed.
                }
            }
            StringBuilder hex = new StringBuilder();
            for (byte b : digest.digest()) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (IOException | NoSuchAlgorithmException ignored) {
            return null;
        }
    }

    private String firstNotBlank(String first, String second) {
        return StringUtils.isNotBlank(first) ? first : StringUtils.trimToNull(second);
    }
}

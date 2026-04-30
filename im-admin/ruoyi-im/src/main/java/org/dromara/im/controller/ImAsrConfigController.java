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
import org.dromara.im.domain.dto.ImAsrConfigDto;
import org.dromara.im.mapper.ImAppConfigMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/im/asr-config")
public class ImAsrConfigController extends BaseController {

    private static final String SPEECH_ASR_CONFIG_KEY = "speech.asr.config";

    private final ImAppConfigMapper appConfigMapper;

    @SaCheckPermission("im:asrConfig:view")
    @DS(ImConstant.DS_IM_PLATFORM)
    @GetMapping
    public R<ImAsrConfigDto> getConfig() {
        ImAppConfig config = appConfigMapper.selectOne(Wrappers.<ImAppConfig>lambdaQuery()
            .eq(ImAppConfig::getConfigKey, SPEECH_ASR_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config) || StringUtils.isBlank(config.getConfigValue())) {
            return R.ok(defaultConfig());
        }
        ImAsrConfigDto dto = JSON.parseObject(config.getConfigValue(), ImAsrConfigDto.class);
        return R.ok(dto == null ? defaultConfig() : dto);
    }

    @SaCheckPermission("im:asrConfig:edit")
    @DS(ImConstant.DS_IM_PLATFORM)
    @Log(title = "语音识别接口", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> updateConfig(@RequestBody ImAsrConfigDto dto) {
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
            .eq(ImAppConfig::getConfigKey, SPEECH_ASR_CONFIG_KEY)
            .last("limit 1"));
        if (Objects.isNull(config)) {
            config = new ImAppConfig();
            config.setConfigKey(SPEECH_ASR_CONFIG_KEY);
            config.setCreatedTime(now);
            config.setRemark("语音识别接口配置");
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

    private ImAsrConfigDto defaultConfig() {
        ImAsrConfigDto dto = new ImAsrConfigDto();
        dto.setEnabled(false);
        dto.setProvider("tencent");
        dto.setRegion("ap-guangzhou");
        dto.setEngineModelType("16k_zh");
        return dto;
    }

    private void normalize(ImAsrConfigDto dto) {
        dto.setEnabled(Boolean.TRUE.equals(dto.getEnabled()));
        dto.setProvider(StringUtils.defaultIfBlank(dto.getProvider(), "tencent").trim());
        dto.setRegion(StringUtils.defaultIfBlank(dto.getRegion(), "ap-guangzhou").trim());
        dto.setEngineModelType(StringUtils.defaultIfBlank(dto.getEngineModelType(), "16k_zh").trim());
    }

    private String validateConfig(ImAsrConfigDto dto) {
        if (!Boolean.TRUE.equals(dto.getEnabled())) {
            return null;
        }
        String provider = dto.getProvider();
        if ("tencent".equals(provider)) {
            if (StringUtils.isAnyBlank(dto.getSecretId(), dto.getSecretKey())) {
                return "腾讯云 SecretId 和 SecretKey 不能为空";
            }
            return null;
        }
        if ("aliyun".equals(provider)) {
            if (StringUtils.isAnyBlank(dto.getAccessKeyId(), dto.getAccessKeySecret(), dto.getAppKey())) {
                return "阿里云 AccessKeyId、AccessKeySecret、AppKey 不能为空";
            }
            return null;
        }
        if ("baidu".equals(provider)) {
            if (StringUtils.isAnyBlank(dto.getApiKey(), dto.getSecretKey())) {
                return "百度 API Key 和 Secret Key 不能为空";
            }
            return null;
        }
        if ("iflytek".equals(provider)) {
            if (StringUtils.isAnyBlank(dto.getAppId(), dto.getApiKey(), dto.getApiSecret())) {
                return "讯飞 AppId、ApiKey、ApiSecret 不能为空";
            }
            return null;
        }
        if ("volcengine".equals(provider)) {
            if (StringUtils.isAnyBlank(dto.getAccessKeyId(), dto.getSecretAccessKey(), dto.getAppId(), dto.getCluster())) {
                return "火山引擎 AccessKeyId、SecretAccessKey、AppId、Cluster 不能为空";
            }
            return null;
        }
        return "不支持的语音识别平台";
    }
}

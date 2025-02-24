package org.dromara.common.web.core;

import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 获取请求头国际化信息
 *
 * @author Lion Li
 */
public class I18nLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String languageHeader = httpServletRequest.getHeader("content-language");
        Locale locale;

        if (languageHeader != null && !languageHeader.isEmpty()) {
            // 处理可能的逗号分隔的语言列表
            String[] languages = languageHeader.split(",");
            String preferredLanguage = languages[0]; // 取第一个语言标签作为首选

            // 处理语言标签的格式（可能包含语言、国家、区域）
            String[] parts = preferredLanguage.split("-");
            switch (parts.length) {
                case 1: // 只有语言代码
                    locale = new Locale(parts[0]);
                    break;
                case 2: // 语言代码和国家代码
                    locale = new Locale(parts[0], parts[1]);
                    break;
                case 3: // 语言代码、国家代码和区域代码
                    locale = new Locale(parts[0], parts[1], parts[2]);
                    break;
                default: // 默认回退到默认语言环境
                    locale = Locale.getDefault();
                    break;
            }
        } else {
            // 如果没有 content-language 头部，使用默认语言环境
            locale = Locale.getDefault();
        }

        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
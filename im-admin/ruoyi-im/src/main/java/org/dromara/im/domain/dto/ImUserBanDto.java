package org.dromara.im.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: Blue
 * @date: 2024-07-14
 * @version: 1.0
 */
@Data
@Schema(description = "用户锁定")
public class ImUserBanDto {

    @NotNull(message = "用户id不可为空")
    @Schema(description = "用户id")
    private Long id;

    @Length(max = 128, message = "封禁原因长度不能超过128")
    @Schema(description = "锁定原因")
    private String reason;
}

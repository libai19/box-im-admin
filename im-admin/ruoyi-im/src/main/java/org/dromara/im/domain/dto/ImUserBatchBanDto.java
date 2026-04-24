package org.dromara.im.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Schema(description = "用户批量锁定")
public class ImUserBatchBanDto {

    @NotEmpty(message = "用户id不可为空")
    @Schema(description = "用户id列表")
    private List<Long> ids;

    @Length(max = 128, message = "封禁原因长度不能超过128")
    @Schema(description = "锁定原因")
    private String reason;
}

package org.dromara.im.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImGroupStatusDto {

    @NotNull(message = "群组id不可为空")
    private Long id;

    private Boolean dissolve;

    private Boolean muted;
}

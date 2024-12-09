package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * UserRedEnvelopeReceiveRequest
 * @author wtj
 * @date 2024/11/09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvelopeReceiveRequest对象", description = "用户领取红包对象")
public class UserRedEnvelopeReceiveRequest {

    @NotNull(message = "红包id不能为空")
    @ApiModelProperty(value = "红包id")
    private Integer id;

    @NotNull(message = "广告id不能为空")
    @ApiModelProperty(value = "广告id")
    private Integer adId;
}

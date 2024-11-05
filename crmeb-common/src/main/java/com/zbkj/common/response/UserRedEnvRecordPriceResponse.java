package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * UserRedEnvelopeRecordListResponse
 * 获取待领取红包数量、昨日收益、历史收益、当前余额
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvRecordPriceResponse对象", description = "用户红包情况")
public class UserRedEnvRecordPriceResponse {

    @ApiModelProperty(value = "记录id")
    private Integer id;

    @ApiModelProperty(value = "金额")
    private BigDecimal price;
}

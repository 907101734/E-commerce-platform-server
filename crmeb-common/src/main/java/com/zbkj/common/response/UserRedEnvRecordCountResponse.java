package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserRedEnvelopeRecordListResponse
 * 获取待领取红包数量、昨日收益、历史收益、当前余额
 * @author wtj
 * @date 2024/11/04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvRecordCountResponse对象", description = "用户收益情况")
public class UserRedEnvRecordCountResponse {

    @ApiModelProperty(value = "待领取数量")
    private Integer waitCount;

    @ApiModelProperty(value = "昨日收益")
    private Double yesterdayIncome;

    @ApiModelProperty(value = "当前余额")
    private Double currentIncome;

    @ApiModelProperty(value = "历史收益")
    private Double hisIncome;
}

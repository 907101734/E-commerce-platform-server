package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserRedEnvelopeRecordRequest
 * @author wtj
 * @date 2024/10/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRedEnvelopeRecordRequest对象", description = " 红包管理表")
public class UserRedEnvelopeRecordRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "添加时间")
    private String dateLimit;

    @ApiModelProperty(value = "领取时间")
    private String receiveDateLimit;

    @ApiModelProperty(value = "用户昵称搜索")
    private String nikeName;

    @ApiModelProperty(value = "状态：0-待领取，1-已领取")
    private Integer status;

}

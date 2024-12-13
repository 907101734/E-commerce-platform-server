package com.zbkj.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * GiftTypeVo
 * @author wtj
 * @date 2024/11/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GiftTypeVo对象", description = "礼品属性")
public class GiftTypeVo {

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private BigDecimal value;

    @ApiModelProperty(value = "是否启用")
    private Boolean enable;
}

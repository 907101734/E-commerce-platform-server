package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * UserRechargeReviewRequest
 * @author wtj
 * @date 2024/12/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRechargeReviewRequest对象", description = "用户充值记录审核对象")
public class UserRechargeConfirmRequest {

    @NotBlank(message = "充值记录不能为空")
    @ApiModelProperty(value = "充值记录id")
    private Integer userRechargeId;

    @NotBlank(message = "审核不能为空")
    @ApiModelProperty(value = "审核动作 0不通过，1通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "财务备注信息")
    private String remark;
}
